package com.example.feature_search_book.domain.useCase

import com.example.core.domain.models.Data
import com.example.core.domain.models.Resources
import com.example.core.domain.repositories.BookRepository
import javax.inject.Inject

class SearchBookByNameUseCase @Inject constructor(
    private val repository: BookRepository
) {

    suspend fun execute(name: String): Resources<Data> {
        return repository.getBooks(name)
    }
}