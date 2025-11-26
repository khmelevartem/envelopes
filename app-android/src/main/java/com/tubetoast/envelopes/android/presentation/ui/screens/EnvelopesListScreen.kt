package com.tubetoast.envelopes.android.presentation.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tubetoast.envelopes.android.presentation.ui.AppNavigation
import com.tubetoast.envelopes.android.presentation.ui.views.CardItem
import com.tubetoast.envelopes.android.presentation.ui.views.EnvelopeView
import com.tubetoast.envelopes.android.presentation.ui.views.PeriodControl
import com.tubetoast.envelopes.android.presentation.ui.views.PeriodControlViewModel
import com.tubetoast.envelopes.android.presentation.ui.views.SettingsButton
import com.tubetoast.envelopes.android.presentation.utils.formatToReadableNumber
import com.tubetoast.envelopes.common.domain.models.sum
import com.tubetoast.envelopes.common.domain.snapshots.EnvelopeSnapshot
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EnvelopesListScreen(
    navController: NavController,
    envelopesListViewModel: EnvelopesListViewModel = koinViewModel(),
    periodControlViewModel: PeriodControlViewModel = koinViewModel()
) {
    val envelopes by envelopesListViewModel.itemModels.collectAsState(initial = emptyList())
    val itemModels = envelopes.asItemModels()
    val listState = rememberLazyListState()
    val filterByYear by envelopesListViewModel.filterByYear.collectAsState()
    Column {
        EnvelopesListTopAppBar(periodControlViewModel, navController)
        TotalView(navController, envelopes, filterByYear)
        ListOfEnvelopes(listState, itemModels, envelopesListViewModel, navController, filterByYear)
    }
}

@Composable
private fun EnvelopesListTopAppBar(
    periodControlViewModel: PeriodControlViewModel,
    navController: NavController
) {
    TopAppBar(
        backgroundColor = Color.Black,
        contentColor = Color.White,
        title = {
            Text(
                text = "Envelopes",
                modifier = Modifier.clickable {
                    navController.navigate(AppNavigation.goalsList)
                }
            )
        },
        actions = {
            PeriodControl(periodControlViewModel)
            SettingsButton(navController)
        }
    )
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun ListOfEnvelopes(
    listState: LazyListState,
    itemModels: List<ItemModel<EnvelopeSnapshot>>,
    envelopesListViewModel: EnvelopesListViewModel,
    navController: NavController,
    filterByYear: Boolean
) {
    LazyColumn(
        verticalArrangement = Arrangement.SpaceAround,
        state = listState,
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        items(itemModels, key = { item -> item.data.envelope.id.code }) { itemModel ->
            EnvelopeView(
                itemModel,
                filterByYear,
                onEditClick = { navController.navigate(AppNavigation.editEnvelope(it)) },
                onDeleteClick = { envelopesListViewModel.delete(it) },
                onAddClick = { navController.navigate(AppNavigation.addCategory(it)) },
                onCategoryClick = { category, envelope ->
                    navController.navigate(
                        AppNavigation.editCategory(category, envelope)
                    )
                },
                modifier = Modifier
                    .animateItemPlacement()
            )
        }
        item {
            CardItem(
                color = MaterialTheme.colors.secondary,
                modifier = Modifier.height(64.dp)
            ) {
                IconButton(onClick = { navController.navigate(AppNavigation.addEnvelope()) }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        tint = MaterialTheme.colors.onSecondary,
                        contentDescription = "Add envelope"
                    )
                }
            }
        }
    }
}

@Composable
fun TotalView(
    navController: NavController,
    envelopes: Iterable<EnvelopeSnapshot>,
    filterByYear: Boolean,
    modifier: Modifier = Modifier
) {
    val sum = envelopes.map { it.sum }.sum()
    val limit = envelopes.map { it.envelope.run { if (filterByYear) yearLimit else limit } }.sum()
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Black)
    ) {
        Text(
            text = "${sum.units.formatToReadableNumber()} / ${limit.units.formatToReadableNumber()}",
            color = Color.LightGray,
            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
        )
        Text(
            text = "${(sum / limit * 100).toInt()} %",
            color = Color.LightGray,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        IconButton(
            onClick = { navController.navigate(AppNavigation.statistics) },
            modifier = Modifier.padding(end = 16.dp, bottom = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                tint = Color.LightGray,
                contentDescription = "statistics"
            )
        }
    }
}
