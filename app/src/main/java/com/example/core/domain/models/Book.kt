package com.example.core.domain.models

import android.os.Parcel
import android.os.Parcelable

data class Book(
    val id: Int = 0,
    val image: String?,
    val isbn13: String?,
    val price: String?,
    val subtitle: String?,
    val title: String?,
    val url: String?
) {

    fun toBookEntity(): BookEntity {
        return BookEntity(
            id = id,
            image = image,
            isbn13 = isbn13,
            price = price,
            subtitle = subtitle,
            title = title,
            url = url
        )
    }

}