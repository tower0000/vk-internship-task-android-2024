package com.tower0000.vktask2024.domain.usecase

import com.tower0000.vktask2024.data.model.ItemListResponse
import com.tower0000.vktask2024.domain.repository.ItemsRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GetItemListUseCase @Inject constructor(private val repo: ItemsRepository) {
    fun execute(skip: Int, limit: Int): Single<ItemListResponse> = repo.getItems(skip, limit)
}