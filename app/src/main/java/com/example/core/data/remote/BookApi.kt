package com.example.core.data.remote

import com.example.core.domain.models.Book
import com.example.core.domain.models.Data
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BookApi {

    @GET("search/{keyWord}")
    suspend fun getBooksByName(@Path("keyWord") keyWord: String, @Query("token") token: String): Data

    @GET("books/{isbn13}")
    suspend fun getBookByISBN13(@Path("isbn13") isbn13: String, @Query("token") token: String): Book

    @GET("new")
    suspend fun getNews(@Query("token") token: String): Data

}