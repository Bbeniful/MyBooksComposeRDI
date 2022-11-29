package com.example.core.data.repositories

import com.example.core.data.local.database.BookDao
import com.example.core.domain.models.BookEntity
import com.example.core.domain.repositories.SavedBooksRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SavedBooksRepositoryImpl @Inject constructor(
    private val dao: BookDao
) : SavedBooksRepository {

    override suspend fun saveBook(book: BookEntity) {
        dao.saveBook(bookEntity = book)
    }

    override suspend fun getBookByIBSN13(isbn13: String): BookEntity {
        return dao.getBookByISBN13(isbn13 = isbn13)
    }

    override suspend fun getSavedBooks(): Flow<List<BookEntity>> {
        return dao.getAllSavedBooks()
    }

    override suspend fun deleteBook(isbn13: String) {
        dao.delete(isbn13 = isbn13)
    }

}