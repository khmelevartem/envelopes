package com.tubetoast.envelopes.ui.presentation.navigation

import androidx.compose.runtime.Composable
import com.tubetoast.envelopes.ui.presentation.ui.screens.StatisticsScreen
import kotlinx.serialization.Serializable

object Statistics : NavigationRoute<Statistics.Args> {
    @Serializable
    object Args : NavigationRouteArgs

    @Composable
    override fun Route(
        args: Args,
        navigate: Navigate
    ) {
        StatisticsScreen(navigate = navigate)
    }
}
