package com.example.feature_book_details.domain.use_case

import com.example.core.domain.models.Book
import com.example.core.domain.repositories.BookRepository
import javax.inject.Inject

class SaveBookUseCase @Inject constructor(
    private val repository: BookRepository
) {

    suspend fun execute(book: Book){
        repository.saveBook(book = book.toBookEntity())
    }
}