package com.example.core.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.core.domain.models.BookEntity
import com.example.core.utils.Constants
import net.sqlcipher.database.SupportFactory

@Database(
    entities = [BookEntity::class],
    version = Constants.DATABASE_VERSION
)
abstract class BookDatabase : RoomDatabase() {

    abstract val dao: BookDao

    companion object {
        private lateinit var instance: BookDatabase

        fun getInstance(app: Context, key: ByteArray): BookDatabase {
            if (!this::instance.isInitialized) {
                instance = Room.databaseBuilder(app, BookDatabase::class.java, Constants.DATABASE_NAME)
                    .setJournalMode(JournalMode.TRUNCATE)
                    .openHelperFactory(SupportFactory(key))
                    .build()
            }
            return instance
        }
    }
}