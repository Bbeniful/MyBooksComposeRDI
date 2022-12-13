package com.example.core.domain.repositories

import com.example.core.domain.models.Book
import com.example.core.domain.models.BookEntity
import com.example.core.domain.models.Data
import com.example.core.domain.models.Resources
import kotlinx.coroutines.flow.Flow

interface BookRepository {

    suspend fun getBooks(name: String): Resources<Data>

    suspend fun getNewBooks(): Resources<Data>

    suspend fun getBook(isbn13: String): Resources<Book?>

    suspend fun getSavedBooks(): Flow<List<BookEntity>>

    suspend fun saveBook(book: BookEntity)

    suspend fun deleteBook(isbn13: String)
}