package com.tower0000.vktask2024.data.apiService

import com.tower0000.vktask2024.data.model.ItemListResponse
import com.tower0000.vktask2024.domain.repository.ItemsRepository
import io.reactivex.rxjava3.core.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

private const val BASE_URL = "https://dummyjson.com/"
class ApiFactory @Inject constructor(): ItemsRepository {

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()

    private val itemsApi: ApiService = retrofit.create(ApiService::class.java)

    override fun getItems(skip: Int, limit: Int): Single<ItemListResponse> {
        return itemsApi.getProducts(skip, limit)
    }

    override fun searchItems(query: String, skip: Int, limit: Int): Single<ItemListResponse> {
        return itemsApi.searchProducts(query, skip, limit)
    }
}