package com.tubetoast.envelopes.ui.presentation.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.ui.presentation.navigation.AppNavigation
import com.tubetoast.envelopes.ui.presentation.navigation.BackTo
import com.tubetoast.envelopes.ui.presentation.navigation.Navigate
import com.tubetoast.envelopes.ui.presentation.ui.views.CardItem
import com.tubetoast.envelopes.ui.presentation.ui.views.SelectableLabelItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SelectEnvelopeScreen(
    navigate: Navigate,
    viewModel: SelectEnvelopeViewModel = koinViewModel(),
    categoryId: Int? = null
) {
    Column {
        val category = viewModel.category(categoryId).collectAsState(initial = Category.EMPTY)
        val isColorful by viewModel.isColorful.collectAsState()
        val itemModels = viewModel.envelopes().value.asItemModels(isColorful.checked)
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
                    SelectableLabelItem(
                        isChosen = it.data.isSelected,
                        label = it.data.item.name,
                        color = it.color
                    ) {
                        viewModel.setNewChosenEnvelope(it.data.item)
                        scope.launch {
                            delay(500)
                            navigate(BackTo(AppNavigation.start))
                        }
                    }
                }
            }
            item {
                CardItem(color = MaterialTheme.colorScheme.secondary) {
                    IconButton(onClick = { navigate(AppNavigation.addEnvelope()) }) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Add envelope")
                    }
                }
            }
        }
    }
}
