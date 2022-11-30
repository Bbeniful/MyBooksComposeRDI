package com.example.feature_book_details.domain.use_case

import com.example.core.domain.models.Book
import com.example.core.domain.models.Resources
import com.example.core.domain.repositories.BookRepository
import javax.inject.Inject

class GetBookUseCase @Inject constructor(
    private val repository: BookRepository
) {

    suspend fun execute(isbn13: String): Resources<Book?> {
       return repository.getBook(isbn13 = isbn13)
    }
}