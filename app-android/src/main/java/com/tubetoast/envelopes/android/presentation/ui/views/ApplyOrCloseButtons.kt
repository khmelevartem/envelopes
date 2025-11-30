package com.tubetoast.envelopes.android.presentation.ui.views

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.contentColorFor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ApplyOrCloseButtons(
    onAbort: () -> Unit,
    canConfirm: Boolean,
    onConfirm: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .padding(8.dp)
    ) {
        CardItem(
            color = MaterialTheme.colors.secondary,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            IconButton(onClick = onAbort) {
                Icon(
                    tint = contentColorFor(MaterialTheme.colors.secondary),
                    imageVector = Icons.Default.Close,
                    contentDescription = "Cancel"
                )
            }
        }

        val colors = ButtonDefaults.buttonColors()
        val background by colors.backgroundColor(enabled = canConfirm)
        val content by colors.contentColor(enabled = canConfirm)
        CardItem(
            color = background,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            IconButton(
                onClick = onConfirm,
                enabled = canConfirm
            ) {
                Icon(
                    tint = content,
                    imageVector = Icons.Default.Done,
                    contentDescription = "Confirm"
                )
            }
        }
    }
}
