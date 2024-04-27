package com.tubetoast.envelopes.android.presentation.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.tubetoast.envelopes.android.presentation.ui.AppNavigation
import com.tubetoast.envelopes.android.presentation.ui.theme.EnvelopesTheme
import com.tubetoast.envelopes.android.presentation.ui.views.EnvelopeLabelView
import com.tubetoast.envelopes.android.presentation.ui.views.PlusView

@Composable
fun ChooseEnvelopeScreen(
    navController: NavController,
    viewModel: ChooseEnvelopeViewModel,
    categoryId: Int? = null,
    envelopeId: Int? = null
) {
    EnvelopesTheme {
        Column {
            val category by remember { viewModel.category(categoryId) }
            val envelopesState =
                viewModel.envelopes(envelopeId).collectAsState(initial = emptyList())
            val envelopes by remember { envelopesState }
            Text(
                text = category.name,
                modifier = Modifier.fillMaxWidth()
            )
            LazyColumn {
                items(envelopes.asItemModels()) {
                    EnvelopeLabelView(itemModel = it, chosen = viewModel.isChosen(it.data)) {
                        viewModel.setNewChosenEnvelope(it.data)
                        navController.popBackStack(AppNavigation.start, false)
                    }
                }
            }
            PlusView {
                navController.navigate(AppNavigation.addEnvelope())
            }
        }
    }
}
