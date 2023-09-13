package com.tubetoast.envelopes.android.presentation.ui.screens

import androidx.compose.ui.graphics.Color
import com.tubetoast.envelopes.android.presentation.ui.theme.EColor
import com.tubetoast.envelopes.android.presentation.ui.theme.next

data class SnapshotItemModel<T>(
    val snapshot: T,
    var color: Color,
)

fun <T> Collection<T>.asItemModels() =
    mapIndexed { index, value -> SnapshotItemModel(value, EColor.palette.next(index)) }
