package com.tubetoast.envelopes.android.presentation.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tubetoast.envelopes.common.domain.EnvelopeInteractor
import com.tubetoast.envelopes.common.domain.SnapshotsInteractor
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.snapshots.EnvelopeSnapshot
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class EnvelopesListViewModel(
    snapshotsInteractor: SnapshotsInteractor,
    private val envelopeInteractor: EnvelopeInteractor,
) : ViewModel() {
    fun delete(envelope: Envelope) {
        viewModelScope.launch {
            envelopeInteractor.deleteEnvelope(envelope)
        }
    }

    val itemModels: Flow<List<EnvelopeSnapshot>> by lazy {
        snapshotsInteractor.envelopeSnapshotFlow.map { it.toList() }
    }
}
