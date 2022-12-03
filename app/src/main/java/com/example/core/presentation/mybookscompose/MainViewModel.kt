package com.example.core.presentation.mybookscompose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.helpers.SaveThemeState
import com.example.core.presentation.mybookscompose.ui.ThemeChooser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val savedThemeState: SaveThemeState
) : ViewModel() {

    init {
        getCurrentTheme()
    }

    private fun getCurrentTheme(){
        viewModelScope.launch {
            ThemeChooser.isDarkTheme.value = savedThemeState.getSavedTheme.first()
        }
    }

    fun saveTheme(theme: Boolean) {
        viewModelScope.launch {
            savedThemeState.savePreference(theme)
            ThemeChooser.isDarkTheme.value = theme
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}