package com.tubetoast.envelopes.ui.presentation.ui.views

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import com.tubetoast.envelopes.ui.presentation.navigation.AppNavigation
import com.tubetoast.envelopes.ui.presentation.navigation.Back
import com.tubetoast.envelopes.ui.presentation.navigation.Navigate

@Composable
fun BackButton(navigate: Navigate) {
    IconButton(onClick = { navigate(Back) }) {
        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
    }
}

@Composable
fun SettingsButton(navigate: Navigate) {
    IconButton(onClick = { navigate(AppNavigation.settings()) }) {
        Icon(Icons.Default.Settings, contentDescription = "Settings")
    }
}
