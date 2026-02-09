package com.tubetoast.envelopes.ui.presentation.ui.views

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tubetoast.envelopes.ui.presentation.ui.screens.asItemModels

@Composable
fun SelectableCategoriesList(viewModel: SelectableCategoriesListViewModel) {
    val categories by viewModel.displayedCategories.collectAsState()
    val isColorful by viewModel.isColorful.collectAsState()
    val itemModels = categories.asItemModels(isColorful.checked)
    LazyColumn(modifier = Modifier.padding(vertical = 16.dp)) {
        item {
            CardItem(color = MaterialTheme.colorScheme.secondary) {
                IconButton(onClick = { viewModel.toggleShowFilter() }) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.List, contentDescription = "Filter envelopes")
                }
            }
        }
        items(itemModels) {
            CardItem(color = it.color) {
                SelectableLabelItem(
                    isChosen = it.data.isSelected,
                    label = it.data.item.name,
                    color = it.color
                ) {
                    viewModel.chooseCategory(it.data.item)
                }
            }
        }
    }
}
