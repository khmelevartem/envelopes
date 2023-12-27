package com.tubetoast.envelopes.android.presentation.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavHostController
import com.tubetoast.envelopes.android.presentation.ui.theme.EnvelopesTheme

@Composable
fun EditEnvelopeScreen(
    navController: NavHostController,
    editEnvelopeViewModel: EditEnvelopeViewModel,
    envelopeId: Int? = null
) {
    EnvelopesTheme {
        Column {
            val envelope by remember { editEnvelopeViewModel.envelope(envelopeId) }
            TextField(
                value = envelope.name,
                onValueChange = { editEnvelopeViewModel.setName(it) },
                modifier = Modifier.fillMaxWidth(),
            )
            TextField(
                value = envelope.limit.units.takeIf { it > 0 }?.toString().orEmpty(),
                onValueChange = { editEnvelopeViewModel.setLimit(it) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )
            Row {
                Button(
                    onClick = {
                        editEnvelopeViewModel.confirm()
                        navController.popBackStack()
                    },
                    enabled = editEnvelopeViewModel.canConfirm(),
                ) {
                    Text(text = "Confirm")
                }
                Button(
                    onClick = {
                        editEnvelopeViewModel.delete()
                        navController.popBackStack()
                    },
                    enabled = editEnvelopeViewModel.canDelete(),
                ) {
                    Text(text = "Delete")
                }
            }
        }
    }
}
