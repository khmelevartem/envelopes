package com.tubetoast.envelopes.android.presentation.ui.views

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ButtonDefaults
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
        CardItem(
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.weight(1f)
        ) {
            IconButton(onClick = onAbort) {
                Icon(
                    tint = contentColorFor(MaterialTheme.colorScheme.secondary),
                    imageVector = Icons.Default.Close,
                    contentDescription = "Cancel"
                )
            }
        }

        val colors = ButtonDefaults.buttonColors()
        val background = if (canConfirm) colors.containerColor else colors.disabledContainerColor
        val content = if (canConfirm) colors.contentColor else colors.disabledContentColor
        CardItem(
            color = background,
            modifier = Modifier.weight(1f)
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
