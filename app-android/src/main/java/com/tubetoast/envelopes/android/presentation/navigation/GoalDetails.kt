package com.tubetoast.envelopes.android.presentation.navigation

import androidx.compose.runtime.Composable
import com.tubetoast.envelopes.android.presentation.ui.screens.EditGoalScreen
import kotlinx.serialization.Serializable

object GoalDetails : NavigationRoute<GoalDetails.Args> {
    @Serializable
    data class Args(
        val goalId: Int? = null
    ) : NavigationRouteArgs

    @Composable
    override fun Route(args: Args, navigate: Navigate) {
        EditGoalScreen(
            navigate = navigate,
            goalId = args.goalId
        )
    }
}
