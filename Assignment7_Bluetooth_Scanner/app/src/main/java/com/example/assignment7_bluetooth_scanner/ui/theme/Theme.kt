package com.example.assignment7_bluetooth_scanner.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF185FA5),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE6F1FB),
    onPrimaryContainer = Color(0xFF0C447C),
    background = Color(0xFFF8F7F2),
    onBackground = Color(0xFF2C2C2A),
    surface = Color.White,
    onSurface = Color(0xFF2C2C2A),
    surfaceVariant = Color(0xFFD3D1C7),
    onSurfaceVariant = Color(0xFF5F5E5A),
    outline = Color(0xFF888780),
    outlineVariant = Color(0xFFD3D1C7),
    errorContainer = Color(0xFFFCEBEB),
    onErrorContainer = Color(0xFFA32D2D),
    tertiaryContainer = Color(0xFFE1F5EE),
    onTertiaryContainer = Color(0xFF085041),
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF85B7EB),
    onPrimary = Color(0xFF042C53),
    primaryContainer = Color(0xFF0C447C),
    onPrimaryContainer = Color(0xFFB5D4F4),
    background = Color(0xFF2C2C2A),
    onBackground = Color(0xFFD3D1C7),
    surface = Color(0xFF444441),
    onSurface = Color(0xFFD3D1C7),
    surfaceVariant = Color(0xFF5F5E5A),
    onSurfaceVariant = Color(0xFFB4B2A9),
    outline = Color(0xFF9D9D93),
    outlineVariant = Color(0xFF5F5E5A),
    errorContainer = Color(0xFF501313),
    onErrorContainer = Color(0xFFF7C1C1),
    tertiaryContainer = Color(0xFF085041),
    onTertiaryContainer = Color(0xFF9FE1CB),
)

@Composable
fun Assignment7_Bluetooth_ScannerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}