package com.tubetoast.envelopes.android.presentation.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.tubetoast.envelopes.android.presentation.ui.views.GoalsListTopAppBar
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun GoalsListScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: GoalsListViewModel = koinViewModel()
) {
    Column {
        GoalsListTopAppBar(navController)
    }
}
