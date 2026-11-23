package com.youarelaunched.challenge.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Green,
    onPrimary = Color.White,
    background = Color.White,
    onBackground = TextPrimary,
    surface = Color.White,
    onSurface = TextPrimary,
    onSurfaceVariant = TextSecondary
)

@Composable
fun VendorsTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        content = content
    )
}
