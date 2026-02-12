@file:Suppress("MemberVisibilityCanBePrivate")

package com.tubetoast.envelopes.ui.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object EColor {
    // Core brand colors
    val Emerald = Color(0xff237D62)
    val Charcoal = Color(0xff49637b)
    val Haki = Color(0xff669672)
    val Vanilla = Color(0xffc9d7ff)

    // Palette colors
    val Azure = Color(0xff6cbaff)
    val Mint = Color(0xff00e2cf)
    val Salad = Color(0xff8ef488)
    val Lemon = Color(0xffffe88b)
    val Peach = Color(0xffffc5a3)
    val Rose = Color(0xffffd7ff)
    val Plum = Color(0xffa9a7fe)

    // Pace/Status colors
    val PaceMint = Color(0xFF00C896)
    val PaceAmber = Color(0xFFFFB74D)
    val PaceCoral = Color(0xFFFF5252)

    // UI surface colors
    val SurfaceDark = Color(0xFF2A2A36)
    val BackgroundDark = Color(0xFF0F0E13)
    val SurfaceLight = Color(0xFFF5F5F5)
    val BackgroundLight = Color(0xFFFFFFFF)
    val TrackBackground = Color(0xFF1C1C24)

    @Composable
    fun ePalette(isDarkTheme: Boolean = isSystemInDarkTheme()): List<Color> = if (isDarkTheme) darkPalette else lightPalette

    private val lightPalette = listOf(
        Azure,
        Mint,
        Salad,
        Lemon,
        Peach,
        Rose,
        Plum
    )

    private val darkPalette = lightPalette.map { it.darken() }

    val GrayDarker = Color(0xff74747f)
    val GrayDark = Color(0xff94949f)
    val Gray = Color(0xffa8a8a6)
    val GrayLight = Color(0xffb9b9c9)
    val GrayLighter = Color(0xffe9e9f9)
}

fun <T> List<T>.next(index: Int): T = this[index.mod(this.size)]

fun Color.darken() = copy(red = red / 1.5f, green = green / 1.5f, blue = blue / 1.5f)
