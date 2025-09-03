package com.example.amazonqdev.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val AlcoLookColorScheme = lightColorScheme(
    primary = Color(0xFF3B82F6),
    onPrimary = Color(0xFFFFFFFF),
    background = Color(0xFFF7F8FA),
    onBackground = Color(0xFF1A1C20),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF1A1C20),
    onSurfaceVariant = Color(0xFF6B7280),
    outline = Color(0xFFE9ECF1),
    surfaceVariant = Color(0xFFF7F8FA),
    primaryContainer = Color(0xFFEBF4FF),
    onPrimaryContainer = Color(0xFF1A1C20)
)

object Spacing {
    val XS = 4.dp
    val SM = 8.dp
    val MD = 12.dp
    val LG = 16.dp
    val XL = 20.dp
    val XXL = 24.dp
}

object Elevation {
    val Card = 2.dp
    val BottomNav = 8.dp
}

@Composable
fun AlcoLookTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = AlcoLookColorScheme,
        content = content
    )
}