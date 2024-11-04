package com.tubetoast.envelopes.android.presentation.ui.views

import androidx.compose.foundation.clickable
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.tubetoast.envelopes.android.presentation.ui.AppNavigation

@Composable
fun EnvelopesTopAppBar(
    viewModel: TopAppBarViewModel,
    navController: NavHostController
) {
    TopAppBar(
        backgroundColor = Color.Black,
        contentColor = Color.White,
        title = { Text("Envelopes") },
        actions = {
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
            IconButton(
                onClick = { navController.navigate(AppNavigation.settings) }
            ) {
                Icon(Icons.Default.Settings, contentDescription = "Import")
            }
        }
    )
}
