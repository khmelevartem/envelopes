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
import com.tubetoast.envelopes.android.presentation.ui.screens.EditCategoryScreen
import com.tubetoast.envelopes.android.presentation.ui.screens.EditCategoryViewModel
import com.tubetoast.envelopes.android.presentation.ui.screens.EditEnvelopeScreen
import com.tubetoast.envelopes.android.presentation.ui.screens.EditEnvelopeViewModel
import com.tubetoast.envelopes.android.presentation.ui.screens.EnvelopesListScreen
import com.tubetoast.envelopes.android.presentation.ui.screens.EnvelopesListViewModel
import com.tubetoast.envelopes.common.domain.models.Envelope

@Composable
fun EnvelopesApp(
    envelopesListViewModel: EnvelopesListViewModel,
    editEnvelopeViewModel: EditEnvelopeViewModel,
    editCategoryViewModel: EditCategoryViewModel,
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    NavHost(navController = navController, startDestination = AppNavigation.start) {
        composable(route = AppNavigation.envelopesList) {
            EnvelopesListScreen(navController, envelopesListViewModel)
        }
        composable(route = AppNavigation.addEnvelope) {
            EditEnvelopeScreen(navController, editEnvelopeViewModel)
        }
        composable(
            route = AppNavigation.editEnvelope,
            arguments = listOf(
                navArgument(AppNavigation.argEnvelopeId) { type = NavType.IntType },
            ),
        ) {
            navBackStackEntry?.arguments?.takeInt(AppNavigation.argEnvelopeId)?.also {
                EditEnvelopeScreen(navController, editEnvelopeViewModel, it)
            }
        }
        composable(
            route = AppNavigation.addCategory,
            arguments = listOf(
                navArgument(AppNavigation.argEnvelopeId) { type = NavType.IntType },
            ),
        ) {
            navBackStackEntry?.arguments?.takeInt(AppNavigation.argEnvelopeId)?.also {
                editCategoryViewModel.setEnvelopeId(it)
            }
            EditCategoryScreen(navController, editCategoryViewModel)
        }
        composable(
            route = AppNavigation.editCategory,
            arguments = listOf(
                navArgument(AppNavigation.argCategoryId) { type = NavType.IntType },
            ),
        ) {
            navBackStackEntry?.arguments?.takeInt(AppNavigation.argCategoryId)?.also {
                editCategoryViewModel.setEditedCategoryId(it)
            }
            EditCategoryScreen(navController, editCategoryViewModel)
        }
    }
}

private const val NO_VALUE = -1

private fun Bundle.takeInt(key: String): Int? {
    return getInt(key, NO_VALUE).takeUnless { it == NO_VALUE }
}

object AppNavigation {
    const val envelopesList = "envelopesList"
    const val addEnvelope = "addEnvelope"
    const val argEnvelopeId = "envelopeId"
    const val argCategoryId = "categoryId"
    const val editEnvelope = "editEnvelope/{$argEnvelopeId}"
    const val addCategory = "addCategory/{$argEnvelopeId}"
    const val editCategory = "addCategory/{$argCategoryId}"

    fun editEnvelope(envelope: Envelope) =
        editEnvelope.replace("{$argEnvelopeId}", "${envelope.id.code}")

    fun addCategory(envelope: Envelope) =
        addCategory.replace("{$argEnvelopeId}", "${envelope.id.code}")

    const val start = envelopesList
}
