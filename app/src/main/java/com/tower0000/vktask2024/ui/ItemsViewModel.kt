package com.tower0000.vktask2024.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tower0000.vktask2024.data.ApiFactory
import com.tower0000.vktask2024.data.ItemListResponse
import com.tower0000.vktask2024.domain.GetItemListUseCase
import com.tower0000.vktask2024.ui.util.ResourceState
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class ItemsViewModel : ViewModel() {
    private val apiFactory = ApiFactory()
    private val getItemListUseCase = GetItemListUseCase(apiFactory)
    private val _itemsData =
        MutableLiveData<ResourceState<ItemListResponse>>(ResourceState.Unspecified())
    val itemsData: LiveData<ResourceState<ItemListResponse>> = _itemsData

    private val compositeDisposable = CompositeDisposable()

    fun loadItems() {
        compositeDisposable.add(apiFactory.itemsApi.getProducts(0, 10)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.i("TESTTEST", it.toString())
            }, {
                Log.e("TESTTEST", it.toString())
            }))
    }
}