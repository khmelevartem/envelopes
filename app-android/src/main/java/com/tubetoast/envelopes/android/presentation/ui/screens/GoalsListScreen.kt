package com.tubetoast.envelopes.android.presentation.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tubetoast.envelopes.android.presentation.navigation.AppNavigation
import com.tubetoast.envelopes.android.presentation.navigation.Navigate
import com.tubetoast.envelopes.android.presentation.ui.theme.EnvelopesTheme
import com.tubetoast.envelopes.android.presentation.ui.theme.topAppBarColors
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
    val isColorful by viewModel.isColorful.collectAsState()
    val items = goals.asItemModels(isColorful.checked)
    Column(modifier = modifier) {
        GoalsListTopAppBar(navigate)
        LazyColumn(modifier = Modifier.padding(top = 4.dp)) {
            items(items, key = { item -> item.data.goal.id.code }) { item ->
                GoalItem(
                    itemModel = item,
                    onEditClick = { navigate(AppNavigation.editGoal(it)) },
                    onDeleteClick = { viewModel.delete(it) }
                )
            }
            item {
                CardItem(
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.height(64.dp)
                ) {
                    IconButton(onClick = { navigate(AppNavigation.addGoal()) }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            tint = MaterialTheme.colorScheme.onSecondary,
                            contentDescription = "Add goal"
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GoalsListTopAppBar(
    navController: Navigate
) {
    TopAppBar(
        colors = EnvelopesTheme.topAppBarColors(),
        title = { Text(text = "Goals") },
        navigationIcon = { BackButton(navController) }
    )
}
