package com.tubetoast.envelopes.android.presentation.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tubetoast.envelopes.android.presentation.ui.AppNavigation
import com.tubetoast.envelopes.android.presentation.ui.views.EnvelopeLabelView
import com.tubetoast.envelopes.android.presentation.ui.views.MainListView
import com.tubetoast.envelopes.android.presentation.ui.views.PlusView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ChooseEnvelopeScreen(
    navController: NavController,
    viewModel: ChooseEnvelopeViewModel,
    categoryId: Int? = null,
    envelopeId: Int? = null
) {
    Column {
        val category by remember { viewModel.category(categoryId) }
        val envelopesState =
            viewModel.envelopes(envelopeId).collectAsState(initial = emptyList())
        val envelopes by remember { envelopesState }
        val iteModels = envelopes.asItemModels()
        Text(
            text = category.name,
            fontSize = TextUnit(value = 24f, type = TextUnitType.Sp),
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(24.dp)
        )
        LazyColumn {
            items(iteModels) {
                MainListView {
                    val scope = rememberCoroutineScope()
                    EnvelopeLabelView(
                        itemModel = it,
                        isChosen = viewModel.isChosen(it.data)
                    ) {
                        viewModel.setNewChosenEnvelope(it.data)
                        scope.launch {
                            delay(500)
                            navController.popBackStack(AppNavigation.start, false)
                        }
                    }
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
