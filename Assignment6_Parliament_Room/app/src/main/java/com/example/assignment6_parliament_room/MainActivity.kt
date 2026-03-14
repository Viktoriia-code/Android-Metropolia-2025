package com.example.assignment6_parliament_room

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.example.assignment6_parliament_room.data.preferences.ThemePreferences
import com.example.assignment6_parliament_room.ui.MpsNavGraph
import com.example.assignment6_parliament_room.ui.strings.AppLanguage
import com.example.assignment6_parliament_room.ui.strings.LocalStrings
import com.example.assignment6_parliament_room.ui.strings.stringsForLanguage
import com.example.assignment6_parliament_room.ui.theme.Assignment6_Parliament_RoomTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val darkTheme by ThemePreferences.isDarkTheme(applicationContext)
                .collectAsStateWithLifecycle(initialValue = false)

            val language by ThemePreferences.getLanguage(applicationContext)
                .collectAsStateWithLifecycle(initialValue = AppLanguage.ENGLISH)
            CompositionLocalProvider(LocalStrings provides stringsForLanguage(language)) {
                Assignment6_Parliament_RoomTheme(darkTheme = darkTheme) {
                    val navController = rememberNavController()
                    // Pass the application so screens can access repositories
                    val app = application as MpsApplication
                    MpsNavGraph(
                        app           = app,
                        navController = navController,
                        darkTheme     = darkTheme,
                        onToggleTheme = {
                            lifecycleScope.launch {
                                ThemePreferences.saveDarkTheme(applicationContext,!darkTheme)
                            }
                        },
                        selectedLanguage = language,
                        onLanguageSelect = { newLang ->
                            kotlinx.coroutines.MainScope().launch {
                                ThemePreferences.setLanguage(applicationContext,newLang)
                            }
                        }
                    )
                }
            }
        }
    }
}