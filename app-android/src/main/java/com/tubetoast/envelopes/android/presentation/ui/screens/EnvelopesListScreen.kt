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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tubetoast.envelopes.android.presentation.navigation.AppNavigation
import com.tubetoast.envelopes.android.presentation.navigation.Navigate
import com.tubetoast.envelopes.android.presentation.navigation.NavigationRouteArgs
import com.tubetoast.envelopes.android.presentation.ui.theme.EnvelopesTheme
import com.tubetoast.envelopes.android.presentation.ui.theme.topAppBarColors
import com.tubetoast.envelopes.android.presentation.ui.views.CardItem
import com.tubetoast.envelopes.android.presentation.ui.views.EnvelopeView
import com.tubetoast.envelopes.android.presentation.ui.views.PeriodControl
import com.tubetoast.envelopes.android.presentation.ui.views.PeriodControlViewModel
import com.tubetoast.envelopes.android.presentation.ui.views.SettingsButton
import com.tubetoast.envelopes.common.domain.models.sum
import com.tubetoast.envelopes.common.domain.snapshots.EnvelopeSnapshot
import com.tubetoast.envelopes.common.utils.formatToReadableNumber
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EnvelopesListScreen(
    navigate: Navigate,
    envelopesListViewModel: EnvelopesListViewModel = koinViewModel(),
    periodControlViewModel: PeriodControlViewModel = koinViewModel()
) {
    val envelopes by envelopesListViewModel.itemModels.collectAsState(initial = emptyList())
    val isColorful by envelopesListViewModel.isColorful.collectAsState()
    val itemModels = envelopes.asItemModels(isColorful.checked)
    val listState = rememberLazyListState()
    val filterByYear by envelopesListViewModel.filterByYear.collectAsState()
    Column {
        EnvelopesListTopAppBar(periodControlViewModel, navigate)
        TotalView(navigate, envelopes, filterByYear)
        ListOfEnvelopes(
            listState = listState,
            itemModels = itemModels,
            envelopesListViewModel = envelopesListViewModel,
            navigate = navigate,
            filterByYear = filterByYear
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EnvelopesListTopAppBar(
    periodControlViewModel: PeriodControlViewModel,
    navigate: Navigate
) {
    TopAppBar(
        colors = EnvelopesTheme.topAppBarColors(),
        title = {
            Text(
                text = "Envelopes",
                modifier = Modifier.clickable {
                    navigate(AppNavigation.goalsList())
                }
            )
        },
        actions = {
            PeriodControl(periodControlViewModel)
            SettingsButton(navigate)
        }
    )
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun ListOfEnvelopes(
    listState: LazyListState,
    itemModels: List<ItemModel<EnvelopeSnapshot>>,
    envelopesListViewModel: EnvelopesListViewModel,
    navigate: (NavigationRouteArgs) -> Unit,
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
                onEditClick = { navigate(AppNavigation.editEnvelope(it)) },
                onDeleteClick = { envelopesListViewModel.delete(it) },
                onAddClick = { navigate(AppNavigation.addCategory(it)) },
                onCategoryClick = { category, envelope ->
                    navigate(AppNavigation.editCategory(category, envelope))
                },
                modifier = Modifier.animateItem()
            )
        }
        item {
            CardItem(
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.height(64.dp)
            ) {
                IconButton(onClick = { navigate(AppNavigation.addEnvelope()) }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        tint = MaterialTheme.colorScheme.onSecondary,
                        contentDescription = "Add envelope"
                    )
                }
            }
        }
    }
}

@Composable
fun TotalView(
    navigate: Navigate,
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
            onClick = { navigate(AppNavigation.statistics()) },
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
