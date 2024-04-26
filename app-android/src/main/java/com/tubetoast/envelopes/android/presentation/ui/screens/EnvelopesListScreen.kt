package com.tubetoast.envelopes.android.presentation.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.tubetoast.envelopes.android.presentation.ui.AppNavigation
import com.tubetoast.envelopes.android.presentation.ui.theme.EnvelopesTheme
import com.tubetoast.envelopes.android.presentation.ui.views.EnvelopeView
import com.tubetoast.envelopes.android.presentation.ui.views.MainListView
import com.tubetoast.envelopes.android.presentation.ui.views.PlusView

@Composable
fun EnvelopesListScreen(
    navController: NavHostController,
    envelopesListViewModel: EnvelopesListViewModel
) {
    val itemModelsState = envelopesListViewModel.itemModels.collectAsState(initial = emptyList())
    val itemModels by remember { itemModelsState }
    EnvelopesTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            Column {
                TopAppBar(
                    backgroundColor = Color.Black,
                    contentColor = Color.White,
                    title = { Text("Envelopes") },
                    actions = {
                        IconButton(
                            onClick = { navController.navigate(AppNavigation.settings) }
                        ) {
                            Icon(Icons.Default.Settings, contentDescription = "Import")
                        }
                    }
                )
                LazyColumn {
                    items(itemModels.asItemModels()) { itemModel ->
                        MainListView {
                            EnvelopeView(
                                itemModel,
                                onEditClick = { navController.navigate(AppNavigation.editEnvelope(it)) },
                                onDeleteClick = { envelopesListViewModel.delete(it) },
                                onAddClick = { navController.navigate(AppNavigation.addCategory(it)) },
                                onCategoryClick = { category, envelope ->
                                    navController.navigate(
                                        AppNavigation.editCategory(category, envelope)
                                    )
                                }
                            )
                        }
                    }
                    item {
                        MainListView {
                            PlusView {
                                navController.navigate(AppNavigation.addEnvelope())
                            }
                        }
                    }
                }
            }
        }
    }
}
