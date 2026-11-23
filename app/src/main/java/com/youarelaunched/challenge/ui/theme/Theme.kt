package com.youarelaunched.challenge.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Green,
    onPrimary = Color.White,
    background = Color.White,
    onBackground = GreyPrimary,
    surface = Color.White,
    onSurface = GreyPrimary,
    surfaceVariant = GreyLight,
    onSurfaceVariant = GreySecondary
)

@Composable
fun VendorsTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = VendorsTypography,
        content = content
    )
}
