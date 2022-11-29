package com.example.feature_saved_books.presentation.savedBooksList

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.feature_search_book.presentation.searchBooks.component.BookItem

@Composable
fun SavedBooks(navController: NavController) {

    val viewModel = hiltViewModel<SavedBooksViewModel>()
    val books = viewModel.books.collectAsState()

    LazyColumn {
        items(items = books.value ?: listOf()) { book ->
            BookItem(book = book, navController = navController)
        }
    }

}