package com.tubetoast.envelopes.android.presentation.navigation

import androidx.compose.runtime.Composable
import com.tubetoast.envelopes.android.presentation.ui.screens.EnvelopesListScreen
import kotlinx.serialization.Serializable

object EnvelopesList : NavigationRoute<EnvelopesList.Args> {

    @Serializable
    object Args : NavigationRouteArgs

    @Composable
    override fun Route(args: Args, navigate: Navigate) {
        EnvelopesListScreen(
            navigate = navigate
        )
    }
}
