package com.tubetoast.envelopes.android.presentation.navigation

import androidx.compose.runtime.Composable
import com.tubetoast.envelopes.android.presentation.ui.screens.ChooseEnvelopeScreen
import kotlinx.serialization.Serializable

object ChooseEnvelope : NavigationRoute<ChooseEnvelope.Args> {
    @Serializable
    data class Args(
        val categoryId: Int
    ) : NavigationRouteArgs

    @Composable
    override fun Route(
        args: Args,
        navigate: Navigate
    ) {
        ChooseEnvelopeScreen(
            navigate = navigate,
            categoryId = args.categoryId
        )
    }
}
