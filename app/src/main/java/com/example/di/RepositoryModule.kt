package com.example.di

import com.example.core.data.repositories.BookRepositoryImpl
import com.example.core.domain.repositories.BookRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindsBookRepository(
        bookRepositoryImpl: BookRepositoryImpl
    ):BookRepository
}