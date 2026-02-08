package com.tubetoast.envelopes.ui.presentation.navigation

import androidx.compose.runtime.Composable
import com.tubetoast.envelopes.ui.presentation.ui.screens.EditEnvelopeScreen
import kotlinx.serialization.Serializable

object EnvelopeDetails : NavigationRoute<EnvelopeDetails.Args> {
    @Serializable
    data class Args(
        val envelopeId: Int? = null
    ) : NavigationRouteArgs

    @Composable
    override fun Route(
        args: Args,
        navigate: Navigate
    ) {
        EditEnvelopeScreen(
            navigate = navigate,
            envelopeId = args.envelopeId
        )
    }
}
