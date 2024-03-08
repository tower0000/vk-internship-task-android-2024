package com.tower0000.vktask2024.domain

import com.tower0000.vktask2024.data.ItemListResponse
import io.reactivex.rxjava3.core.Single

interface ItemsRepository {
    fun getItems(skip: Int, limit: Int): Single<ItemListResponse>
}