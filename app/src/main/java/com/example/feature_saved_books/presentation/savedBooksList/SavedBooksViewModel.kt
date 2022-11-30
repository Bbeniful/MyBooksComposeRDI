package com.example.feature_saved_books.presentation.savedBooksList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.models.Book
import com.example.feature_saved_books.domain.SavedBooksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedBooksViewModel @Inject constructor(
    private val savesBooksUseCase: SavedBooksUseCase
) : ViewModel() {

    private var _books = MutableStateFlow<List<Book>?>(null)
    val books = _books.asStateFlow()


    init {
        getSavedBooks()
    }

    private fun getSavedBooks() {
        viewModelScope.launch {
            savesBooksUseCase.execute().collectLatest {
                _books.value = it.map { it.toBook() }
            }
        }
    }


}