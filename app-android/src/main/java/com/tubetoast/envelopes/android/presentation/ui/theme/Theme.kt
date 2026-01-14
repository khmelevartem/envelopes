package com.tubetoast.envelopes.android.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColorScheme(
    primary = EColor.Emerald.darken(),
    primaryContainer = EColor.Emerald.darken().copy(alpha = 0.8f),
    secondary = EColor.Charcoal.darken(),
    tertiary = EColor.Vanilla.darken()
)

private val LightColorPalette = lightColorScheme(
    primary = EColor.Emerald,
    primaryContainer = EColor.Emerald.copy(alpha = 0.8f),
    secondary = EColor.Charcoal,
    tertiary = EColor.Vanilla
)

@Composable
fun EnvelopesTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colorScheme = colors,
        shapes = Shapes,
        content = content
    )
}

object EnvelopesTheme

@Composable
fun EnvelopesTheme.topAppBarColors(): TopAppBarColors =
    TopAppBarDefaults.topAppBarColors(
        containerColor = Color.Black,
        titleContentColor = Color.White,
        subtitleContentColor = Color.White,
        actionIconContentColor = Color.White,
        navigationIconContentColor = Color.White
    )
