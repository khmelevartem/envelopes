package com.tubetoast.envelopes.android.presentation.ui.views

import androidx.compose.foundation.clickable
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun PeriodControl(viewModel: PeriodControlViewModel) {
    IconButton(
        onClick = { viewModel.previousPeriod() }
    ) {
        Icon(
            Icons.Default.KeyboardArrowLeft,
            contentDescription = "previousMonth"
        )
    }
    val periodState = viewModel.displayedPeriod.collectAsState("")
    val period by remember { periodState }
    Text(
        text = period,
        modifier = Modifier.clickable {
            viewModel.changePeriodType()
        }
    )
    IconButton(
        onClick = { viewModel.nextPeriod() }
    ) {
        Icon(Icons.Default.KeyboardArrowRight, contentDescription = "nextMonth")
    }
}
