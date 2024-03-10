package com.tower0000.vktask2024.ui.fragments

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tower0000.vktask2024.data.model.Item
import com.tower0000.vktask2024.data.model.ItemListResponse
import com.tower0000.vktask2024.domain.usecase.GetCategoriesUseCase
import com.tower0000.vktask2024.domain.usecase.GetItemListUseCase
import com.tower0000.vktask2024.domain.usecase.GetItemsByCategoryUseCase
import com.tower0000.vktask2024.domain.usecase.SearchItemUseCase
import com.tower0000.vktask2024.ui.util.PageMode
import com.tower0000.vktask2024.ui.util.ResourceState
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

const val ITEMS_LIMIT = 20
const val FIRST_PAGE_INDEX = 0

@HiltViewModel
class ItemsViewModel @Inject constructor(
    private val getItemListUseCase: GetItemListUseCase,
    private val searchItemUseCase: SearchItemUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getItemsByCategoryUseCase: GetItemsByCategoryUseCase
) : ViewModel() {

    private val _itemsData =
        MutableLiveData<ResourceState<ItemListResponse>>(ResourceState.Unspecified())
    val itemsData: LiveData<ResourceState<ItemListResponse>> = _itemsData

    private val _categories =
        MutableLiveData<ResourceState<List<String>>>(ResourceState.Unspecified())
    val categories: LiveData<ResourceState<List<String>>> = _categories

    private val _pageMode = MutableLiveData<PageMode<String>>(PageMode.Base())
    val pageMode: LiveData<PageMode<String>> = _pageMode

    private val pagingInfo = PagingInfo()
    private val productsMap = mutableMapOf<Int, Item>()
    private val compositeDisposable = CompositeDisposable()

    init {
        getAllProducts()
        getCategories()
    }

    fun loadMoreItems() {
        when (_pageMode.value) {
            is PageMode.Base -> getAllProducts()
            is PageMode.Search -> getProductsByQuery(pagingInfo.query)
            is PageMode.Category -> getProductsByCategory(pagingInfo.category)
            else -> Unit
        }
    }

    fun switchToSearchMode(query: String) {
        resetItemPages()
        getProductsByQuery(query)
        _pageMode.postValue(PageMode.Search(query))
        pagingInfo.query = query
    }

    fun switchToBaseMode() {
        resetItemPages()
        getAllProducts()
        _pageMode.postValue(PageMode.Base())
    }

    fun switchToCategoryMode(category: String) {
        resetItemPages()
        getProductsByCategory(category)
        _pageMode.postValue(PageMode.Category(category))
        pagingInfo.category = category
    }

    private fun getCategories() {
        _categories.postValue(ResourceState.Loading())
        compositeDisposable.add(
            getCategoriesUseCase.execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    _categories.postValue(ResourceState.Success(response))
                    Log.d("ItemsViewModel", "$response")
                }, { e ->
                    _categories.postValue(ResourceState.Error(e.message.toString()))
                    e.printStackTrace()
                })
        )
    }

    private fun fetchItems(
        useCase: Single<ItemListResponse>
    ) {
        _itemsData.postValue(ResourceState.Loading())
        compositeDisposable.add(
            useCase
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    response.products.forEach { item -> productsMap[item.itemId] = item }
                    val adaptedMapValues = ItemListResponse(productsMap.values.toList())
                    _itemsData.postValue(ResourceState.Success(adaptedMapValues))
                    Log.d("ItemsViewModel", "$response")
                }, { e ->
                    _itemsData.postValue(ResourceState.Error(e.message.toString()))
                    e.printStackTrace()
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    private fun getAllProducts() {
        fetchItems(
            getItemListUseCase.execute(
                skip = pagingInfo.page * pagingInfo.limit,
                limit = pagingInfo.limit
            )
        )
        pagingInfo.page++
    }

    private fun getProductsByQuery(query: String) {
        fetchItems(
            searchItemUseCase.execute(
                query,
                skip = pagingInfo.page * pagingInfo.limit,
                limit = pagingInfo.limit
            )
        )
        pagingInfo.page++
    }

    private fun getProductsByCategory(category: String) {
        fetchItems(getItemsByCategoryUseCase.execute(category))
        pagingInfo.page++
    }

    private fun resetItemPages() {
        pagingInfo.page = FIRST_PAGE_INDEX
        productsMap.clear()
    }
}

internal data class PagingInfo(
    var page: Int = FIRST_PAGE_INDEX,
    val limit: Int = ITEMS_LIMIT,
    var query: String = "",
    var category: String = ""
)