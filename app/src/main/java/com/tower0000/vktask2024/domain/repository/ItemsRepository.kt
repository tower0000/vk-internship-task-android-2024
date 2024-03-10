package com.tower0000.vktask2024.domain.repository

import com.tower0000.vktask2024.data.model.ItemListResponse
import io.reactivex.rxjava3.core.Single

interface ItemsRepository {
    fun getItems(skip: Int, limit: Int): Single<ItemListResponse>
    fun searchItems(query: String, skip: Int, limit: Int): Single<ItemListResponse>
    fun getCategories(): Single<List<String>>
    fun getItemsByCategory(category: String): Single<ItemListResponse>
}