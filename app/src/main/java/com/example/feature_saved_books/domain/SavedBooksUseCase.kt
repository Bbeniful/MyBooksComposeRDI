package com.example.feature_saved_books.domain

import com.example.core.domain.models.BookEntity
import com.example.core.domain.models.Data
import com.example.core.domain.models.Resources
import com.example.core.domain.repositories.SavedBooksRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SavedBooksUseCase @Inject constructor(
    private val savedBooksRepository: SavedBooksRepository
) {

    suspend fun execute(): Flow<List<BookEntity>> {
        return savedBooksRepository.getSavedBooks()
    }
}