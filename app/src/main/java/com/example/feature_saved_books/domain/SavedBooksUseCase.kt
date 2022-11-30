package com.example.feature_saved_books.domain

import com.example.core.domain.models.BookEntity
import com.example.core.domain.repositories.BookRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SavedBooksUseCase @Inject constructor(
    private val repository: BookRepository
) {

    suspend fun execute(): Flow<List<BookEntity>> {
        return repository.getSavedBooks()
    }
}