package com.tubetoast.envelopes.android.presentation.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tubetoast.envelopes.common.domain.EnvelopeInteractor
import com.tubetoast.envelopes.common.domain.SnapshotsInteractor
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.snapshots.EnvelopeSnapshot
import com.tubetoast.envelopes.common.settings.MutableSettingsRepository
import com.tubetoast.envelopes.common.settings.Setting
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class EnvelopesListViewModel(
    snapshotsInteractor: SnapshotsInteractor,
    private val envelopeInteractor: EnvelopeInteractor,
    settingsRepository: MutableSettingsRepository
) : ViewModel() {

    private val _filterByYear = settingsRepository.getSettingFlow(Setting.Key.FILTER_BY_YEAR)

    val filterByYear get() = _filterByYear.value.checked

    val itemModels: Flow<Iterable<EnvelopeSnapshot>> = snapshotsInteractor.snapshotsBySelectedPeriod()

    fun delete(envelope: Envelope) {
        viewModelScope.launch {
            envelopeInteractor.deleteEnvelope(envelope)
        }
    }
}
