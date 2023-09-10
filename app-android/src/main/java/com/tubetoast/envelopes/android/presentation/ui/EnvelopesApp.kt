package com.tubetoast.envelopes.android.presentation.ui

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tubetoast.envelopes.android.presentation.ui.screens.EditEnvelopeScreen
import com.tubetoast.envelopes.android.presentation.ui.screens.EditEnvelopeViewModel
import com.tubetoast.envelopes.android.presentation.ui.screens.EnvelopesListScreen
import com.tubetoast.envelopes.android.presentation.ui.screens.EnvelopesListViewModel
import com.tubetoast.envelopes.common.data.CategoriesRepositoryImpl
import com.tubetoast.envelopes.common.data.EnvelopesRepositoryImpl
import com.tubetoast.envelopes.common.data.SpendingRepositoryImpl
import com.tubetoast.envelopes.common.domain.SnapshotsInteractorImpl
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.stub.StubEditInteractorImpl

private const val NO_VALUE = -1

@Composable
fun EnvelopesApp(
    envelopesListViewModel: EnvelopesListViewModel,
    editEnvelopeViewModel: EditEnvelopeViewModel,
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
                navArgument(AppNavigation.argEnvelopeHash) {
                    type = NavType.IntType
                },
            ),
        ) { navBackStackEntry ->
            EditEnvelopeScreen(
                navController,
                editEnvelopeViewModel.also { vm ->
                    navBackStackEntry.arguments?.takeIntAndRemove(AppNavigation.argEnvelopeHash) {
                        vm.setEnvelopeHash(it)
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
    const val editEnvelope = "editEnvelope/{$argEnvelopeHash}"

    fun editEnvelope(envelope: Envelope) =
        editEnvelope.replace("{$argEnvelopeHash}", "${envelope.hashCode()}")

    const val start = envelopesList
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val repository = EnvelopesRepositoryImpl()
    val editInteractor = StubEditInteractorImpl(repository)
    val snapshotsInteractor = SnapshotsInteractorImpl(
        SpendingRepositoryImpl(),
        CategoriesRepositoryImpl(),
        repository,
    )
    EnvelopesApp(
        EnvelopesListViewModel(
            snapshotsInteractor,
            editInteractor,
        ),
        EditEnvelopeViewModel(
            editInteractor,
            repository,
        ),
    )
}
