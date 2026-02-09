package com.tubetoast.envelopes.ui.presentation.navigation

import androidx.compose.runtime.Composable
import kotlinx.serialization.Serializable

sealed interface NavigationRoute<Args : NavigationRouteArgs> {
    @Composable
    fun Route(
        args: Args,
        navigate: Navigate
    )
}

interface NavigationRouteArgs

@Serializable
object Back : NavigationRouteArgs

data class BackTo(
    val to: NavigationRouteArgs,
    val inclusive: Boolean = false
) : NavigationRouteArgs

typealias Navigate = (NavigationRouteArgs) -> Unit
