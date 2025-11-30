package com.tubetoast.envelopes.android.presentation.navigation

import androidx.compose.runtime.Composable

sealed interface NavigationRoute<Args : NavigationRouteArgs> {
    @Composable
    fun Route(args: Args, navigate: Navigate)
}

interface NavigationRouteArgs

object Back : NavigationRouteArgs

data class BackTo(
    val to: NavigationRouteArgs,
    val inclusive: Boolean = false
) : NavigationRouteArgs

typealias Navigate = (NavigationRouteArgs) -> Unit
