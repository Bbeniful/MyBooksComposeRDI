package com.example.core.domain.helpers

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.example.core.domain.extensions.datastore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SaveThemeState @Inject constructor(
    private val context: Context
) {

    private val themePreference = booleanPreferencesKey("theme")

    val getSavedTheme = context.datastore.data.map {
        it[themePreference] ?: true
    }

    suspend fun savePreference(themeState: Boolean) {
        context.datastore.edit {
            it[themePreference] = themeState
        }
    }
}