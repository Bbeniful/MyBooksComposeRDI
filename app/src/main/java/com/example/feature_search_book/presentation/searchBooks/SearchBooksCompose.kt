package com.example.feature_search_book.presentation.searchBooks

import android.os.Handler
import android.os.Looper
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.core.presentation.mybookscompose.MainActivity
import com.example.core.utils.BottomItems
import com.example.feature_search_book.presentation.searchBooks.component.BookItem
import kotlinx.coroutines.flow.MutableStateFlow


val keyWords = MutableStateFlow<ArrayList<String>?>(null)

@Composable
fun SearchBooks(
    navController: NavController, viewModel: SearchBooksViewModel = hiltViewModel()
) {
    val books = viewModel.books.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()
    val error = viewModel.error.collectAsState()
    val isNewBooks = viewModel.isNewBooks.collectAsState()
    val keyWordsList = keyWords.collectAsState()
    var hasBeenTouched by remember {
        mutableStateOf(false)
    }

    var keyWord by rememberSaveable { mutableStateOf("") }

    if (!hasBeenTouched && keyWord.isEmpty()) {
        viewModel.getNewBook()
    }

    val arrayTest = ArrayList<String>()
    (1..2).forEach {
        arrayTest.add("Data $it")
    }

    LaunchedEffect(Unit) {
        MainActivity.topBarTitle.value = BottomItems.SEARCH_BOOK.title
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 50.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.Center) {
            Column() {
                
            }
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
                                //savePreviousSearches(it)
                            }, 1500)
                        } else if (it.isEmpty()) {
                            hasBeenTouched = false
                            viewModel.clearBooks()
                        }
                    })
                //Spacer(modifier = Modifier.height(12.dp))
               // Row(modifier = Modifier.fillMaxWidth()) {
                   // if (keyWordsList.value != null) {
                        LazyColumn {
                            items(arrayTest) { word ->
                                Text(text = word, modifier = Modifier
                                    .clickable {
                                        keyWord = word
                                    }
                                    .height(10.dp), textAlign = TextAlign.Center)
                                Divider(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 12.dp, end = 12.dp),
                                    thickness = 2.dp,
                                    color = Color.White
                                )
                            }
                        }
                    //}
                //}
            }

            if (isNewBooks.value) {
                Text(
                    text = "Latest books",
                    textAlign = TextAlign.Start,
                    modifier = Modifier.padding(start = 15.dp)
                )
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

val array = ArrayList<String>()
fun savePreviousSearches(keyWord: String) {
    array.add(keyWord)
    keyWords.value = array
}

@Preview
@Composable
fun PreviewTest() {
    val vm = hiltViewModel<SearchBooksViewModel>()
    val navController = rememberNavController()
    SearchBooks(navController = navController, viewModel = vm)
}


