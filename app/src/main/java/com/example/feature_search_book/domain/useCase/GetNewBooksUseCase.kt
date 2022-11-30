package com.example.feature_search_book.domain.useCase

import com.example.core.domain.models.Data
import com.example.core.domain.models.Resources
import com.example.core.domain.repositories.BookRepository
import javax.inject.Inject

class GetNewBooksUseCase @Inject constructor(
    private val repository: BookRepository
) {


    suspend fun execute():Resources<Data>{
        return repository.getNewBooks()
    }
}