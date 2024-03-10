package com.tower0000.vktask2024.domain.usecase

import com.tower0000.vktask2024.domain.repository.ItemsRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(private val repo: ItemsRepository) {
    fun execute(): Single<List<String>> = repo.getCategories()
}