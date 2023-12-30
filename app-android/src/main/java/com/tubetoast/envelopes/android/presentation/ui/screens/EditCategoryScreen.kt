package com.tubetoast.envelopes.android.presentation.ui.screens

import androidx.compose.foundation.layout.Arrangement
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
import androidx.navigation.NavController
import com.tubetoast.envelopes.android.presentation.ui.AppNavigation
import com.tubetoast.envelopes.android.presentation.ui.theme.EnvelopesTheme

@Composable
fun EditCategoryScreen(
    navController: NavController,
    viewModel: EditCategoryViewModel,
    categoryId: Int? = null,
    envelopeId: Int? = null,
) {
    EnvelopesTheme {
        Column {
            val category by remember { viewModel.category(categoryId) }
            val envelope by remember { viewModel.envelope(envelopeId) }
            TextField(
                value = category.name,
                onValueChange = { viewModel.setName(it) },
                modifier = Modifier.fillMaxWidth(),
            )
            TextField(
                value = category.limit?.units?.takeIf { it > 0 }?.toString().orEmpty(),
                onValueChange = { viewModel.setLimit(it) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        viewModel.confirm()
                        navController.popBackStack()
                    },
                    enabled = viewModel.canConfirm(),
                ) {
                    Text(text = "Confirm")
                }
                Button(
                    onClick = {
                        viewModel.delete()
                        navController.popBackStack()
                    },
                    enabled = viewModel.canDelete(),
                ) {
                    Text(text = "Delete")
                }
                Button(onClick = {
                    navController.navigate(
                        AppNavigation.chooseEnvelope(category, envelope)
                    )
                }) {
                    Text(text = "Envelope: ${envelope.name}")
                }
            }
        }
    }
}
