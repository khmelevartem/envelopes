package com.tubetoast.envelopes.android.presentation.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.tubetoast.envelopes.android.presentation.navigation.AppNavigation
import com.tubetoast.envelopes.android.presentation.navigation.Back
import com.tubetoast.envelopes.android.presentation.navigation.Navigate
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EditCategoryScreen(
    navigate: Navigate,
    viewModel: EditCategoryViewModel = koinViewModel(),
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
                    navigate(Back)
                },
                enabled = categoryOperations.canConfirm
            ) {
                Text(text = "Confirm")
            }
            Button(
                onClick = {
                    viewModel.delete()
                    navigate(Back)
                },
                enabled = categoryOperations.canDelete
            ) {
                Text(text = "Delete")
            }

            val envelope by remember { viewModel.envelope }
            Button(
                onClick = {
                    navigate(AppNavigation.selectEnvelope(draftCategory))
                },
                enabled = viewModel.canChooseEnvelope()
            ) {
                Text(text = "Envelope: ${envelope.name}")
            }
        }
    }
}
