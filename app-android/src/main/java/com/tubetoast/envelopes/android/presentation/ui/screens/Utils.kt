package com.tubetoast.envelopes.android.presentation.ui.screens

import androidx.compose.ui.graphics.Color
import com.tubetoast.envelopes.android.presentation.ui.theme.EColor
import com.tubetoast.envelopes.android.presentation.ui.theme.next

data class ItemModel<T>(
    val data: T,
    var color: Color,
)

fun <T> Collection<T>.asItemModels() =
    mapIndexed { index, value -> ItemModel(value, EColor.palette.next(index)) }
