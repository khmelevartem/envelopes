package com.tubetoast.envelopes.android.presentation.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.tubetoast.envelopes.android.presentation.ui.views.CardItem
import com.tubetoast.envelopes.android.presentation.ui.views.EnvelopeLabelView
import com.tubetoast.envelopes.android.presentation.utils.formatToReadableNumber
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun StatisticsScreen(
    navController: NavHostController,
    viewModel: StatisticsScreenViewModel = koinViewModel()
) {
    Column {
        Average(viewModel)

        val envelopes by viewModel.envelopesFilter.collectAsState()
        val itemModels = envelopes.asItemModels()
        LazyColumn(modifier = Modifier.padding(top = 16.dp)) {
            item {
                CardItem(color = MaterialTheme.colors.secondary) {
                    IconButton(onClick = { viewModel.toggleShowFilter() }) {
                        Icon(imageVector = Icons.Default.List, contentDescription = "Filter envelopes")
                    }
                }
            }
            items(itemModels) {
                CardItem(color = it.color) {
                    EnvelopeLabelView(
                        isChosen = it.data.isChosen,
                        envelope = it.data.envelope,
                        color = it.color
                    ) {
                        viewModel.toggleEnvelopesFilter(it.data.envelope)
                    }
                }
            }
        }
    }
}

@Composable
fun Average(viewModel: StatisticsScreenViewModel) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        val period by viewModel.displayedPeriod.collectAsState()
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = { viewModel.minusPeriod() }
            ) {
                Icon(
                    Icons.Default.KeyboardArrowDown,
                    contentDescription = "minus month"
                )
            }
            Text(
                text = "Average for $period",
                modifier = Modifier.clickable {
                    viewModel.changePeriodType()
                }
            )
            IconButton(
                onClick = { viewModel.plusPeriod() }
            ) {
                Icon(Icons.Default.KeyboardArrowUp, contentDescription = "plus month")
            }
        }
        val averageInMonth by viewModel.displayedAverageInMonth.collectAsState()
        Text("${averageInMonth.formatToReadableNumber()} / month")

        val averageInYear by viewModel.displayedAverageInYear.collectAsState()
        Text("${averageInYear.formatToReadableNumber()} / year")
    }
}
