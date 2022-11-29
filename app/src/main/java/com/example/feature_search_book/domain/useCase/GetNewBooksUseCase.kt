package com.example.feature_search_book.domain.useCase

import com.example.core.domain.models.Data
import com.example.core.domain.models.Resources
import com.example.core.domain.repositories.BookApiRepository
import javax.inject.Inject

class GetNewBooksUseCase @Inject constructor(
    private val repository: BookApiRepository
) {


    suspend fun execute():Resources<Data>{
        return repository.getNewBooks()
    }
}