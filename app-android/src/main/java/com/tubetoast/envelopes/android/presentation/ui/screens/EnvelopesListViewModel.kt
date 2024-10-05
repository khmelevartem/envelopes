package com.tubetoast.envelopes.android.presentation.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tubetoast.envelopes.android.presentation.state.SelectedPeriodRepository
import com.tubetoast.envelopes.common.domain.EnvelopeInteractor
import com.tubetoast.envelopes.common.domain.SnapshotsInteractor
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.snapshots.EnvelopeSnapshot
import com.tubetoast.envelopes.common.settings.MutableSettingsRepository
import com.tubetoast.envelopes.common.settings.Setting
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

class EnvelopesListViewModel(
    private val snapshotsInteractor: SnapshotsInteractor,
    private val envelopeInteractor: EnvelopeInteractor,
    settingsRepository: MutableSettingsRepository,
    private val selectedPeriodRepository: SelectedPeriodRepository
) : ViewModel() {

    private var job: Job? = null
    private val _filterByYear = settingsRepository.getSettingFlow(Setting.Key.FILTER_BY_YEAR)

    val filterByYear get() = _filterByYear.value.checked

    val itemModels: Flow<Iterable<EnvelopeSnapshot>> = callbackFlow {
        startCollecting()
        awaitClose {
            stopCollecting()
        }
    }

    private fun stopCollecting() {
        job?.cancel()
        job = null
    }

    fun delete(envelope: Envelope) {
        viewModelScope.launch {
            envelopeInteractor.deleteEnvelope(envelope)
        }
    }

    private fun ProducerScope<Iterable<EnvelopeSnapshot>>.startCollecting() {
        viewModelScope.launch {
            selectedPeriodRepository.selectedPeriodFlow.collect {
                stopCollecting()
                job = this@startCollecting.launch {
                    snapshotsInteractor.snapshotsByDatesFlow(it)
                        .collect {
                            this@startCollecting.trySendBlocking(it)
                        }
                }
            }
        }


    }
}
