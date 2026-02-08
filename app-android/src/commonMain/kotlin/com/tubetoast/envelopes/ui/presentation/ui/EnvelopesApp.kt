package com.tubetoast.envelopes.ui.presentation.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.tubetoast.envelopes.ui.presentation.navigation.AppNavigation
import com.tubetoast.envelopes.ui.presentation.navigation.Back
import com.tubetoast.envelopes.ui.presentation.navigation.BackTo
import com.tubetoast.envelopes.ui.presentation.navigation.CategoryDetails
import com.tubetoast.envelopes.ui.presentation.navigation.EnvelopeDetails
import com.tubetoast.envelopes.ui.presentation.navigation.EnvelopesList
import com.tubetoast.envelopes.ui.presentation.navigation.GoalDetails
import com.tubetoast.envelopes.ui.presentation.navigation.GoalsList
import com.tubetoast.envelopes.ui.presentation.navigation.NavigationRoute
import com.tubetoast.envelopes.ui.presentation.navigation.NavigationRouteArgs
import com.tubetoast.envelopes.ui.presentation.navigation.SelectEnvelope
import com.tubetoast.envelopes.ui.presentation.navigation.Settings

@Composable
fun EnvelopesApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AppNavigation.start) {
        with(this to navController) {
            register(EnvelopeDetails)
            register(CategoryDetails)
            register(SelectEnvelope)
            register(EnvelopesList)
            register(Settings)
            register(GoalsList)
            register(GoalDetails)
        }
    }
}

private inline infix fun <reified A : NavigationRouteArgs> Pair<NavGraphBuilder, NavController>.register(route: NavigationRoute<A>) {
    first.composable<A> {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.surfaceDim
        ) {
            route.Route(it.toRoute<A>()) { next ->
                second.next(next)
            }
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
