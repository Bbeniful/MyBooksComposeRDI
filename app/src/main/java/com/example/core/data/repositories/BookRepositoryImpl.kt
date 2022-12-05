package com.example.core.data.repositories

import com.example.core.data.local.database.BookDao
import com.example.core.data.remote.BookApi
import com.example.core.domain.models.Book
import com.example.core.domain.models.BookEntity
import com.example.core.domain.models.Data
import com.example.core.domain.models.Resources
import com.example.core.domain.repositories.BookRepository
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(
    private val dao: BookDao, private val api: BookApi
) : BookRepository {

    override suspend fun getBooks(name: String): Resources<Data> {
        return try {
            Resources.Success(data = api.getBooksByName(name))
        } catch (e: HttpException) {
            Resources.Error(message = e.message ?: "Something went wrong")
        }
    }

    override suspend fun getNewBooks(): Resources<Data> {
        return try {
            Resources.Success(data = api.getNews())
        } catch (e: HttpException) {
            Resources.Error(message = e.message() ?: "Something went wrong")
        }
    }

    override suspend fun getBook(isbn13: String): Resources<Book?> {
        val bookFromDb = dao.getBookByISBN13(isbn13 = isbn13)
        return if (bookFromDb != null) {
            Resources.Success(data = bookFromDb.toBook())
        } else {
            try {
                Resources.Success(data = api.getBookByISBN13(isbn13 = isbn13))
            } catch (e: HttpException) {
                Resources.Error(
                    data = api.getBookByISBN13(isbn13 = isbn13), message = e.message ?: ""
                )
            }
        }
    }

    override suspend fun getSavedBooks(): Flow<List<BookEntity>> {
        return dao.getAllSavedBooks()
    }

    override suspend fun saveBook(book: BookEntity) {
        dao.saveBook(bookEntity = book)
    }


    override suspend fun deleteBook(isbn13: String) {
        dao.delete(isbn13 = isbn13)
    }
}