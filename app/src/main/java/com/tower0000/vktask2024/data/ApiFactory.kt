package com.tower0000.vktask2024.data

import com.tower0000.vktask2024.domain.ItemsRepository
import io.reactivex.rxjava3.core.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ApiFactory : ItemsRepository {
    private val BASE_URL = "https://dummyjson.com/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()

    val itemsApi: ApiService = retrofit.create(ApiService::class.java)

    override fun getItems(skip: Int, limit: Int): Single<ItemListResponse> {
        return itemsApi.getProducts(skip, limit)
    }
}