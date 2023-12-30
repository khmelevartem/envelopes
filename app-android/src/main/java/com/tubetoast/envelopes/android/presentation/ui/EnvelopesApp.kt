package com.tubetoast.envelopes.android.presentation.ui

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tubetoast.envelopes.android.presentation.ui.screens.ChooseEnvelopeScreen
import com.tubetoast.envelopes.android.presentation.ui.screens.ChooseEnvelopeViewModel
import com.tubetoast.envelopes.android.presentation.ui.screens.EditCategoryScreen
import com.tubetoast.envelopes.android.presentation.ui.screens.EditCategoryViewModel
import com.tubetoast.envelopes.android.presentation.ui.screens.EditEnvelopeScreen
import com.tubetoast.envelopes.android.presentation.ui.screens.EditEnvelopeViewModel
import com.tubetoast.envelopes.android.presentation.ui.screens.EnvelopesListScreen
import com.tubetoast.envelopes.android.presentation.ui.screens.EnvelopesListViewModel
import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.ImmutableModel

@Composable
fun EnvelopesApp(
    envelopesListViewModel: EnvelopesListViewModel,
    editEnvelopeViewModel: EditEnvelopeViewModel,
    editCategoryViewModel: EditCategoryViewModel,
    chooseEnvelopeViewModel: ChooseEnvelopeViewModel,
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    NavHost(navController = navController, startDestination = AppNavigation.start) {
        composable(route = AppNavigation.envelopesList) {
            EnvelopesListScreen(navController, envelopesListViewModel)
        }
        composable(
            route = AppNavigation.envelopeScreen,
            arguments = listOf(
                navArgument(AppNavigation.argEnvelopeId) { type = NavType.IntType },
            ),
        ) {
            val envelopeId = navBackStackEntry?.arguments?.takeInt(AppNavigation.argEnvelopeId)
            EditEnvelopeScreen(navController, editEnvelopeViewModel, envelopeId)
        }
        composable(
            route = AppNavigation.categoryScreen,
            arguments = listOf(
                navArgument(AppNavigation.argCategoryId) { type = NavType.IntType },
                navArgument(AppNavigation.argEnvelopeId) { type = NavType.IntType },
            ),
        ) {
            navBackStackEntry?.arguments?.run {
                val envelopeId = takeInt(AppNavigation.argEnvelopeId)
                val categoryId = takeInt(AppNavigation.argCategoryId)
                EditCategoryScreen(
                    navController = navController,
                    viewModel = editCategoryViewModel,
                    categoryId = categoryId,
                    envelopeId = envelopeId
                )
            }
        }
        composable(
            route = AppNavigation.chooseEnvelope,
            arguments = listOf(
                navArgument(AppNavigation.argCategoryId) { type = NavType.IntType },
                navArgument(AppNavigation.argEnvelopeId) { type = NavType.IntType },
            ),
        ) {
            navBackStackEntry?.arguments?.run {
                val envelopeId = takeInt(AppNavigation.argEnvelopeId)
                val categoryId = takeInt(AppNavigation.argCategoryId)
                ChooseEnvelopeScreen(
                    navController = navController,
                    viewModel = chooseEnvelopeViewModel,
                    categoryId = categoryId,
                    envelopeId = envelopeId
                )
            }
        }
    }
}

private const val NO_VALUE = -1

private fun Bundle.takeInt(key: String): Int? {
    return getInt(key, NO_VALUE).takeUnless { it == NO_VALUE }
}

object AppNavigation {
    const val envelopesList = "envelopesListScreen"
    const val argEnvelopeId = "envelopeId"
    const val argCategoryId = "categoryId"
    const val envelopeScreen = "envelopeScreen/{$argEnvelopeId}"
    const val categoryScreen = "categoryScreen/{$argCategoryId}/{$argEnvelopeId}"
    const val chooseEnvelope = "chooseEnvelopeScreen/{$argCategoryId}/{$argEnvelopeId}"

    fun addEnvelope() = envelopeScreen.putArg(argEnvelopeId, null)

    fun editEnvelope(envelope: Envelope) = envelopeScreen.putArg(argEnvelopeId, envelope)

    fun addCategory(envelope: Envelope) =
        categoryScreen.putArg(argCategoryId, null).putArg(argEnvelopeId, envelope)

    fun editCategory(category: Category, envelope: Envelope) =
        categoryScreen.putArg(argCategoryId, category).putArg(argEnvelopeId, envelope)

    private fun String.putArg(argName: String, argValue: ImmutableModel<*>?) =
        this.replace("{$argName}", "${argValue?.id?.code ?: NO_VALUE}")

    fun chooseEnvelope(category: Category, envelope: Envelope) =
        chooseEnvelope.putArg(argCategoryId, category).putArg(argEnvelopeId, envelope)

    const val start = envelopesList
}
