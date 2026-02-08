package com.tubetoast.envelopes.ui.presentation.navigation

import androidx.compose.runtime.Composable
import com.tubetoast.envelopes.ui.presentation.ui.screens.SettingsScreen
import kotlinx.serialization.Serializable

object Settings : NavigationRoute<Settings.Args> {
    @Serializable
    object Args : NavigationRouteArgs

    @Composable
    override fun Route(
        args: Args,
        navigate: Navigate
    ) {
        SettingsScreen(navigate = navigate)
    }
}
