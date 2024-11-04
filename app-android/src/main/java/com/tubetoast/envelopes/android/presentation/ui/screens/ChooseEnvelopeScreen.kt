package com.tubetoast.envelopes.android.presentation.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tubetoast.envelopes.android.presentation.ui.AppNavigation
import com.tubetoast.envelopes.android.presentation.ui.views.CardItem
import com.tubetoast.envelopes.android.presentation.ui.views.EnvelopeLabelView
import com.tubetoast.envelopes.common.domain.models.Category
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ChooseEnvelopeScreen(
    navController: NavController,
    viewModel: ChooseEnvelopeViewModel,
    categoryId: Int? = null
) {
    Column {
        val category = viewModel.category(categoryId).collectAsState(initial = Category.EMPTY)
        val itemModels = viewModel.envelopes().value.asItemModels()
        Text(
            text = category.value.name,
            fontSize = TextUnit(value = 24f, type = TextUnitType.Sp),
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(24.dp)
        )
        LazyColumn {
            items(itemModels) {
                CardItem(color = it.color) {
                    val scope = rememberCoroutineScope()
                    EnvelopeLabelView(
                        isChosen = it.data.isChosen,
                        envelope = it.data.envelope,
                        color = it.color
                    ) {
                        viewModel.setNewChosenEnvelope(it.data.envelope)
                        scope.launch {
                            delay(500)
                            navController.popBackStack(AppNavigation.start, false)
                        }
                    }
                }
            }
            item {
                CardItem(color = MaterialTheme.colors.secondary) {
                    IconButton(onClick = { navController.navigate(AppNavigation.addEnvelope()) }) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Add envelope")
                    }
                }
            }
        }
    }
}
