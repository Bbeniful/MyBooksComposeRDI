package com.example.core.data.repositories

import com.example.core.data.remote.BookApi
import com.example.core.domain.models.Book
import com.example.core.domain.models.Data
import com.example.core.domain.models.Resources
import com.example.core.domain.repositories.BookApiRepository
import javax.inject.Inject

class BookApiRepositoryImpl @Inject constructor(
    private val api: BookApi
) : BookApiRepository {

    override suspend fun getBooks(name: String): Resources<Data> {
        return try {
            Resources.Success(data = api.getBooksByName(name))
        } catch (e: java.lang.Exception) {
            Resources.Error(message = e.message ?: "Something went wrong")
        }
    }

    override suspend fun getNewBooks(): Resources<Data> {
        return try {
            Resources.Success(data = api.getNews())
        } catch (e: java.lang.Exception) {
            Resources.Error(message = e.message ?: "Something went wrong")
        }
    }

    override suspend fun getBook(isbn13: String): Resources<Book?> {
        return try {
            Resources.Success(data = api.getBookByISBN13(isbn13))
        } catch (e: java.lang.Exception) {
            Resources.Error(message = e.message ?: "Something went wrong")
        }
    }

}