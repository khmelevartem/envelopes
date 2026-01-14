package com.tubetoast.envelopes.android.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.tubetoast.envelopes.android.presentation.navigation.Back
import com.tubetoast.envelopes.android.presentation.navigation.Navigate
import com.tubetoast.envelopes.android.presentation.ui.theme.EnvelopesTheme
import com.tubetoast.envelopes.android.presentation.ui.theme.topAppBarColors
import com.tubetoast.envelopes.android.presentation.ui.views.ApplyOrCloseButtons
import com.tubetoast.envelopes.android.presentation.ui.views.BackButton
import com.tubetoast.envelopes.android.presentation.ui.views.DatePickerFieldToModal
import com.tubetoast.envelopes.android.presentation.ui.views.SelectableCategoriesList
import com.tubetoast.envelopes.android.presentation.ui.views.SelectableCategoriesListViewModel
import com.tubetoast.envelopes.common.domain.models.Goal
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EditGoalScreen(
    navigate: Navigate,
    editGoalViewModel: EditGoalViewModel = koinViewModel(),
    selectableCategoriesListViewModel: SelectableCategoriesListViewModel = koinViewModel(),
    goalId: Int? = null
) {
    val draftGoal by remember { editGoalViewModel.goal(goalId) }
    val isNewGoal by remember { editGoalViewModel.isNewGoal }
    val operations by remember { editGoalViewModel.operations }
    Box {
        Column(
            modifier = Modifier.fillMaxSize().align(Alignment.TopCenter)
        ) {
            EditGoalTopAppBar(navigate, isNewGoal)
            GoalInfo(
                draftGoal = draftGoal,
                editGoalViewModel = editGoalViewModel
            )
            SelectableCategoriesList(
                selectableCategoriesListViewModel
            )
        }
        ApplyOrCloseButtons(
            modifier = Modifier.align(Alignment.BottomCenter)
                .background(
                    brush = Brush.verticalGradient(
                        0.1f to Color.Transparent,
                        0.5f to MaterialTheme.colorScheme.background
                    )

                ),
            onAbort = { navigate(Back) },
            canConfirm = operations.canConfirm,
            onConfirm = {
                editGoalViewModel.confirm()
                navigate(Back)
            }
        )
    }
}

@Composable
fun GoalInfo(
    draftGoal: Goal,
    editGoalViewModel: EditGoalViewModel
) {
    Column(
        modifier = Modifier.padding(top = 8.dp, end = 8.dp, start = 8.dp)
    ) {
        OutlinedTextField(
            value = draftGoal.name,
            onValueChange = { editGoalViewModel.setName(it) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Name") },
            maxLines = 1
        )
        OutlinedTextField(
            value = draftGoal.target.units.takeIf { it > 0 }?.toString().orEmpty(),
            onValueChange = { editGoalViewModel.setTarget(it) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text("Target") },
            maxLines = 1
        )
        DatePickerFieldToModal(label = "Start date", selectedDate = draftGoal.start) {
            editGoalViewModel.setStart(it)
        }
        DatePickerFieldToModal(label = "End date", selectedDate = draftGoal.finish) {
            editGoalViewModel.setFinish(it)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditGoalTopAppBar(
    navigate: Navigate,
    isNewGoal: Boolean
) {
    TopAppBar(
        colors = EnvelopesTheme.topAppBarColors(),
        title = { Text(text = if (isNewGoal) "Add goal" else "Edit goal") },
        navigationIcon = { BackButton(navigate) }
    )
}
