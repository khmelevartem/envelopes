package com.tubetoast.envelopes.android.presentation.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tubetoast.envelopes.android.presentation.navigation.AppNavigation
import com.tubetoast.envelopes.android.presentation.navigation.Navigate
import com.tubetoast.envelopes.android.presentation.ui.views.BackButton
import com.tubetoast.envelopes.android.presentation.ui.views.CardItem
import com.tubetoast.envelopes.android.presentation.ui.views.GoalItem
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun GoalsListScreen(
    navigate: Navigate,
    modifier: Modifier = Modifier,
    viewModel: GoalsListViewModel = koinViewModel()
) {
    val goals by viewModel.goals.collectAsState()
    val items = goals.toList().asItemModels()
    Column(modifier = modifier) {
        GoalsListTopAppBar(navigate)
        LazyColumn {
            items(items, key = { item -> item.data.id.code }) { item ->
                GoalItem(
                    itemModel = item,
                    onEditClick = { navigate(AppNavigation.editGoal(it)) },
                    onDeleteClick = { viewModel.delete(it) }
                )
            }
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
