package com.tubetoast.envelopes.android.presentation.ui.views

import androidx.compose.foundation.clickable
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.tubetoast.envelopes.android.presentation.ui.AppNavigation
import com.tubetoast.envelopes.android.presentation.ui.screens.EditEnvelopeViewModel

@Composable
fun EnvelopesListTopAppBar(
    periodControlViewModel: PeriodControlViewModel,
    navController: NavController
) {
    TopAppBar(
        backgroundColor = Color.Black,
        contentColor = Color.White,
        title = {
            Text(
                text = "Envelopes",
                modifier = Modifier.clickable {
                    navController.navigate(AppNavigation.goalsList)
                }
            )
        },
        actions = {
            PeriodControl(periodControlViewModel)
            SettingsButton(navController)
        }
    )
}

@Composable
fun EditEnvelopeTopAppBar(
    periodControlViewModel: PeriodControlViewModel,
    editEnvelopeViewModel: EditEnvelopeViewModel,
    navController: NavController
) {
    val isNewEnvelope by remember { editEnvelopeViewModel.isNewEnvelope }
    TopAppBar(
        backgroundColor = Color.Black,
        contentColor = Color.White,
        title = { Text(text = if (isNewEnvelope) "Add envelope" else "Edit envelope") },
        navigationIcon = { BackButton(navController) },
        actions = {
            PeriodControl(periodControlViewModel)
            val operations by remember { editEnvelopeViewModel.operations }
            IconButton(
                onClick = {
                    editEnvelopeViewModel.delete()
                    navController.popBackStack()
                },
                enabled = operations.canDelete
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    )
}

@Composable
private fun BackButton(navController: NavController) {
    IconButton(onClick = { navController.popBackStack() }) {
        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
    }
}

@Composable
fun SettingsTopAppBar(
    navController: NavController
) {
    TopAppBar(
        backgroundColor = Color.Black,
        contentColor = Color.White,
        title = { Text(text = "Settings") },
        navigationIcon = { BackButton(navController) }
    )
}

@Composable
fun GoalsListTopAppBar(
    navController: NavController
) {
    TopAppBar(
        backgroundColor = Color.Black,
        contentColor = Color.White,
        title = { Text(text = "Goals") },
        navigationIcon = { BackButton(navController) },
        actions = {
            SettingsButton(navController)
        }
    )
}

@Composable
private fun SettingsButton(navController: NavController) {
    IconButton(
        onClick = { navController.navigate(AppNavigation.settings) }
    ) {
        Icon(Icons.Default.Settings, contentDescription = "Settings")
    }
}
