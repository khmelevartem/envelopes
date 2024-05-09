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

@Composable
fun EditCategoryScreen(
    navController: NavController,
    viewModel: EditCategoryViewModel,
    categoryId: Int? = null,
    envelopeId: Int? = null
) {
    Column {
        val draftCategory by remember { viewModel.init(categoryId, envelopeId) }
        TextField(
            value = draftCategory.name,
            onValueChange = { viewModel.setName(it) },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = draftCategory.limit?.units?.takeIf { it > 0 }?.toString().orEmpty(),
            onValueChange = { viewModel.setLimit(it) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val categoryOperations by remember { viewModel.categoryOperations }
            Button(
                onClick = {
                    viewModel.confirm()
                    navController.popBackStack()
                },
                enabled = categoryOperations.canConfirm
            ) {
                Text(text = "Confirm")
            }
            Button(
                onClick = {
                    viewModel.delete()
                    navController.popBackStack()
                },
                enabled = categoryOperations.canDelete
            ) {
                Text(text = "Delete")
            }

            val envelope by remember { viewModel.envelope }
            Button(
                onClick = {
                    navController.navigate(
                        AppNavigation.chooseEnvelope(draftCategory, envelope)
                    )
                },
                enabled = viewModel.canChooseEnvelope()
            ) {
                Text(text = "Envelope: ${envelope.name}")
            }
        }
    }
}
