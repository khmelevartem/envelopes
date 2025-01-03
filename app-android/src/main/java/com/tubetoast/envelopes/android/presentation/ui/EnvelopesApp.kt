package com.tubetoast.envelopes.android.presentation.ui

import android.os.Bundle
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tubetoast.envelopes.android.presentation.ui.screens.ChooseEnvelopeScreen
import com.tubetoast.envelopes.android.presentation.ui.screens.EditCategoryScreen
import com.tubetoast.envelopes.android.presentation.ui.screens.EditEnvelopeScreen
import com.tubetoast.envelopes.android.presentation.ui.screens.EnvelopesListScreen
import com.tubetoast.envelopes.android.presentation.ui.screens.SettingsScreen
import com.tubetoast.envelopes.android.presentation.ui.screens.StatisticsScreen
import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.ImmutableModel

@Composable
fun EnvelopesApp() {
    MaterialTheme {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        NavHost(navController = navController, startDestination = AppNavigation.start) {
            composable(route = AppNavigation.envelopesList) {
                EnvelopesListScreen(
                    navController = navController
                )
            }
            composable(
                route = AppNavigation.envelopeScreen,
                arguments = listOf(
                    navArgument(AppNavigation.argEnvelopeId) { type = NavType.IntType }
                )
            ) {
                val envelopeId = navBackStackEntry?.arguments?.takeInt(AppNavigation.argEnvelopeId)
                EditEnvelopeScreen(
                    navController = navController,
                    envelopeId = envelopeId
                )
            }
            composable(
                route = AppNavigation.categoryScreen,
                arguments = listOf(
                    navArgument(AppNavigation.argCategoryId) { type = NavType.IntType },
                    navArgument(AppNavigation.argEnvelopeId) { type = NavType.IntType }
                )
            ) {
                navBackStackEntry?.arguments?.run {
                    val envelopeId = takeInt(AppNavigation.argEnvelopeId)
                    val categoryId = takeInt(AppNavigation.argCategoryId)
                    EditCategoryScreen(
                        navController = navController,
                        categoryId = categoryId,
                        envelopeId = envelopeId
                    )
                }
            }
            composable(
                route = AppNavigation.chooseEnvelope,
                arguments = listOf(
                    navArgument(AppNavigation.argCategoryId) { type = NavType.IntType }
                )
            ) {
                navBackStackEntry?.arguments?.run {
                    val categoryId = takeInt(AppNavigation.argCategoryId)
                    ChooseEnvelopeScreen(
                        navController = navController,
                        categoryId = categoryId
                    )
                }
            }
            composable(
                route = AppNavigation.settings
            ) {
                SettingsScreen(
                    navController = navController
                )
            }
            composable(
                route = AppNavigation.statistics
            ) {
                StatisticsScreen(
                    navController = navController
                )
            }
        }
    }
}

private const val NO_VALUE = -1

private fun Bundle.takeInt(key: String): Int? {
    return getInt(key, NO_VALUE).takeUnless { it == NO_VALUE }
}

@Suppress("ConstPropertyName")
object AppNavigation {
    const val envelopesList = "envelopesListScreen"
    const val argEnvelopeId = "envelopeId"
    const val argCategoryId = "categoryId"
    const val envelopeScreen = "envelopeScreen/{$argEnvelopeId}"
    const val categoryScreen = "categoryScreen/{$argCategoryId}/{$argEnvelopeId}"
    const val chooseEnvelope = "chooseEnvelopeScreen/{$argCategoryId}"
    const val settings = "settings"
    const val statistics = "statistics"

    fun addEnvelope() = envelopeScreen.putArg(argEnvelopeId, null)

    fun editEnvelope(envelope: Envelope) = envelopeScreen.putArg(argEnvelopeId, envelope)

    fun addCategory(envelope: Envelope) =
        categoryScreen.putArg(argCategoryId, null).putArg(argEnvelopeId, envelope)

    fun editCategory(category: Category, envelope: Envelope) =
        categoryScreen.putArg(argCategoryId, category).putArg(argEnvelopeId, envelope)

    private fun String.putArg(argName: String, argValue: ImmutableModel<*>?) =
        this.replace("{$argName}", "${argValue?.id?.code ?: NO_VALUE}")

    fun chooseEnvelope(category: Category) =
        chooseEnvelope.putArg(argCategoryId, category)

    const val start = envelopesList
}
