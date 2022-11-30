package com.example.feature_search_book.presentation.searchBooks

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.feature_search_book.presentation.searchBooks.component.BookItem

@Composable
fun SearchBooks(
    navController: NavController, viewModel: SearchBooksViewModel = hiltViewModel()
) {
    val books = viewModel.books.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()
    val error = viewModel.error.collectAsState()
    val isNewBooks = viewModel.isNewBooks.collectAsState()
    var hasBeenTouched by remember {
        mutableStateOf(false)
    }

    var keyWord by remember { mutableStateOf("") }

    if (!hasBeenTouched && keyWord.isEmpty()) {
        viewModel.getNewBook()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(bottom = 50.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.Center) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
            ) {
                OutlinedTextField(modifier = Modifier.fillMaxWidth(),
                    value = keyWord,
                    placeholder = { Text(text = "Name or part of the target book topic") },
                    label = { Text(text = "Name or part of the target book topic") },
                    onValueChange = {
                        keyWord = it
                        hasBeenTouched = true

                        if (it.length >= 3) {
                            Handler(Looper.getMainLooper()).postDelayed({
                                viewModel.searchBooks(it)
                            },1500)
                        } else if (it.isEmpty()) {
                            hasBeenTouched = false
                            viewModel.clearBooks()
                        }
                    })
            }

            if (isNewBooks.value) {
                Text(text = "Latest books", textAlign = TextAlign.Start, modifier = Modifier.padding(start = 15.dp))
            }

            LazyColumn {
                items(items = books.value ?: listOf()) { book ->
                    BookItem(book = book, navController = navController)
                }
            }

            if (isLoading.value) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }

            if (hasBeenTouched && books.value.isNullOrEmpty()) {
                Text(text = "No book :(", textAlign = TextAlign.Center)
            }

            if (error.value != null) {
                Text(textAlign = TextAlign.Center, text = error.value ?: "", color = Color.Red)
            }

        }
    }

}