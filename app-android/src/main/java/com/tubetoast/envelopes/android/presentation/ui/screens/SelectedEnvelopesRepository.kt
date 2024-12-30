package com.tubetoast.envelopes.android.presentation.ui.screens

import com.tubetoast.envelopes.android.presentation.models.ChoosableEnvelope
import com.tubetoast.envelopes.common.domain.snapshots.EnvelopeSnapshot
import kotlinx.coroutines.flow.StateFlow

interface SelectedEnvelopesRepository {
    val selectedEnvelopes: StateFlow<Set<ChoosableEnvelope>>
    fun changeSelection(change: Set<ChoosableEnvelope>.() -> Set<ChoosableEnvelope>)
}

fun SelectedEnvelopesRepository.isChosen(snapshot: EnvelopeSnapshot) =
    selectedEnvelopes.value.find { it.envelope == snapshot.envelope }
        ?.isChosen ?: false
