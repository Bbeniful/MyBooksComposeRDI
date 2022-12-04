package com.example.core.presentation.mybookscompose

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.core.presentation.bottomNavigation.BooksBottomNavigation
import com.example.core.presentation.mybookscompose.ui.ThemeChooser
import com.example.core.presentation.mybookscompose.ui.theme.MyBooksComposeTheme
import com.example.core.utils.BottomItems
import com.example.core.utils.Routes
import com.example.feature_book_details.presentation.bookDetails.BookDetails
import com.example.feature_saved_books.presentation.savedBooksList.SavedBooks
import com.example.feature_search_book.presentation.searchBooks.SearchBooks
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    companion object {
        var topBarTitle = MutableStateFlow("Default")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {

            MyBooksComposeTheme() {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background
                ) {
                    val viewModel = hiltViewModel<MainViewModel>()
                    val navController = rememberNavController()
                    BottomNavigationView(navController = navController, viewModel = viewModel)

                }
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun BottomNavigationView(navController: NavHostController, viewModel: MainViewModel) {
    val items = listOf(
        BottomItems.SEARCH_BOOK, BottomItems.SAVED_BOOKS
    )

    val isDetailsScreen =
        navController.currentBackStackEntryAsState().value?.destination?.route !in items.map { it.route }
    val title = MainActivity.topBarTitle.collectAsState()
    val isDarkTheme = ThemeChooser.isDarkTheme.collectAsState()

    Scaffold(topBar = {
        TopAppBar(title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title.value,
                    fontSize = 20.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (!isDetailsScreen) {
                    Checkbox(checked = isDarkTheme.value, onCheckedChange = {
                        viewModel.saveTheme(it)
                    })
                }
            }

        }, navigationIcon = {
            if (isDetailsScreen) {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack, contentDescription = "Back"
                    )
                }
            }
        })
    },
        bottomBar = { BooksBottomNavigation(bottomItems = items, navController = navController) },
        content = { padding ->
            ContentView(
                modifier = Modifier.padding(padding), navController = navController
            )
        })
}


@Composable
fun ContentView(modifier: Modifier = Modifier, navController: NavHostController) {
    NavHost(navController = navController, startDestination = Routes.SEARCH_BOOK) {
        composable(route = Routes.SEARCH_BOOK) {
            SearchBooks(navController = navController)
        }

        composable(route = Routes.SAVED_BOOK) {
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
