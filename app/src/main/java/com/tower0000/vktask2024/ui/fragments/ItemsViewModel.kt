package com.tower0000.vktask2024.ui.fragments

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tower0000.vktask2024.data.model.Item
import com.tower0000.vktask2024.data.model.ItemListResponse
import com.tower0000.vktask2024.domain.usecase.GetItemListUseCase
import com.tower0000.vktask2024.domain.usecase.SearchItemUseCase
import com.tower0000.vktask2024.ui.util.ResourceState
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

const val ITEMS_LIMIT = 20
const val FIRST_PAGE_INDEX = 0

@HiltViewModel
class ItemsViewModel @Inject constructor(
    private val getItemListUseCase: GetItemListUseCase,
    private val searchItemUseCase: SearchItemUseCase
) : ViewModel() {
    private val _itemsData =
        MutableLiveData<ResourceState<ItemListResponse>>(ResourceState.Unspecified())
    val itemsData: LiveData<ResourceState<ItemListResponse>> = _itemsData

    private val pagingInfo = PagingInfo()
    private val productsMap = mutableMapOf<Int, Item>()
    private val compositeDisposable = CompositeDisposable()

    init {
        loadItems()
    }

    fun loadItems() {
        _itemsData.postValue(ResourceState.Loading())
        compositeDisposable.add(
            getItemListUseCase.execute(
                skip = pagingInfo.page * pagingInfo.limit,
                limit = pagingInfo.limit
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    it.products.forEach { item -> productsMap[item.itemId] = item }
                    val formattedMapValues = ItemListResponse(productsMap.values.toList())
                    _itemsData.postValue(ResourceState.Success(formattedMapValues))
                    Log.d("ItemsViewModel", "$it")
                }, { e ->
                    _itemsData.postValue(ResourceState.Error(e.message.toString()))
                    e.printStackTrace()
                })
        )
        pagingInfo.page++
    }

    fun searchItems(query: String) {
        _itemsData.postValue(ResourceState.Loading())
        compositeDisposable.add(
            searchItemUseCase.execute(
                query,
                skip = pagingInfo.searchPage * pagingInfo.limit,
                limit = pagingInfo.limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _itemsData.postValue(ResourceState.Success(it))
                    Log.d("ItemsViewModel", "$it")
                }, { e ->
                    _itemsData.postValue(ResourceState.Error(e.message.toString()))
                    e.printStackTrace()
                })
        )
        pagingInfo.searchPage++
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun resetSearchPage() {
        pagingInfo.searchPage = FIRST_PAGE_INDEX
    }
}

internal data class PagingInfo(
    var page: Int = FIRST_PAGE_INDEX,
    var searchPage: Int = FIRST_PAGE_INDEX,
    val limit: Int = ITEMS_LIMIT
)