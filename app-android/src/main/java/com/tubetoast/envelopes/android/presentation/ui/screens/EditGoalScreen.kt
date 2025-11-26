package com.tubetoast.envelopes.android.presentation.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tubetoast.envelopes.android.presentation.ui.views.BackButton
import com.tubetoast.envelopes.common.domain.models.Goal
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EditGoalScreen(
    navController: NavController,
    editGoalViewModel: EditGoalViewModel = koinViewModel(),
    goalId: Int? = null
) {
    val draftGoal by remember { editGoalViewModel.goal(goalId) }
    val isNewGoal by remember { editGoalViewModel.isNewGoal }
    Column {
        EditGoalTopAppBar(navController, isNewGoal)
        GoalInfo(draftGoal, editGoalViewModel)
    }
}

@Composable
fun GoalInfo(
    draftGoal: Goal,
    editGoalViewModel: EditGoalViewModel
) {
    OutlinedTextField(
        value = draftGoal.name,
        onValueChange = { editGoalViewModel.setName(it) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, end = 8.dp, start = 8.dp),
        label = { Text("Name") },
        maxLines = 1
    )
    OutlinedTextField(
        value = draftGoal.target.units.takeIf { it > 0 }?.toString().orEmpty(),
        onValueChange = { editGoalViewModel.setTarget(it) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, end = 8.dp, start = 8.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        label = { Text("Target") },
        maxLines = 1
    )
}

@Composable
private fun EditGoalTopAppBar(
    navController: NavController,
    isNewGoal: Boolean
) {
    TopAppBar(
        backgroundColor = Color.Black,
        contentColor = Color.White,
        title = { Text(text = if (isNewGoal) "Add goal" else "Edit goal") },
        navigationIcon = { BackButton(navController) }
    )
}
