package com.example.core.presentation.mybookscompose

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.core.domain.models.Book
import com.example.core.presentation.actionBar.ActionBarComp
import com.example.core.presentation.bottomNavigation.BooksBottomNavigation
import com.example.core.presentation.mybookscompose.ui.theme.MyBooksComposeTheme
import com.example.core.utils.Routes
import com.example.feature_book_details.presentation.bookDetails.BookDetails
import com.example.feature_saved_books.presentation.savedBooksList.SavedBooks
import com.example.feature_search_book.presentation.searchBooks.SearchBooks
import com.example.feature_search_book.presentation.searchBooks.SearchBooksViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyBooksComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    BottomNavigationView(navController = navController)
                }
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun BottomNavigationView(navController: NavHostController) {
    val isBackButtonVisible by derivedStateOf {
        navController.previousBackStackEntry != null
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Scaffold Examples") }, navigationIcon = {
                if (isBackButtonVisible) {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            })
        },
        bottomBar = { BooksBottomNavigation(navController = navController) },
        content = { padding ->
            ContentView(
                modifier = Modifier.padding(padding), navController = navController
            )
        })
}

@Composable
fun ContentView(modifier: Modifier = Modifier, navController: NavHostController) {
    NavHost(navController = navController, startDestination = Routes.SEARCH_BOOK) {
        composable(Routes.SEARCH_BOOK) {
            SearchBooks(navController = navController)
        }

        composable(Routes.SAVED_BOOK) {
            SavedBooks(navController = navController)
        }
        composable(
            route = Routes.BOOK_DETAILS + "?isbn13={isbn13}", arguments = listOf(navArgument(
                name = "isbn13"
            ) {
                type = NavType.StringType
                defaultValue = ""
            })
        ) {
            BookDetails()
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyBooksComposeTheme {
        Greeting("Android")
    }
}