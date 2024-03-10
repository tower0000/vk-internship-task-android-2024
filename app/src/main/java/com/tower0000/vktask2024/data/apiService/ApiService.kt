package com.tower0000.vktask2024.data.apiService

import com.tower0000.vktask2024.data.model.ItemListResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("./products")
    fun getProducts(
        @Query("skip") skip: Int,
        @Query("limit") limit: Int,
        ): Single<ItemListResponse>

    @GET("./products/search")
    fun searchProducts(
        @Query("q") query: String,
        @Query("skip") skip: Int,
        @Query("limit") limit: Int,
    ): Single<ItemListResponse>

    @GET("./products/categories")
    fun getCategories(): Single<List<String>>

    @GET("products/category/{path}")
    fun getCategoryItems(
        @Path("path") category: String
    ): Single<ItemListResponse>

}