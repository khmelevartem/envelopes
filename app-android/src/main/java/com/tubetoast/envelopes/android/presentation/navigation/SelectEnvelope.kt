package com.tubetoast.envelopes.android.presentation.navigation

import androidx.compose.runtime.Composable
import com.tubetoast.envelopes.android.presentation.ui.screens.SelectEnvelopeScreen
import kotlinx.serialization.Serializable

object SelectEnvelope : NavigationRoute<SelectEnvelope.Args> {
    @Serializable
    data class Args(
        val categoryId: Int
    ) : NavigationRouteArgs

    @Composable
    override fun Route(
        args: Args,
        navigate: Navigate
    ) {
        SelectEnvelopeScreen(
            navigate = navigate,
            categoryId = args.categoryId
        )
    }
}
