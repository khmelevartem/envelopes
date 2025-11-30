package com.tubetoast.envelopes.android.presentation.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.tubetoast.envelopes.android.presentation.navigation.AppNavigation
import com.tubetoast.envelopes.android.presentation.navigation.Back
import com.tubetoast.envelopes.android.presentation.navigation.BackTo
import com.tubetoast.envelopes.android.presentation.navigation.CategoryDetails
import com.tubetoast.envelopes.android.presentation.navigation.ChooseEnvelope
import com.tubetoast.envelopes.android.presentation.navigation.EnvelopeDetails
import com.tubetoast.envelopes.android.presentation.navigation.EnvelopesList
import com.tubetoast.envelopes.android.presentation.navigation.GoalDetails
import com.tubetoast.envelopes.android.presentation.navigation.GoalsList
import com.tubetoast.envelopes.android.presentation.navigation.NavigationRoute
import com.tubetoast.envelopes.android.presentation.navigation.NavigationRouteArgs
import com.tubetoast.envelopes.android.presentation.navigation.Settings
import com.tubetoast.envelopes.android.presentation.navigation.Statistics

@Composable
fun EnvelopesApp() {
    MaterialTheme {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = AppNavigation.start) {
            with(this to navController) {
                register(EnvelopeDetails)
                register(CategoryDetails)
                register(ChooseEnvelope)
                register(EnvelopesList)
                register(Settings)
                register(Statistics)
                register(GoalsList)
                register(GoalDetails)
            }
        }
    }
}

private inline infix fun <reified A : NavigationRouteArgs> Pair<NavGraphBuilder, NavController>.register(
    route: NavigationRoute<A>
) {
    first.composable<A> {
        route.Route(it.toRoute<A>()) { next ->
            second.next(next)
        }
    }
}

private fun NavController.next(next: NavigationRouteArgs) {
    when (next) {
        Back -> popBackStack()
        is BackTo -> popBackStack(next.to, inclusive = next.inclusive)
        else -> navigate(next)
    }
}
