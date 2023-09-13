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
import androidx.navigation.NavController
import com.tubetoast.envelopes.android.presentation.ui.theme.EnvelopesTheme

@Composable
fun EditCategoryScreen(
    navController: NavController,
    viewModel: EditCategoryViewModel,
) {
    EnvelopesTheme {
        Column {
            val category by remember { viewModel.category }
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
            Row {
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
            }
        }
    }
}
