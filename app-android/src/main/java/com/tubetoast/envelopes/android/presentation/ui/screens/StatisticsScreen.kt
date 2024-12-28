package com.tubetoast.envelopes.android.presentation.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
    inflationViewModel: InflationViewModel = koinViewModel(),
    averageViewModel: AverageViewViewModel = koinViewModel(),
    filterViewModel: EnvelopesFilterViewModel = koinViewModel()
) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.padding(16.dp)
    ) {
        Average(averageViewModel)
        Inflation(inflationViewModel)
        FilterByEnvelopes(filterViewModel)
    }
}

@Composable
fun Average(viewModel: AverageViewViewModel) {
    Box(
        Modifier
            .border(1.dp, color = MaterialTheme.colors.secondary, shape = RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
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
}

@Composable
fun Inflation(viewModel: InflationViewModel) {
    Box(
        Modifier
            .border(1.dp, color = MaterialTheme.colors.secondary, shape = RoundedCornerShape(16.dp))
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Column {
            val yy by viewModel.yearlyInflation.collectAsState()
            Text(
                text = "Inflation y/y $yy%"
            )

            val mm by viewModel.monthlyInflation.collectAsState()
            Text(
                text = "Inflation m/m $mm%"
            )
        }
    }
}

@Composable
private fun FilterByEnvelopes(viewModel: EnvelopesFilterViewModel) {
    val envelopes by viewModel.displayedEnvelopes.collectAsState()
    val itemModels = envelopes.asItemModels()
    LazyColumn(modifier = Modifier.padding(vertical = 16.dp)) {
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
        item {
            CardItem(color = MaterialTheme.colors.secondary) {
                IconButton(onClick = { viewModel.toggleShowFilter() }) {
                    Icon(imageVector = Icons.Default.List, contentDescription = "Filter envelopes")
                }
            }
        }
    }
}
