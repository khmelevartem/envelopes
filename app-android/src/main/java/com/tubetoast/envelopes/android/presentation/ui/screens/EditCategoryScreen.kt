package com.tubetoast.envelopes.android.presentation.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.tubetoast.envelopes.android.presentation.navigation.AppNavigation
import com.tubetoast.envelopes.android.presentation.navigation.Back
import com.tubetoast.envelopes.android.presentation.navigation.Navigate
import com.tubetoast.envelopes.android.presentation.ui.screens.EditCategoryViewModel.CategoryOperations
import com.tubetoast.envelopes.android.presentation.ui.theme.EnvelopesTheme
import com.tubetoast.envelopes.android.presentation.ui.theme.topAppBarColors
import com.tubetoast.envelopes.android.presentation.ui.views.ApplyOrCloseButtons
import com.tubetoast.envelopes.android.presentation.ui.views.BackButton
import com.tubetoast.envelopes.android.presentation.ui.views.PeriodControl
import com.tubetoast.envelopes.android.presentation.ui.views.PeriodControlViewModel
import com.tubetoast.envelopes.common.domain.models.Category
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EditCategoryScreen(
    navigate: Navigate,
    viewModel: EditCategoryViewModel = koinViewModel(),
    periodControlViewModel: PeriodControlViewModel = koinViewModel(),
    categoryId: Int? = null,
    envelopeId: Int? = null
) {
    Column(verticalArrangement = Arrangement.SpaceBetween) {
        val draftCategory by remember { viewModel.init(categoryId, envelopeId) }
        val categoryOperations by remember { viewModel.categoryOperations }
        val isNew by remember { viewModel.isNewCategory }
        val envelope by remember { viewModel.envelope }

        Column {
            EditCategoryTopAppBar(
                periodControlViewModel,
                isNew,
                categoryOperations,
                viewModel::delete,
                navigate
            )
            CategoryInfo(draftCategory, viewModel)
            val cardModifier = if (categoryOperations.canChooseEnvelope) {
                Modifier
                    .clickable { navigate(AppNavigation.selectEnvelope(draftCategory)) }
            } else {
                Modifier
            }
            Surface(
                modifier = Modifier
                    .padding(top = 16.dp, end = 8.dp, start = 8.dp)
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(8.dp))
                    .border(BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Box(
                    modifier = cardModifier
                ) {
                    Text(
                        text = "Envelope: ${envelope.name}",
                        modifier = Modifier.align(Alignment.CenterStart).padding(horizontal = 16.dp, vertical = 16.dp),
                        maxLines = 1
                    )
                }
            }
        }

        ApplyOrCloseButtons(
            onAbort = { navigate(Back) },
            canConfirm = categoryOperations.canConfirm,
            onConfirm = {
                viewModel.confirm()
                navigate(Back)
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditCategoryTopAppBar(
    periodControlViewModel: PeriodControlViewModel,
    isNewCategory: Boolean,
    operations: CategoryOperations,
    onDelete: () -> Unit,
    navigate: Navigate
) {
    TopAppBar(
        colors = EnvelopesTheme.topAppBarColors(),
        title = { Text(text = if (isNewCategory) "Add category" else "Edit category") },
        navigationIcon = { BackButton(navigate) },
        actions = {
            PeriodControl(periodControlViewModel)
            IconButton(
                onClick = {
                    onDelete()
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
private fun CategoryInfo(
    draftCategory: Category,
    viewModel: EditCategoryViewModel
) {
    OutlinedTextField(
        value = draftCategory.name,
        onValueChange = { viewModel.setName(it) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, end = 8.dp, start = 8.dp),
        label = { Text("Name") },
        maxLines = 1
    )
    OutlinedTextField(
        value = draftCategory.limit?.units?.takeIf { it > 0 }?.toString().orEmpty(),
        onValueChange = { viewModel.setLimit(it) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, end = 8.dp, start = 8.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        label = { Text("Limit") },
        maxLines = 1
    )
}
