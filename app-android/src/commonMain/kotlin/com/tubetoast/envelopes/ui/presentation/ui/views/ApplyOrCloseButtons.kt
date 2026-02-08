package com.tubetoast.envelopes.ui.presentation.ui.views

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ApplyOrCloseButtons(
    modifier: Modifier = Modifier,
    onAbort: () -> Unit,
    canConfirm: Boolean,
    onConfirm: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(72.dp)
            .padding(bottom = 8.dp, top = 12.dp)
    ) {
        val color = MaterialTheme.colorScheme.secondary
        CardItem(
            color = color,
            modifier = Modifier.weight(1f)
        ) {
            IconButton(onClick = onAbort) {
                Icon(
                    tint = contentColorFor(color),
                    imageVector = Icons.Default.Close,
                    contentDescription = "Cancel"
                )
            }
        }

        val buttonColor =
            if (canConfirm) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer
        CardItem(
            color = buttonColor,
            modifier = Modifier.weight(1f)
        ) {
            IconButton(
                onClick = onConfirm,
                enabled = canConfirm
            ) {
                Icon(
                    tint = contentColorFor(buttonColor),
                    imageVector = Icons.Default.Done,
                    contentDescription = "Confirm"
                )
            }
        }
    }
}
