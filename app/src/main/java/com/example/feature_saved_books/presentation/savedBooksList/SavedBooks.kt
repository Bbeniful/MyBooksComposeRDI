package com.example.feature_saved_books.presentation.savedBooksList

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.core.presentation.mybookscompose.MainActivity
import com.example.core.utils.BottomItems
import com.example.feature_search_book.presentation.searchBooks.component.BookItem

@Composable
fun SavedBooks(navController: NavController) {

    val viewModel = hiltViewModel<SavedBooksViewModel>()
    val books = viewModel.books.collectAsState()

    LaunchedEffect(Unit){
        MainActivity.topBarTitle.value = BottomItems.SAVED_BOOKS.title
    }

    LazyColumn(
        modifier = Modifier.padding(bottom = 50.dp)
    ) {
        items(items = books.value ?: listOf()) { book ->
            BookItem(book = book, navController = navController)
        }
    }
}