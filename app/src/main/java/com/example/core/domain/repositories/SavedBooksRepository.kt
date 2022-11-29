package com.example.core.domain.repositories

import com.example.core.domain.models.BookEntity
import kotlinx.coroutines.flow.Flow

interface SavedBooksRepository {

    suspend fun saveBook(book: BookEntity)

    suspend fun getBookByIBSN13(isbn13: String): BookEntity?

    suspend fun getSavedBooks(): Flow<List<BookEntity>>

    suspend fun deleteBook(isbn13: String)
}