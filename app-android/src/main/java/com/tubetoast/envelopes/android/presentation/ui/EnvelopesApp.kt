package com.tubetoast.envelopes.android.presentation.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
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
import kotlinx.serialization.Serializable

@Composable
fun EnvelopesApp() {
    MaterialTheme {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = AppNavigation.start) {
            EnvelopesList(navController)
            EnvelopeDetails(navController)
            CategoryDetails(navController)
            ChooseEnvelope(navController)
            Settings(navController)
            Statistics(navController)
            GoalsList(navController)
            GoalDetails(navController)
        }
    }
}

@Serializable
data class GoalDetails(
    val goalId: Int? = null
)

private fun NavGraphBuilder.GoalDetails(navController: NavHostController) {
    composable<GoalDetails> {
        EditGoalScreen(
            navController = navController,
            goalId = it.toRoute<GoalDetails>().goalId
        )
    }
}

@Serializable
object GoalsList

private fun NavGraphBuilder.GoalsList(navController: NavHostController) {
    composable<GoalsList> {
        GoalsListScreen(navController = navController)
    }
}

@Serializable
object Statistics

private fun NavGraphBuilder.Statistics(navController: NavHostController) {
    composable<Statistics> {
        StatisticsScreen(navController = navController)
    }
}

@Serializable
object Settings

private fun NavGraphBuilder.Settings(navController: NavHostController) {
    composable<Settings> {
        SettingsScreen(navController = navController)
    }
}

@Serializable
data class ChooseEnvelope(
    val categoryId: Int
)

private fun NavGraphBuilder.ChooseEnvelope(
    navController: NavHostController
) {
    composable<ChooseEnvelope> {
        ChooseEnvelopeScreen(
            navController = navController,
            categoryId = it.toRoute<ChooseEnvelope>().categoryId
        )
    }
}

@Serializable
data class CategoryDetails(
    val envelopeId: Int,
    val categoryId: Int? = null
)

private fun NavGraphBuilder.CategoryDetails(
    navController: NavHostController
) {
    composable<CategoryDetails> {
        val (envelopeId, categoryId) = it.toRoute<CategoryDetails>()
        EditCategoryScreen(
            navController = navController,
            categoryId = categoryId,
            envelopeId = envelopeId
        )
    }
}

@Serializable
data class EnvelopeDetails(
    val envelopeId: Int? = null
)

private fun NavGraphBuilder.EnvelopeDetails(navController: NavHostController) {
    composable<EnvelopeDetails> {
        val envelopeId = it.toRoute<EnvelopeDetails>().envelopeId
        EditEnvelopeScreen(
            navController = navController,
            envelopeId = envelopeId
        )
    }
}

@Serializable
object EnvelopesList

private fun NavGraphBuilder.EnvelopesList(navController: NavHostController) {
    composable<EnvelopesList> {
        EnvelopesListScreen(
            navController = navController
        )
    }
}

object AppNavigation {
    val start = EnvelopesList

    fun addEnvelope() =
        EnvelopeDetails()

    fun editEnvelope(envelope: Envelope) =
        EnvelopeDetails(envelope.id.code)

    fun addCategory(envelope: Envelope) =
        CategoryDetails(envelopeId = envelope.id.code)

    fun editCategory(category: Category, envelope: Envelope) =
        CategoryDetails(envelopeId = envelope.id.code, categoryId = category.id.code)

    fun chooseEnvelope(category: Category) =
        ChooseEnvelope(category.id.code)

    fun settings() = Settings

    fun statistics() = Statistics

    fun goalsList() = GoalsList

    fun addGoal() = GoalDetails()

    fun editGoal(goal: Goal) = GoalDetails(goal.id.code)
}
