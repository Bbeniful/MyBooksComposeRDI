package com.example.feature_book_details.domain.use_case

import com.example.core.domain.repositories.BookRepository
import javax.inject.Inject

class DeleteBookUseCase @Inject constructor(
    private val repository: BookRepository
) {

    suspend fun execute(isbn13: String) {
        repository.deleteBook(isbn13 = isbn13)
    }
}