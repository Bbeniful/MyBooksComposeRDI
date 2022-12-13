package com.example.core.data.repositories

import com.example.core.data.remote.BookApi
import com.example.core.domain.models.Book
import com.example.core.domain.models.BookEntity
import com.example.core.domain.models.Data
import com.example.core.domain.models.Resources
import com.example.core.domain.repositories.BookRepository
import com.example.di.AppModule
import com.example.mybookscompose.BuildConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import retrofit2.HttpException
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(private val api: BookApi) : BookRepository {

    override suspend fun getBooks(name: String): Resources<Data> {
        return try {
            Resources.Success(data = api.getBooksByName(name, token = BuildConfig.NOT_NEEDED_API_TOKEN))
        } catch (e: Exception) {
            Resources.Error(message = e.message ?: "Something went wrong")
        }
    }

    override suspend fun getNewBooks(): Resources<Data> {
        return try {
            Resources.Success(data = api.getNews(token = BuildConfig.NOT_NEEDED_API_TOKEN))
        } catch (e: HttpException) {
            Resources.Error(message = e.message() ?: "Something went wrong")
        }
    }

    override suspend fun getBook(isbn13: String): Resources<Book?> {
        val bookFromDb = AppModule.getBookDao()?.getBookByISBN13(isbn13 = isbn13)
        return if (bookFromDb != null) {
            Resources.Success(data = bookFromDb.toBook())
        } else {
            try {
                Resources.Success(data = api.getBookByISBN13(isbn13 = isbn13, token = BuildConfig.NOT_NEEDED_API_TOKEN))
            } catch (e: java.lang.Exception) {
                Resources.Error(
                    data = api.getBookByISBN13(isbn13 = isbn13, token = BuildConfig.NOT_NEEDED_API_TOKEN), message = e.message ?: ""
                )
            }
        }
    }

    override suspend fun getSavedBooks(): Flow<List<BookEntity>> {
        return AppModule.getBookDao()?.getAllSavedBooks() ?: flowOf(listOf())
    }

    override suspend fun saveBook(book: BookEntity) {
        AppModule.getBookDao()?.saveBook(bookEntity = book)
    }

    override suspend fun deleteBook(isbn13: String) {
        AppModule.getBookDao()?.delete(isbn13 = isbn13)
    }
}