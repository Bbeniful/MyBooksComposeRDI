package com.example.feature_book_details.presentation.bookDetails.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.example.core.data.local.database.BookDao
import com.example.feature_book_details.presentation.bookDetails.openLink
import com.example.mybookscompose.R

@Composable
fun BookTextItem(
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 16.sp,
    nameOfText: String? = null,
    text: String?,
    fontWeight: FontWeight = FontWeight.Normal,
    color: Color = Color.Black
) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
        if (nameOfText != null) {
            Text(
                text = "$nameOfText:", fontSize = fontSize, fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(10.dp))
        }
        Text(
            modifier = modifier,
            text = text ?: stringResource(
                id = R.string.product_is_not_available, "$nameOfText"
            ), fontSize = fontSize,
            fontWeight = fontWeight,
            color = color
        )
    }
    Spacer(modifier = Modifier.height(25.dp))
}