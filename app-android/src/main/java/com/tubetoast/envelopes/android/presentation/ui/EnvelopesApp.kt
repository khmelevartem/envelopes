package com.tubetoast.envelopes.android.presentation.ui

import android.os.Bundle
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tubetoast.envelopes.android.presentation.ui.screens.ChooseEnvelopeScreen
import com.tubetoast.envelopes.android.presentation.ui.screens.EditCategoryScreen
import com.tubetoast.envelopes.android.presentation.ui.screens.EditEnvelopeScreen
import com.tubetoast.envelopes.android.presentation.ui.screens.EditGoalScreen
import com.tubetoast.envelopes.android.presentation.ui.screens.EnvelopesListScreen
import com.tubetoast.envelopes.android.presentation.ui.screens.GoalsListScreen
import com.tubetoast.envelopes.android.presentation.ui.screens.SettingsScreen
import com.tubetoast.envelopes.android.presentation.ui.screens.StatisticsScreen
import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.Goal
import com.tubetoast.envelopes.common.domain.models.ImmutableModel

@Composable
fun EnvelopesApp() {
    MaterialTheme {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        NavHost(navController = navController, startDestination = AppNavigation.start) {
            EnvelopesList(navController)
            EnvelopeScreen(navBackStackEntry, navController)
            CategoryScreen(navBackStackEntry, navController)
            ChooseEnvelopeScreen(navBackStackEntry, navController)
            Settings(navController)
            Statistics(navController)
            GoalsList(navController)
            GoalScreen(navBackStackEntry, navController)
        }
    }
}

private fun NavGraphBuilder.GoalScreen(navBackStackEntry: NavBackStackEntry?, navController: NavHostController) {
    composable(
        route = AppNavigation.editGoal,
        arguments = listOf(
            navArgument(AppNavigation.argGoalId) { type = NavType.IntType }
        )
    ) {
        val goalId = navBackStackEntry?.arguments?.run {
            takeInt(AppNavigation.argGoalId)
        }
        EditGoalScreen(
            navController = navController,
            goalId = goalId
        )
    }
}

private fun NavGraphBuilder.GoalsList(navController: NavHostController) {
    composable(
        route = AppNavigation.goalsList
    ) {
        GoalsListScreen(
            navController = navController
        )
    }
}

private fun NavGraphBuilder.Statistics(navController: NavHostController) {
    composable(
        route = AppNavigation.statistics
    ) {
        StatisticsScreen(
            navController = navController
        )
    }
}

private fun NavGraphBuilder.Settings(navController: NavHostController) {
    composable(
        route = AppNavigation.settings
    ) {
        SettingsScreen(
            navController = navController
        )
    }
}

private fun NavGraphBuilder.ChooseEnvelopeScreen(
    navBackStackEntry: NavBackStackEntry?,
    navController: NavHostController
) {
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
}

private fun NavGraphBuilder.CategoryScreen(
    navBackStackEntry: NavBackStackEntry?,
    navController: NavHostController
) {
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
}

private fun NavGraphBuilder.EnvelopeScreen(
    navBackStackEntry: NavBackStackEntry?,
    navController: NavHostController
) {
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
}

private fun NavGraphBuilder.EnvelopesList(navController: NavHostController) {
    composable(route = AppNavigation.envelopesList) {
        EnvelopesListScreen(
            navController = navController
        )
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
    const val argGoalId = "goalId"

    const val envelopeScreen = "envelopeScreen/{$argEnvelopeId}"
    const val categoryScreen = "categoryScreen/{$argCategoryId}/{$argEnvelopeId}"
    const val chooseEnvelope = "chooseEnvelopeScreen/{$argCategoryId}"
    const val settings = "settings"
    const val statistics = "statistics"
    const val goalsList = "goalsList"
    const val editGoal = "editGoal/{$argGoalId}"

    fun addEnvelope() = envelopeScreen.putArg(argEnvelopeId, null)

    fun editEnvelope(envelope: Envelope) = envelopeScreen.putArg(argEnvelopeId, envelope)

    fun addCategory(envelope: Envelope) =
        categoryScreen.putArg(argCategoryId, null).putArg(argEnvelopeId, envelope)

    fun editCategory(category: Category, envelope: Envelope) =
        categoryScreen.putArg(argCategoryId, category).putArg(argEnvelopeId, envelope)

    fun chooseEnvelope(category: Category) =
        chooseEnvelope.putArg(argCategoryId, category)

    fun addGoal() = editGoal.putArg(argGoalId, null)

    fun editGoal(goal: Goal) = editGoal.putArg(argGoalId, goal)

    const val start = envelopesList

    private fun String.putArg(argName: String, argValue: ImmutableModel<*>?) =
        this.replace("{$argName}", "${argValue?.id?.code ?: NO_VALUE}")
}
