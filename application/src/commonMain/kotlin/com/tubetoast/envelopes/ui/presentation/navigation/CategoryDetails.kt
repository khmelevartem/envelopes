package com.tubetoast.envelopes.ui.presentation.navigation

import androidx.compose.runtime.Composable
import com.tubetoast.envelopes.ui.presentation.ui.screens.EditCategoryScreen
import kotlinx.serialization.Serializable

object CategoryDetails : NavigationRoute<CategoryDetails.Args> {
    @Serializable
    data class Args(
        val envelopeId: Int,
        val categoryId: Int? = null
    ) : NavigationRouteArgs

    @Composable
    override fun Route(
        args: Args,
        navigate: Navigate
    ) {
        EditCategoryScreen(
            navigate = navigate,
            categoryId = args.categoryId,
            envelopeId = args.envelopeId
        )
    }
}
