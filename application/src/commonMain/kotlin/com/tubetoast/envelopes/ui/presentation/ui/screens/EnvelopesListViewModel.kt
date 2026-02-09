package com.tubetoast.envelopes.ui.presentation.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tubetoast.envelopes.common.domain.EnvelopeInteractor
import com.tubetoast.envelopes.common.domain.SelectedPeriodRepository
import com.tubetoast.envelopes.common.domain.SnapshotsInteractor
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.snapshots.EnvelopeSnapshot
import com.tubetoast.envelopes.common.settings.MutableSettingsRepository
import com.tubetoast.envelopes.common.settings.Setting
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted.Companion.Eagerly
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EnvelopesListViewModel(
    snapshotsInteractor: SnapshotsInteractor,
    private val envelopeInteractor: EnvelopeInteractor,
    settingsRepository: MutableSettingsRepository,
    selectedPeriodRepository: SelectedPeriodRepository
) : ViewModel() {
    val filterByYear = settingsRepository
        .getSettingFlow(Setting.Key.FILTER_BY_YEAR)
        .map { it.checked }
        .stateIn(
            viewModelScope,
            started = Eagerly,
            initialValue = settingsRepository.getSetting(Setting.Key.FILTER_BY_YEAR).checked
        )

    val itemModels: Flow<Iterable<EnvelopeSnapshot>> = snapshotsInteractor
        .envelopeSnapshots(selectedPeriodRepository.selectedPeriodFlow)

    val isColorful = settingsRepository.getSettingFlow(Setting.Key.COLORFUL)

    fun delete(envelope: Envelope) {
        viewModelScope.launch {
            envelopeInteractor.deleteEnvelope(envelope)
        }
    }
}
