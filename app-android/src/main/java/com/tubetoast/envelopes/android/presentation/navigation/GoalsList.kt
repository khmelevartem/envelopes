package com.tubetoast.envelopes.android.presentation.navigation

import androidx.compose.runtime.Composable
import com.tubetoast.envelopes.android.presentation.ui.screens.GoalsListScreen
import kotlinx.serialization.Serializable

object GoalsList : NavigationRoute<GoalsList.Args> {
    @Serializable
    object Args : NavigationRouteArgs

    @Composable
    override fun Route(args: Args, navigate: Navigate) {
        GoalsListScreen(navigate = navigate)
    }
}
