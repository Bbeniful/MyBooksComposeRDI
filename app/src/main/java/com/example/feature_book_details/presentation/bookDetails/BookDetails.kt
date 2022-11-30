package com.example.feature_book_details.presentation.bookDetails

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.core.presentation.mybookscompose.MainActivity
import com.example.core.presentation.mybookscompose.ui.theme.Purple200
import com.example.core.utils.BottomItems
import com.example.mybookscompose.R


@Composable
fun BookDetails() {

    val viewModel = hiltViewModel<BookDetailsViewModel>()
    val book = viewModel.book.collectAsState()
    val isSavedBook = viewModel.isSavedBook.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit){
        MainActivity.topBarTitle.value = book.value?.title ?: "Book details"
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(start = 15.dp, end = 15.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            ConstraintLayout() {

                val (bookImage, saveOrDeleteIcon) = createRefs()

                AsyncImage(
                    modifier = Modifier
                        .size(300.dp)
                        .constrainAs(bookImage) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        },
                    model = book.value?.image,
                    contentDescription = "Image of the selected book",
                    alignment = Alignment.Center
                )

                val resourceId = if (isSavedBook.value) {
                    R.drawable.ic_delete
                } else {
                    R.drawable.ic_save
                }

                val iconTint = if (isSavedBook.value) {
                    Color.Red
                } else {
                    Color.Gray
                }

                IconButton(onClick = {
                    if (isSavedBook.value) {
                        viewModel.deleteBook(book = book.value)
                    } else {
                        viewModel.saveBook(book = book.value)
                    }
                }, modifier = Modifier
                    .padding(top = 12.dp)
                    .constrainAs(saveOrDeleteIcon) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                    }) {
                    Icon(
                        painter = painterResource(id = resourceId),
                        contentDescription = "icon of save book",
                        tint = iconTint,
                    )
                }
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text(
                    text = book.value?.title ?: stringResource(
                        id = R.string.product_is_not_available, "Title"
                    ), fontSize = 20.sp, fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(25.dp))
            Text(
                text = book.value?.subtitle ?: stringResource(
                    id = R.string.product_is_not_available, "Subtitle"
                ), fontSize = 16.sp
            )
            Text(
                text = ("Price: " + book.value?.price) ?: stringResource(
                    id = R.string.product_is_not_available, "Title"
                ), fontSize = 16.sp
            )
            Text(
                modifier = Modifier.clickable {
                    openLink(context, book.value?.url)
                },
                text = ("Buy it from: " + book.value?.url) ?: stringResource(
                    id = R.string.product_is_not_available, "Title"
                ), fontSize = 16.sp
            )

        }

        val error = viewModel.error.collectAsState()

        if (error.value != null) {
            Text(text = error.value ?: "")
        }
    }
}

fun openLink(context: Context, link: String?) {
    if (link.isNullOrEmpty()) return
    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
    context.startActivity(browserIntent)
}