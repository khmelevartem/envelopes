package com.tubetoast.envelopes.android.presentation.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tubetoast.envelopes.android.presentation.ui.views.BackButton
import com.tubetoast.envelopes.android.presentation.ui.views.CheckboxSettingItem
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = koinViewModel()
) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxHeight()
    ) {
        SettingsTopAppBar(navController)
        val items = viewModel.items
        LazyColumn(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp).weight(1f)
        ) {
            items(items) { item ->
                val itemState = item.checked.collectAsState()
                val checked by remember { itemState }
                CheckboxSettingItem(
                    text = item.text,
                    default = checked
                ) {
                    viewModel.toggle(item, it)
                }
            }
        }
        Button(
            modifier = Modifier.fillMaxWidth().height(48.dp),
            onClick = { navController.popBackStack() }
        ) {
            Text(text = "OK")
        }
    }
}

@Composable
private fun SettingsTopAppBar(
    navController: NavController
) {
    TopAppBar(
        backgroundColor = Color.Black,
        contentColor = Color.White,
        title = { Text(text = "Settings") },
        navigationIcon = { BackButton(navController) }
    )
}
