package com.tubetoast.envelopes.android.presentation.ui.screens

import com.tubetoast.envelopes.android.presentation.models.ChoosableEnvelope
import kotlinx.coroutines.flow.StateFlow

interface SelectedEnvelopesRepository {
    val selectedEnvelopes: StateFlow<Set<ChoosableEnvelope>>
    fun changeSelection(change: Set<ChoosableEnvelope>.() -> Set<ChoosableEnvelope>)
}
