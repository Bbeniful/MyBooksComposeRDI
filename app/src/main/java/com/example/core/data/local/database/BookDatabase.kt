package com.example.core.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.core.domain.models.BookEntity
import com.example.core.utils.Constants

@Database(
    entities = [BookEntity::class],
    version = Constants.DATABASE_VERSION
)
abstract class BookDatabase : RoomDatabase() {

    abstract val dao: BookDao
}