package com.tubetoast.envelopes.android.presentation.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tubetoast.envelopes.android.presentation.navigation.AppNavigation
import com.tubetoast.envelopes.android.presentation.navigation.Navigate
import com.tubetoast.envelopes.android.presentation.ui.views.BackButton
import com.tubetoast.envelopes.android.presentation.ui.views.CardItem
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun GoalsListScreen(
    navigate: Navigate,
    modifier: Modifier = Modifier,
    viewModel: GoalsListViewModel = koinViewModel()
) {
    Column {
        GoalsListTopAppBar(navigate)
        LazyColumn {
            item {
                CardItem(
                    color = MaterialTheme.colors.secondary,
                    modifier = Modifier.height(64.dp)
                ) {
                    IconButton(onClick = { navigate(AppNavigation.addGoal()) }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            tint = MaterialTheme.colors.onSecondary,
                            contentDescription = "Add goal"
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun GoalsListTopAppBar(
    navController: Navigate
) {
    TopAppBar(
        backgroundColor = Color.Black,
        contentColor = Color.White,
        title = { Text(text = "Goals") },
        navigationIcon = { BackButton(navController) }
    )
}
