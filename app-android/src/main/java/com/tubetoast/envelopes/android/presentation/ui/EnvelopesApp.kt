package com.tubetoast.envelopes.android.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
        composable(route = AppNavigation.editEnvelope) {
            EditEnvelopeScreen(navController, editEnvelopeViewModel)
        }
    }
}

object AppNavigation {
    const val envelopesList = "envelopes list"
    const val editEnvelope = "edit envelope"
    fun editEnvelope(envelope: Envelope) = "$editEnvelope/${envelope.hash}"

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
        ),
    )
}
