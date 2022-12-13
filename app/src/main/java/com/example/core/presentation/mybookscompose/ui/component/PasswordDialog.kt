package com.example.core.presentation.mybookscompose.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.core.presentation.mybookscompose.MainActivity
import com.example.core.utils.Constants
import com.example.di.AppModule
import com.example.mybookscompose.R
import kotlinx.coroutines.android.awaitFrame

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PasswordDialog(activity: MainActivity) {
    val showKeyboard = remember { mutableStateOf(true) }
    var input by remember { mutableStateOf("") }
    val keyboard = LocalSoftwareKeyboardController.current
    val focusRequester = FocusRequester()
    var passwordVisible by remember { mutableStateOf(false) }
    AlertDialog(
        onDismissRequest = {
            MainActivity.openPasswordDialog.value = false
        },
        title = {
            Text(stringResource(R.string.app_password_dialog_title))
        },
        text = {
            Column() {
                TextField(
                    modifier = Modifier.focusRequester(focusRequester),
                    value = input,
                    onValueChange = { input = it },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        val image = if (passwordVisible)
                            Icons.Filled.Visibility
                        else Icons.Filled.VisibilityOff

                        // Please provide localized description for accessibility services
                        val description = if (passwordVisible) stringResource(R.string.app_password_hide) else stringResource(
                            R.string.app_password_show)

                        IconButton(onClick = {passwordVisible = !passwordVisible}){
                            Icon(imageVector = image, description)
                        }
                    }
                )
            }
        },
        buttons = {
            Row(
                modifier = Modifier.padding(all = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        MainActivity.startAuthRevokeTimer(Constants.MY_AUTH_REVOKE_TIME)
                        val key = input.toByteArray()
                        input = ""
                        MainActivity.openPasswordDialog.value = false
                        activity.cryptographyManager.processText(activity, key, true, MainActivity.authCryptoObject.value)
                        if (key.isNotEmpty()) {
                            if (key.contentEquals(Constants.MY_SECRET_KEY_REMOTE_VALUE.toByteArray())) {
                                AppModule.getBookDatabase(activity.application.applicationContext, key)
                                MainActivity.isSecureUnlocked.value = true
                            }
                        } else {
                            MainActivity.openPasswordDialog.value = true
                        }
                    }
                ) {
                    Text(stringResource(R.string.app_password_dialog_button))
                }
            }
        }
    )
    LaunchedEffect(Unit) {
        if (showKeyboard.value) {
            awaitFrame()
            focusRequester.requestFocus()
            keyboard?.show()
        }
    }
}