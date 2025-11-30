package com.tubetoast.envelopes.android.presentation.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.contentColorFor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.tubetoast.envelopes.android.presentation.navigation.AppNavigation
import com.tubetoast.envelopes.android.presentation.navigation.Back
import com.tubetoast.envelopes.android.presentation.navigation.Navigate
import com.tubetoast.envelopes.android.presentation.ui.views.BackButton
import com.tubetoast.envelopes.android.presentation.ui.views.CardItem
import com.tubetoast.envelopes.android.presentation.ui.views.CategoryWithSumView
import com.tubetoast.envelopes.android.presentation.ui.views.PeriodControl
import com.tubetoast.envelopes.android.presentation.ui.views.PeriodControlViewModel
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.snapshots.CategorySnapshot
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EditEnvelopeScreen(
    navigate: Navigate,
    editEnvelopeViewModel: EditEnvelopeViewModel = koinViewModel(),
    periodControlViewModel: PeriodControlViewModel = koinViewModel(),
    envelopeId: Int? = null
) {
    Column(verticalArrangement = Arrangement.SpaceBetween) {
        val draftEnvelope by remember { editEnvelopeViewModel.envelope(envelopeId) }
        val envelopeOperations by remember { editEnvelopeViewModel.operations }
        val categories by remember { editEnvelopeViewModel.categories }
        Column {
            EditEnvelopeTopAppBar(periodControlViewModel, editEnvelopeViewModel, navigate)
            EnvelopeInfo(draftEnvelope, editEnvelopeViewModel)
            Categories(categories, draftEnvelope, navigate)
        }

        Buttons(navigate, envelopeOperations, editEnvelopeViewModel)
    }
}

@Composable
private fun EditEnvelopeTopAppBar(
    periodControlViewModel: PeriodControlViewModel,
    editEnvelopeViewModel: EditEnvelopeViewModel,
    navigate: Navigate
) {
    val isNewEnvelope by remember { editEnvelopeViewModel.isNewEnvelope }
    TopAppBar(
        backgroundColor = Color.Black,
        contentColor = Color.White,
        title = { Text(text = if (isNewEnvelope) "Add envelope" else "Edit envelope") },
        navigationIcon = { BackButton(navigate) },
        actions = {
            PeriodControl(periodControlViewModel)
            val operations by remember { editEnvelopeViewModel.operations }
            IconButton(
                onClick = {
                    editEnvelopeViewModel.delete()
                    navigate(Back)
                },
                enabled = operations.canDelete
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    )
}

@Composable
private fun EnvelopeInfo(
    draftEnvelope: Envelope,
    editEnvelopeViewModel: EditEnvelopeViewModel
) {
    OutlinedTextField(
        value = draftEnvelope.name,
        onValueChange = { editEnvelopeViewModel.setName(it) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, end = 8.dp, start = 8.dp),
        label = { Text("Name") },
        maxLines = 1
    )
    OutlinedTextField(
        value = draftEnvelope.limit.units.takeIf { it > 0 }?.toString().orEmpty(),
        onValueChange = { editEnvelopeViewModel.setLimit(it) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, end = 8.dp, start = 8.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        label = { Text("Limit") },
        maxLines = 1
    )
}

@Composable
private fun Categories(
    categories: List<CategorySnapshot>,
    draftEnvelope: Envelope,
    navigate: Navigate
) {
    LazyColumn(
        modifier = Modifier.padding(top = 8.dp, end = 8.dp, start = 8.dp)
    ) {
        items(categories) { categorySnapshot ->
            CategoryWithSumView(
                snapshot = categorySnapshot,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                navigate(
                    AppNavigation.editCategory(categorySnapshot.category, draftEnvelope)
                )
            }
        }
    }
}

@Composable
private fun Buttons(
    navigate: Navigate,
    envelopeOperations: EditEnvelopeViewModel.EnvelopeOperations,
    editEnvelopeViewModel: EditEnvelopeViewModel
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
            IconButton(onClick = { navigate(Back) }) {
                Icon(
                    tint = contentColorFor(MaterialTheme.colors.secondary),
                    imageVector = Icons.Default.Close,
                    contentDescription = "Cancel"
                )
            }
        }

        val canConfirm = envelopeOperations.canConfirm
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
                onClick = {
                    editEnvelopeViewModel.confirm()
                    navigate(Back)
                },
                enabled = envelopeOperations.canConfirm
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
