package com.example.feature_search_book.presentation.searchBooks.component

import android.util.Log
import androidx.compose.foundation.clickable
import com.example.mybookscompose.R
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.core.domain.models.Book
import com.example.core.utils.Routes

@Composable
fun BookItem(
    modifier: Modifier = Modifier,navController: NavController? = null, book: Book
) {
    Box(modifier = modifier
        .fillMaxWidth()
        .clickable {
            navController?.navigate(Routes.BOOK_DETAILS + "?isbn13=${book.isbn13}")
        }) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = dimensionResource(id = R.dimen.my_value)
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                AsyncImage(
                    modifier = Modifier.size(120.dp),
                    model = book.image,
                    contentDescription = "Image of book",
                )
                Spacer(modifier = Modifier.width(5.dp))
                Column(
                    modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = book.title ?: stringResource(
                            R.string.product_is_not_available, "Title"
                        )
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = book.price ?: stringResource(
                            R.string.product_is_not_available, "Price"
                        )
                    )
                }
            }
            Divider(
                color = Color.Gray.copy(0.5f),
                thickness = 0.5.dp,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp)
            )
        }
    }
}