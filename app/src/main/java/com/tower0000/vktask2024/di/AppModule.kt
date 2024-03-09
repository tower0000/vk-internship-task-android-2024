package com.tower0000.vktask2024.di

import com.tower0000.vktask2024.data.apiService.ApiFactory
import com.tower0000.vktask2024.domain.repository.ItemsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideMyRepository(repository: ApiFactory): ItemsRepository {
        return repository
    }
}