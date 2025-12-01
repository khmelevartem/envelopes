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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer
import com.tubetoast.envelopes.android.presentation.navigation.Navigate
import com.tubetoast.envelopes.android.presentation.ui.theme.EColor
import com.tubetoast.envelopes.android.presentation.ui.views.CardItem
import com.tubetoast.envelopes.android.presentation.ui.views.SelectableLabelItem
import com.tubetoast.envelopes.android.presentation.utils.formatToReadableNumber
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun StatisticsScreen(
    navigate: Navigate,
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
            .border(1.dp, color = MaterialTheme.colorScheme.secondary, shape = RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            val yearlyData by viewModel.movingAverage.collectAsState()
            Plot(y = yearlyData)
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
    YearlyInflation(viewModel)
}

@Composable
private fun YearlyInflation(viewModel: InflationViewModel) {
    val yearlyData by viewModel.inflationByYearsData.collectAsState()
    yearlyData.ifEmpty { return }
    val years by viewModel.years.collectAsState()
    val yearlyAverage by viewModel.inflationAverageByYears.collectAsState()
    Plot(years, yearlyData, yearlyAverage)
}

@Composable
private fun Plot(x: List<Number>? = null, y: List<Number>, average: Int? = null) {
    val modelProducer = remember { CartesianChartModelProducer() }
    LaunchedEffect(y) {
        modelProducer.runTransaction {
            lineSeries {
                x?.let {
                    series(x, y)
                    average?.let {
                        series(x, x.map { average })
                    }
                } ?: series(y)
            }
        }
    }
    CartesianChartHost(
        chart = rememberCartesianChart(
            rememberLineCartesianLayer(
                lineProvider = LineCartesianLayer.LineProvider.series(
                    EColor.ePalette().map { color ->
                        LineCartesianLayer.rememberLine(
                            fill = LineCartesianLayer.LineFill.single(fill(color))
                        )
                    }
                )
            ),
            startAxis = VerticalAxis.rememberStart(),
            bottomAxis = HorizontalAxis.rememberBottom(guideline = null)
        ),
        modelProducer = modelProducer
    )
}

@Composable
private fun FilterByEnvelopes(viewModel: EnvelopesFilterViewModel) {
    val envelopes by viewModel.displayedEnvelopes.collectAsState()
    val itemModels = envelopes.asItemModels()
    LazyColumn(modifier = Modifier.padding(vertical = 16.dp)) {
        item {
            CardItem(color = MaterialTheme.colorScheme.secondary) {
                IconButton(onClick = { viewModel.toggleShowFilter() }) {
                    Icon(imageVector = Icons.Default.List, contentDescription = "Filter envelopes")
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
                    viewModel.toggleEnvelopesFilter(it.data.item)
                }
            }
        }
    }
}
