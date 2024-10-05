package com.tubetoast.envelopes.android.presentation.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.tubetoast.envelopes.android.presentation.ui.AppNavigation
import com.tubetoast.envelopes.android.presentation.ui.views.CategoryWithSumView
import com.tubetoast.envelopes.android.presentation.ui.views.EnvelopesTopAppBar
import com.tubetoast.envelopes.android.presentation.ui.views.TopAppBarViewModel

@Composable
fun EditEnvelopeScreen(
    navController: NavHostController,
    editEnvelopeViewModel: EditEnvelopeViewModel,
    topAppBarViewModel: TopAppBarViewModel,
    envelopeId: Int? = null
) {
    Column {
        val draftEnvelope by remember { editEnvelopeViewModel.envelope(envelopeId) }
        val envelopeOperations by remember { editEnvelopeViewModel.operations }
        val categories by remember { editEnvelopeViewModel.categories }
        EnvelopesTopAppBar(topAppBarViewModel, navController)
        TextField(
            value = draftEnvelope.name,
            onValueChange = { editEnvelopeViewModel.setName(it) },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = draftEnvelope.limit.units.takeIf { it > 0 }?.toString().orEmpty(),
            onValueChange = { editEnvelopeViewModel.setLimit(it) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Row {
            Button(
                onClick = {
                    editEnvelopeViewModel.confirm()
                    navController.popBackStack()
                },
                enabled = envelopeOperations.canConfirm
            ) {
                Text(text = "Confirm", color = MaterialTheme.colors.onSurface)
            }
            Button(
                onClick = {
                    editEnvelopeViewModel.delete()
                    navController.popBackStack()
                },
                enabled = envelopeOperations.canDelete
            ) {
                Text(text = "Delete", color = MaterialTheme.colors.onSurface)
            }
        }
        LazyColumn {
            items(categories) { categorySnapshot ->
                CategoryWithSumView(
                    snapshot = categorySnapshot,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    navController.navigate(
                        AppNavigation.editCategory(categorySnapshot.category, draftEnvelope)
                    )
                }
            }
        }
    }
}
