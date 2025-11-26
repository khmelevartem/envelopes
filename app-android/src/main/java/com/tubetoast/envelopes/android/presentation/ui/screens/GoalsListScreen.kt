package com.tubetoast.envelopes.android.presentation.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tubetoast.envelopes.android.presentation.ui.AppNavigation
import com.tubetoast.envelopes.android.presentation.ui.views.CardItem
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
        LazyColumn {
            item {
                CardItem(
                    color = MaterialTheme.colors.secondary,
                    modifier = Modifier.height(64.dp)
                ) {
                    IconButton(onClick = { navController.navigate(AppNavigation.editGoal) }) {
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
