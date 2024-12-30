package com.tubetoast.envelopes.android.presentation.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
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
        modifier = Modifier.padding(16.dp)
    ) {
        Inflation(inflationViewModel)
        Average(averageViewModel)
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
    val yearlyData by viewModel.inflationByYearsData.collectAsState()
    val years by viewModel.years.collectAsState()
    val yearlyAverage by viewModel.inflationAverageByYears.collectAsState()
    InflationPlot(years, yearlyData, yearlyAverage)

    val monthlyData by viewModel.inflationByMonthsData.collectAsState()
    monthlyData.ifEmpty { return }
    val months by viewModel.months.collectAsState()
    val monthlyAverage by viewModel.inflationAverageByMonths.collectAsState()
    InflationPlot(months, monthlyData, monthlyAverage)
}

@Composable
private fun InflationPlot(x: List<Int>, y: List<Int>, average: Int) {
    val modelProducer = remember { CartesianChartModelProducer() }
    LaunchedEffect(y) {
        modelProducer.runTransaction {
            lineSeries {
                series(x, y)
                series(x, x.map { average })
            }
        }
    }
    CartesianChartHost(
        rememberCartesianChart(
            rememberLineCartesianLayer(),
            startAxis = VerticalAxis.rememberStart(guideline = null),
            bottomAxis = HorizontalAxis.rememberBottom(guideline = null)
        ),
        modelProducer
    )
}

@Composable
private fun FilterByEnvelopes(viewModel: EnvelopesFilterViewModel) {
    val envelopes by viewModel.displayedEnvelopes.collectAsState()
    val itemModels = envelopes.asItemModels()
    LazyColumn(modifier = Modifier.padding(vertical = 16.dp)) {
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
