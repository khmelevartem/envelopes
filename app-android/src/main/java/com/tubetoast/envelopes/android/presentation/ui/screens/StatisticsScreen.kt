package com.tubetoast.envelopes.android.presentation.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.tubetoast.envelopes.android.presentation.utils.formatToReadableNumber
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun StatisticsScreen(
    navController: NavHostController,
    viewModel: StatisticsScreenViewModel = koinViewModel()
) {
    Average(viewModel)
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
