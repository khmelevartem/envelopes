package com.tubetoast.envelopes.ui.presentation.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tubetoast.envelopes.ui.presentation.navigation.Back
import com.tubetoast.envelopes.ui.presentation.navigation.Navigate
import com.tubetoast.envelopes.ui.presentation.ui.theme.EnvelopesTheme
import com.tubetoast.envelopes.ui.presentation.ui.theme.topAppBarColors
import com.tubetoast.envelopes.ui.presentation.ui.views.BackButton
import com.tubetoast.envelopes.ui.presentation.ui.views.CheckboxSettingItem
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SettingsScreen(
    navigate: Navigate,
    viewModel: SettingsViewModel = koinViewModel()
) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxHeight()
    ) {
        SettingsTopAppBar(navigate)
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
            onClick = { navigate(Back) }
        ) {
            Text(text = "OK")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsTopAppBar(navigate: Navigate) {
    TopAppBar(
        colors = EnvelopesTheme.topAppBarColors(),
        title = { Text(text = "Settings") },
        navigationIcon = { BackButton(navigate) }
    )
}
