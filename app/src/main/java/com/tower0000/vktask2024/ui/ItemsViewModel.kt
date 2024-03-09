package com.tower0000.vktask2024.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tower0000.vktask2024.data.ApiFactory
import com.tower0000.vktask2024.data.ItemListResponse
import com.tower0000.vktask2024.domain.GetItemListUseCase
import com.tower0000.vktask2024.ui.util.ResourceState
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class ItemsViewModel @Inject constructor(
    private val getItemListUseCase: GetItemListUseCase
) : ViewModel() {
    private val _itemsData =
        MutableLiveData<ResourceState<ItemListResponse>>(ResourceState.Unspecified())
    val itemsData: LiveData<ResourceState<ItemListResponse>> = _itemsData

    private val compositeDisposable = CompositeDisposable()

    fun loadItems() {
        _itemsData.postValue(ResourceState.Loading())
        compositeDisposable.add(getItemListUseCase.execute(0, 20)
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
    }
}