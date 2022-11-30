package com.example.core.utils

import com.example.mybookscompose.R

sealed class BottomItems(var icon: Int,var title : String, var route: String) {

    object SEARCH_BOOK: BottomItems(icon = R.drawable.ic_list, title = "Search books", route =  Routes.SEARCH_BOOK )
    object SAVED_BOOKS: BottomItems(icon = R.drawable.ic_save, title = "Saved books", route =  Routes.SAVED_BOOK )
    object BOOK_DETAILS: BottomItems(icon = R.drawable.ic_save, title = "Book details", route =  Routes.BOOK_DETAILS )

}