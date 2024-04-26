package com.tubetoast.envelopes.android.presentation.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.tubetoast.envelopes.android.presentation.ui.theme.EnvelopesTheme
import com.tubetoast.envelopes.android.presentation.ui.views.CheckboxSettingItem

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
) {
    val items by remember { viewModel.items }
    EnvelopesTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            LazyColumn {
                items(items) { item ->
                    val state by remember { item.checked }
                    CheckboxSettingItem(
                        text = item.text,
                        default = state
                    ) {
                        viewModel.toggle(item, it)
                    }
                }
            }
        }
    }
}