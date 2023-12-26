package com.tubetoast.envelopes.android.presentation.ui

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tubetoast.envelopes.android.presentation.ui.screens.*
import com.tubetoast.envelopes.common.data.CategoriesRepositoryImpl
import com.tubetoast.envelopes.common.data.EnvelopesRepositoryImpl
import com.tubetoast.envelopes.common.data.SpendingRepositoryImpl
import com.tubetoast.envelopes.common.domain.CategoryInteractorImpl
import com.tubetoast.envelopes.common.domain.EnvelopeInteractorImpl
import com.tubetoast.envelopes.common.domain.SnapshotsInteractorImpl
import com.tubetoast.envelopes.common.domain.models.Envelope

private const val NO_VALUE = -1

@Composable
fun EnvelopesApp(
    envelopesListViewModel: EnvelopesListViewModel,
    editEnvelopeViewModel: EditEnvelopeViewModel,
    editCategoryViewModel: EditCategoryViewModel,
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AppNavigation.start) {
        composable(route = AppNavigation.envelopesList) {
            EnvelopesListScreen(navController, envelopesListViewModel)
        }
        composable(route = AppNavigation.addEnvelope) {
            EditEnvelopeScreen(
                navController,
                editEnvelopeViewModel,
            )
        }
        composable(
            route = AppNavigation.editEnvelope,
            arguments = listOf(
                navArgument(AppNavigation.argEnvelopeHash) { type = NavType.IntType },
            ),
        ) { navBackStackEntry ->
            EditEnvelopeScreen(
                navController,
                editEnvelopeViewModel.also { vm ->
                    navBackStackEntry.arguments?.takeIntAndRemove(AppNavigation.argEnvelopeHash) {
                        vm.setEditedEnvelopeHash(it)
                    }
                },
            )
        }
        composable(
            route = AppNavigation.addCategory,
            arguments = listOf(
                navArgument(AppNavigation.argEnvelopeHash) { type = NavType.IntType },
            ),
        ) { navBackStackEntry ->
            EditCategoryScreen(
                navController,
                editCategoryViewModel.also { vm ->
                    navBackStackEntry.arguments?.takeIntAndRemove(AppNavigation.argEnvelopeHash) {
                        vm.setEnvelopeHash(it)
                    }
                },
            )
        }
        composable(
            route = AppNavigation.editCategory,
            arguments = listOf(
                navArgument(AppNavigation.argCategoryHash) { type = NavType.IntType },
            ),
        ) { navBackStackEntry ->
            EditCategoryScreen(
                navController,
                editCategoryViewModel.also { vm ->
                    navBackStackEntry.arguments?.takeIntAndRemove(AppNavigation.argCategoryHash) {
                        vm.setEditedCategoryHash(it)
                    }
                },
            )
        }
    }
}

private fun Bundle.takeIntAndRemove(key: String, action: (Int) -> Unit) {
    getInt(key, NO_VALUE)
        .takeUnless { it == NO_VALUE }
        ?.also {
            action(it)
            remove(key)
        }
}

object AppNavigation {
    const val envelopesList = "envelopesList"
    const val addEnvelope = "addEnvelope"
    const val argEnvelopeHash = "envelopeHash"
    const val argCategoryHash = "categoryHash"
    const val editEnvelope = "editEnvelope/{$argEnvelopeHash}"
    const val addCategory = "addCategory/{$argEnvelopeHash}"
    const val editCategory = "addCategory/{$argCategoryHash}"

    fun editEnvelope(envelope: Envelope) =
        editEnvelope.replace("{$argEnvelopeHash}", "${envelope.hashCode()}")

    fun addCategory(envelope: Envelope) =
        addCategory.replace("{$argEnvelopeHash}", "${envelope.hashCode()}")

    const val start = envelopesList
}
