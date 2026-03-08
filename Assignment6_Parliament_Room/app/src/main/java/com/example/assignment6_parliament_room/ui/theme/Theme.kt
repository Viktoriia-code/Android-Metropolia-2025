package com.example.assignment6_parliament_room.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary              = Color(0xFF1A5276),
    onPrimary            = Color.White,
    primaryContainer     = Color(0xFFD6E4F0),
    onPrimaryContainer   = Color(0xFF0A2540),
    secondary            = Color(0xFF2E86C1),
    onSecondary          = Color.White,
    secondaryContainer   = Color(0xFFEBF5FB),
    onSecondaryContainer = Color(0xFF1A5276),
    background           = Color(0xFFF5F5F5),
    onBackground         = Color(0xFF1A1A1A),
    surface              = Color(0xFFFFFFFF),
    onSurface            = Color(0xFF1A1A1A),
    surfaceVariant       = Color(0xFFE8EEF4),
    onSurfaceVariant     = Color(0xFF4A5568),
    error                = Color(0xFFB71C1C),
    onError              = Color.White,
    errorContainer       = Color(0xFFFFDAD6),
    onErrorContainer     = Color(0xFF410002)
)

private val DarkColorScheme = darkColorScheme(
    primary              = Color(0xFF90CAF9),
    onPrimary            = Color(0xFF003258),
    primaryContainer     = Color(0xFF1A4F72),
    onPrimaryContainer   = Color(0xFFD6E4F0),
    secondary            = Color(0xFF64B5F6),
    onSecondary          = Color(0xFF003258),
    secondaryContainer   = Color(0xFF1E3A5F),
    onSecondaryContainer = Color(0xFFBBDEFB),
    background           = Color(0xFF121212),
    onBackground         = Color(0xFFE8E8E8),
    surface              = Color(0xFF1E1E1E),
    onSurface            = Color(0xFFE8E8E8),
    surfaceVariant       = Color(0xFF2C2C2C),
    onSurfaceVariant     = Color(0xFFB0BEC5),
    error                = Color(0xFFEF9A9A),
    onError              = Color(0xFF690005),
    errorContainer       = Color(0xFF93000A),
    onErrorContainer     = Color(0xFFFFDAD6)
)

@Composable
fun Assignment6_Parliament_RoomTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}