package com.example.core.domain.repositories

import com.example.core.domain.models.Book
import com.example.core.domain.models.Data
import com.example.core.domain.models.Resources

interface BookApiRepository {

    suspend fun getBooks(name: String): Resources<Data>

    suspend fun getNewBooks(): Resources<Data>

    suspend fun getBook(isbn13: String): Resources<Book?>
}