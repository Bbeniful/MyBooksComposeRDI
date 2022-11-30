package com.example.feature_search_book.presentation.searchBooks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.models.Book
import com.example.core.domain.models.Resources
import com.example.feature_search_book.domain.useCase.BookListUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchBooksViewModel @Inject constructor(
    private val bookListUseCases: BookListUseCases
) : ViewModel() {

    private var _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private var _books = MutableStateFlow<List<Book>?>(null)
    val books = _books.asStateFlow()

    private var _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private var _isNewBooks = MutableStateFlow(false)
    val isNewBooks = _isNewBooks.asStateFlow()

    fun searchBooks(name: String) {
        viewModelScope.launch {
            _isLoading.value = true
            when (val result = bookListUseCases.searchBookByNameUseCase.execute(name)) {
                is Resources.Success -> {
                    _isLoading.value = false
                    _isNewBooks.value = false
                    val data = result.data
                    _books.value = data?.books
                }
                is Resources.Error -> {
                    _isLoading.value = false

                    _error.value = result.message
                    if (result.data != null) {
                        val data = result.data
                        _books.value = data?.books
                    }
                }
            }
        }
    }

    fun getNewBook(){
        viewModelScope.launch {
            _isLoading.value = true
            when (val result = bookListUseCases.getNewBooksUseCase.execute()) {
                is Resources.Success -> {
                    _isLoading.value = false
                    _isNewBooks.value = true
                    val data = result.data
                    _books.value = data?.books
                }
                is Resources.Error -> {
                    _isLoading.value = false

                    _error.value = result.message
                    if (result.data != null) {
                        val data = result.data
                        _books.value = data?.books
                    }
                }
            }
        }
    }

    fun clearBooks() {
        _books.value = listOf()
    }

    fun clearError(){
        _error.value = ""
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}