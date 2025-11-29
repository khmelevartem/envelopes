package com.tubetoast.envelopes.android.presentation.ui.views

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.tubetoast.envelopes.android.presentation.ui.AppNavigation

@Composable
fun BackButton(navController: NavController) {
    IconButton(onClick = { navController.popBackStack() }) {
        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
    }
}

@Composable
fun SettingsButton(navController: NavController) {
    IconButton(
        onClick = { navController.navigate(AppNavigation.settings()) }
    ) {
        Icon(Icons.Default.Settings, contentDescription = "Settings")
    }
}
