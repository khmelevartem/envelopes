package com.tubetoast.envelopes.ui.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object Typography {
    val DisplayLarge = 42.sp
    val DisplayMedium = 24.sp
    val BodyLarge = 12.sp
    val BodyMedium = 11.sp
}

object FontWeights {
    val ExtraBold = FontWeight.ExtraBold
    val Bold = FontWeight.Bold
}

object Dimensions {
    val PaddingSmall = 8.dp
    val PaddingMedium = 12.dp
    val PaddingLarge = 24.dp
    val SpacingSmall = 8.dp
    val SpacingXSmall = 4.dp
    val IconSmall = 14.dp
    val PaddingBottomNav = 80.dp
}

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
fun EnvelopesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnvelopesTheme.topAppBarColors(): TopAppBarColors =
    TopAppBarDefaults.topAppBarColors(
        containerColor = Color.Black,
        titleContentColor = Color.White,
        actionIconContentColor = Color.White,
        navigationIconContentColor = Color.White
    )
