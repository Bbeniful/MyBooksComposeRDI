package com.example.feature_book_details.domain.use_case

import com.example.core.domain.models.Book
import com.example.core.domain.models.Resources
import com.example.core.domain.repositories.BookApiRepository
import com.example.core.domain.repositories.SavedBooksRepository
import javax.inject.Inject

class GetBookUseCase @Inject constructor(
    private val bookApiRepository: BookApiRepository,
    private val savedBooksRepository: SavedBooksRepository
) {

    suspend fun execute(isbn13: String): Resources<Book?> {
        val bookFromDb = savedBooksRepository.getBookByIBSN13(isbn13)?.toBook()
        return if (bookFromDb != null)
            Resources.Success(data = bookFromDb)
        else bookApiRepository.getBook(isbn13 = isbn13)
    }
}