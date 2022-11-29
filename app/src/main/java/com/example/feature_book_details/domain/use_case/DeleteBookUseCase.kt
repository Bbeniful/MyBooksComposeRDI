package com.example.feature_book_details.domain.use_case

import com.example.core.domain.models.Book
import com.example.core.domain.repositories.SavedBooksRepository
import javax.inject.Inject

class DeleteBookUseCase @Inject constructor(
    private val savedBooksRepository: SavedBooksRepository
) {

    suspend fun execute(isbn13: String) {
        savedBooksRepository.deleteBook(isbn13 = isbn13)
    }
}