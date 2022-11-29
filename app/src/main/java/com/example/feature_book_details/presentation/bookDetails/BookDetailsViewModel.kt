package com.example.feature_book_details.presentation.bookDetails

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.models.Book
import com.example.core.domain.models.Resources
import com.example.feature_book_details.domain.use_case.BookDetailsUseCases
import com.example.feature_book_details.domain.use_case.GetBookUseCase
import com.example.feature_saved_books.domain.SavedBooksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val bookDetailsUseCases: BookDetailsUseCases,
    private val savedBooksUseCase: SavedBooksUseCase
) : ViewModel() {

    private var _book = MutableStateFlow<Book?>(null)
    val book = _book.asStateFlow()

    private var _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private var _isSavedBook = MutableStateFlow(false)
    val isSavedBook = _isSavedBook.asStateFlow()

    init {
        savedStateHandle.get<String>("isbn13")?.let {
            getBook(it)
            isSavedBook(it)
        }
    }


    private fun getBook(isbn13: String) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = bookDetailsUseCases.getBookUseCase.execute(isbn13 = isbn13)) {
                is Resources.Success -> {
                    _book.value = result.data
                    Log.e("result ->", " datares -> ${result.data}")
                }
                is Resources.Error -> {
                    _error.value = result.message
                }
            }
        }
    }

    private fun isSavedBook(isbn13: String) {
        viewModelScope.launch {
            savedBooksUseCase.execute().collectLatest {
                _isSavedBook.value = it.find { it.isbn13 == isbn13 } != null
            }
        }
    }

    fun saveBook(book: Book?) {
        if (book == null) {

            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            bookDetailsUseCases.saveBook.execute(book = book)
        }
    }

    fun deleteBook(book: Book?) {
         if (book == null) {
             return
         }
        viewModelScope.launch(Dispatchers.IO) {
            bookDetailsUseCases.deleteBookUseCase.execute(isbn13 = book.isbn13 ?: "")
        }
    }


    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}