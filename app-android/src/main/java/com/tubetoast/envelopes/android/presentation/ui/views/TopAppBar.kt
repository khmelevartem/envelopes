package com.tubetoast.envelopes.android.presentation.ui.views

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import com.tubetoast.envelopes.android.presentation.navigation.AppNavigation
import com.tubetoast.envelopes.android.presentation.navigation.Back
import com.tubetoast.envelopes.android.presentation.navigation.Navigate

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
