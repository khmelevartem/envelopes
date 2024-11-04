package com.tubetoast.envelopes.android.presentation.ui.views

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.tubetoast.envelopes.android.presentation.ui.AppNavigation
import com.tubetoast.envelopes.android.presentation.ui.screens.EditEnvelopeViewModel

@Composable
fun EnvelopesListTopAppBar(
    periodControlViewModel: PeriodControlViewModel,
    navController: NavHostController
) {
    TopAppBar(
        backgroundColor = Color.Black,
        contentColor = Color.White,
        title = { Text("Envelopes") },
        actions = {
            PeriodControl(periodControlViewModel)
            IconButton(
                onClick = { navController.navigate(AppNavigation.settings) }
            ) {
                Icon(Icons.Default.Settings, contentDescription = "Settings")
            }
        }
    )
}

@Composable
fun EditEnvelopeTopAppBar(
    periodControlViewModel: PeriodControlViewModel,
    editEnvelopeViewModel: EditEnvelopeViewModel,
    navController: NavHostController
) {
    TopAppBar(
        backgroundColor = Color.Black,
        contentColor = Color.White,
        title = { Text(text = if (editEnvelopeViewModel.isNewEnvelope) "Add envelope" else "Edit envelope") },
//        navigationIcon = {
//            IconButton(onClick = { navController.popBackStack() }) {
//                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
//            }
//        },
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
