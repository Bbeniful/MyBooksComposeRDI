package com.example.core.data.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.core.domain.models.BookEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {

    @Query("SELECT * FROM saved_books")
    fun getAllSavedBooks(): Flow<List<BookEntity>>

    @Query("SELECT * FROM saved_books WHERE isbn13 = :isbn13")
    fun getBookByISBN13(isbn13: String): BookEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveBook(bookEntity: BookEntity)

    @Query("Delete FROM saved_books WHERE isbn13 = :isbn13")
    fun delete(isbn13: String)
}