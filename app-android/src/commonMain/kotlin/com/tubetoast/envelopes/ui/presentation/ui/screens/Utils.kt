package com.tubetoast.envelopes.ui.presentation.ui.screens

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.tubetoast.envelopes.ui.presentation.ui.theme.EColor
import com.tubetoast.envelopes.ui.presentation.ui.theme.next

data class ItemModel<T>(
    val data: T,
    var color: Color
)

@Composable
fun <T> Iterable<T>.asItemModels(colorful: Boolean = true) =
    mapIndexed { index, value ->
        ItemModel(
            value,
            if (colorful) {
                EColor.ePalette().next(index)
            } else {
                MaterialTheme.colorScheme.surfaceContainer
            }
        )
    }
