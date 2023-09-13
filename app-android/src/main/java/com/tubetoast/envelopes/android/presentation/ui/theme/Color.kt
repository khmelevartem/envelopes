package com.tubetoast.envelopes.android.presentation.ui.theme

import androidx.compose.ui.graphics.Color
import com.tubetoast.envelopes.android.presentation.ui.theme.EColor.palette

val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)

object EColor {

    val Blue = Color(0xff6cbaff)
    val Mint = Color(0xff00e2cf)
    val GreenLight = Color(0xff8ef488)
    val Yellow = Color(0xffffe88b)
    val Peach = Color(0xffffc5a3)
    val Pink = Color(0xffffd7ff)
    val Vanilla = Color(0xffc9d7ff)

    val palette = listOf(
        Blue,
        Mint,
        GreenLight,
        Yellow,
        Peach,
        Pink,
        Vanilla,
    )

    val GrayDarker = Color(0xff74747f)
    val GrayDark = Color(0xff94949f)
    val Gray = Color(0xffa8a8a6)
    val GrayLight = Color(0xffb9b9c9)
    val GrayLighter = Color(0xffe9e9f9)
}

fun List<Color>.next(index: Int) = palette[index.mod(palette.size)]
