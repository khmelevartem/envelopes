package com.tubetoast.envelopes.ui.presentation.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.snapshots.CategorySnapshot
import com.tubetoast.envelopes.ui.presentation.navigation.AppNavigation
import com.tubetoast.envelopes.ui.presentation.navigation.Back
import com.tubetoast.envelopes.ui.presentation.navigation.Navigate
import com.tubetoast.envelopes.ui.presentation.ui.theme.EnvelopesTheme
import com.tubetoast.envelopes.ui.presentation.ui.theme.topAppBarColors
import com.tubetoast.envelopes.ui.presentation.ui.views.ApplyOrCloseButtons
import com.tubetoast.envelopes.ui.presentation.ui.views.BackButton
import com.tubetoast.envelopes.ui.presentation.ui.views.CategoryWithSumView
import com.tubetoast.envelopes.ui.presentation.ui.views.PeriodControl
import com.tubetoast.envelopes.ui.presentation.ui.views.PeriodControlViewModel
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

        ApplyOrCloseButtons(
            onAbort = { navigate(Back) },
            canConfirm = envelopeOperations.canConfirm,
            onConfirm = {
                editEnvelopeViewModel.confirm()
                navigate(Back)
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditEnvelopeTopAppBar(
    periodControlViewModel: PeriodControlViewModel,
    editEnvelopeViewModel: EditEnvelopeViewModel,
    navigate: Navigate
) {
    val isNewEnvelope by remember { editEnvelopeViewModel.isNewEnvelope }
    TopAppBar(
        colors = EnvelopesTheme.topAppBarColors(),
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
        value = draftEnvelope.limit.units
            .takeIf { it > 0 }
            ?.toString()
            .orEmpty(),
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
