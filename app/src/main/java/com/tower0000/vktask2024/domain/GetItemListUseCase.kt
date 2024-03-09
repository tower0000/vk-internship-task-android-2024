package com.tower0000.vktask2024.domain

import com.tower0000.vktask2024.data.ItemListResponse
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GetItemListUseCase @Inject constructor(private val repo: ItemsRepository) {
    fun execute(skip: Int, limit: Int): Single<ItemListResponse> = repo.getItems(skip, limit)
}