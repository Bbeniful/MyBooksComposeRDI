package com.example.core.presentation.mybookscompose

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.compose.setContent
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.PromptInfo
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
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.core.cryptography.CryptographyManagerImpl
import com.example.core.presentation.bottomNavigation.BooksBottomNavigation
import com.example.core.presentation.mybookscompose.MainActivity.Companion.authCryptoObject
import com.example.core.presentation.mybookscompose.MainActivity.Companion.isSecureUnlocked
import com.example.core.presentation.mybookscompose.MainActivity.Companion.openPasswordDialog
import com.example.core.presentation.mybookscompose.MainActivity.Companion.startAuthRevokeTimer
import com.example.core.presentation.mybookscompose.ui.ThemeChooser
import com.example.core.presentation.mybookscompose.ui.component.PasswordDialog
import com.example.core.presentation.mybookscompose.ui.theme.MyBooksComposeTheme
import com.example.core.utils.BottomItems
import com.example.core.utils.Constants
import com.example.core.utils.Routes
import com.example.di.AppModule
import com.example.feature_book_details.presentation.bookDetails.BookDetails
import com.example.feature_saved_books.presentation.savedBooksList.SavedBooks
import com.example.feature_search_book.presentation.searchBooks.SearchBooks
import com.example.mybookscompose.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import javax.crypto.Cipher

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    val cryptographyManager: CryptographyManagerImpl = CryptographyManagerImpl()

    companion object {
        var topBarTitle = MutableStateFlow("Default")
        var isSecureUnlocked = MutableStateFlow(false)
        var openPasswordDialog = MutableStateFlow(false)
        var authCryptoObject = MutableStateFlow<BiometricPrompt.CryptoObject?>(null)

        fun startAuthRevokeTimer(milliSeconds: Long) {
            Handler(Looper.getMainLooper()).postDelayed({
                destroyState()
            }, milliSeconds)
        }

        fun destroyState() {
            authCryptoObject.value = null
            isSecureUnlocked.value = false
            openPasswordDialog.value = false
        }
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

                    BottomNavigationView(navController = navController, viewModel = viewModel, this)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (authCryptoObject.value == null) {
            isSecureUnlocked.value = false
            openPasswordDialog.value = false
        }
    }

    override fun onPause() {
        super.onPause()
        authCryptoObject.value = null
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun BottomNavigationView(navController: NavHostController, viewModel: MainViewModel, activity: MainActivity) {
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
                modifier = Modifier.padding(padding), navController = navController, activity = activity
            )
        })
}

@Composable
fun ContentView(modifier: Modifier = Modifier, navController: NavHostController, activity: MainActivity) {
    NavHost(navController = navController, startDestination = Routes.SEARCH_BOOK) {

        composable(route = Routes.SEARCH_BOOK) {
            SearchBooks(navController = navController)
        }

        composable(route = Routes.SAVED_BOOK) {
            val openDialog = openPasswordDialog.collectAsState()
            val isUnlocked = isSecureUnlocked.collectAsState()
            if (openDialog.value) {
                PasswordDialog(activity)
            }
            if (isUnlocked.value) {
                SavedBooks(navController = navController)
            } else {
                LaunchedEffect(Unit) {
                    showBiometricPrompt(activity, isEncryption(activity), navController)
                }
            }
        }
        composable(
            route = Routes.BOOK_DETAILS + "?isbn13={isbn13}", arguments = listOf(navArgument(
                name = "isbn13"
            ) {
                type = NavType.StringType
                defaultValue = ""
            })
        ) {
            val openDialog = openPasswordDialog.collectAsState()
            val isUnlocked = isSecureUnlocked.collectAsState()
            if (openDialog.value) {
                PasswordDialog(activity)
            }
            if (isUnlocked.value) {
                BookDetails()
            } else {
                LaunchedEffect(Unit) {
                    showBiometricPrompt(activity, isEncryption(activity), navController)
                }
            }
        }
    }
}

fun showBiometricPrompt(activity: MainActivity, isEncryption: Boolean, navController: NavHostController) {
    var cipher: Cipher? = null
    try {
        cipher = activity.cryptographyManager.getCipherForAuthentication(activity, isEncryption)
    } catch (rte: RuntimeException) {
        rte.printStackTrace()
    }

    cipher?.let {
        BiometricPrompt(activity, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                if (errorCode == 13) navController.navigateUp()
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                handleSuccessAuthentication(activity, isEncryption, result.cryptoObject)
            }
        }).authenticate(getBiometricPromptInfo(activity), BiometricPrompt.CryptoObject(it))
    }
}

fun isEncryption(activity: MainActivity): Boolean {
    val text = activity.cryptographyManager.getEncryptedSharedPrefs(activity).getString(Constants.MY_SECRET_AUTH_KEY_ALIAS, "") ?: ""
    val iv = activity.cryptographyManager.getEncryptedSharedPrefs(activity).getString(Constants.MY_SECRET_IV_ALIAS, "") ?: ""
    return (text.isEmpty() || iv.isEmpty())
}

private fun getBiometricPromptInfo(context: Context): PromptInfo {
    return PromptInfo.Builder()
        .setTitle(context.getString(R.string.biometric_title))
        .setDescription(context.getString(R.string.biometric_description))
        .setNegativeButtonText(context.getString(R.string.biometric_negative_text))
        .setConfirmationRequired(false)
        .build()
}

private fun handleSuccessAuthentication(activity: MainActivity, isEncryption: Boolean, cryptoObject: BiometricPrompt.CryptoObject?) {
    startAuthRevokeTimer(Constants.MY_AUTH_REVOKE_TIME)
    val text = activity.cryptographyManager.getEncryptedSharedPrefs(activity).getString(Constants.MY_SECRET_AUTH_KEY_ALIAS, "") ?: ""
    if (isEncryption) {
        authCryptoObject.value = cryptoObject
        openPasswordDialog.value = true
    } else {
        val key = activity.cryptographyManager.processText(activity, text.toByteArray(), isEncryption, cryptoObject)
        if (key.isNotEmpty()) {
            if (String(key) == Constants.MY_SECRET_KEY_REMOTE_VALUE) {
                AppModule.getBookDatabase(activity.application.applicationContext, key)
                isSecureUnlocked.value = true
            }
        } else {
            openPasswordDialog.value = true
        }
    }
}
