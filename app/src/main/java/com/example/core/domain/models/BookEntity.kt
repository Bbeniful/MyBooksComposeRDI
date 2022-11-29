package com.example.core.domain.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_books")
data class BookEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,

    @ColumnInfo(name = "image") val image: String?,

    @ColumnInfo(name = "isbn13") val isbn13: String?,

    @ColumnInfo(name = "price") val price: String?,

    @ColumnInfo(name = "subtitle") val subtitle: String?,

    @ColumnInfo(name = "title") val title: String?,

    @ColumnInfo(name = "url") val url: String?
) {

    fun toBook(): Book {
        return Book(
            image = image,
            isbn13 = isbn13,
            price = price,
            subtitle = subtitle,
            title = title,
            url = url
        )
    }
}