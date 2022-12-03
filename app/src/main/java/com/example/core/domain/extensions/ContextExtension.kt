package com.example.core.domain.extensions

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.core.utils.Constants

val Context.datastore : DataStore<Preferences> by  preferencesDataStore(name = Constants.THEME_DATASTORE)