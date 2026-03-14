package com.example.assignment6_parliament_room.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.assignment6_parliament_room.ui.strings.AppLanguage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("settings")

object ThemePreferences {

    private val DARK_THEME_KEY = booleanPreferencesKey("dark_theme_enabled")
    private val LANGUAGE_KEY   = stringPreferencesKey("language")

    suspend fun saveDarkTheme(context: Context, isDark: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[DARK_THEME_KEY] = isDark
        }
    }

    fun isDarkTheme(context: Context): Flow<Boolean> =
        context.dataStore.data.map { prefs ->
            prefs[DARK_THEME_KEY] ?: false // default to light theme
        }

    fun getLanguage(context: Context): Flow<AppLanguage> =
        context.dataStore.data.map { prefs ->
            val code = prefs[LANGUAGE_KEY] ?: AppLanguage.ENGLISH.code
            AppLanguage.entries.find { it.code == code } ?: AppLanguage.ENGLISH
        }

    suspend fun setLanguage(context: Context, language: AppLanguage) {
        context.dataStore.edit { prefs ->
            prefs[LANGUAGE_KEY] = language.code
        }
    }
}