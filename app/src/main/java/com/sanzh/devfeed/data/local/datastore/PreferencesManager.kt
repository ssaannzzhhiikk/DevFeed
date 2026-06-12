package com.sanzh.devfeed.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.*

class PreferencesManager(context: Context) {
    private val dataStore = context.dataStore
    companion object {
        private val Context.dataStore by preferencesDataStore(name =
            "devfeed_prefs")
        val KEY_DARK_MODE = booleanPreferencesKey("dark_mode")
        val KEY_LANGUAGE = stringPreferencesKey("language_filter")
    }
    // Flows — emit new value whenever preference changes
    val isDarkMode: Flow<Boolean> = dataStore.data
        .map { it[KEY_DARK_MODE] ?: false }
    val languageFilter: Flow<String> = dataStore.data
        .map { it[KEY_LANGUAGE] ?: "kotlin" }
    suspend fun setDarkMode(enabled: Boolean) {
        dataStore.edit { it[KEY_DARK_MODE] = enabled }
    }
    suspend fun setLanguage(lang: String) {
        dataStore.edit { it[KEY_LANGUAGE] = lang }
    }
}
