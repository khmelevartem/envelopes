package com.tubetoast.envelopes.android.presentation.ui.screens

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tubetoast.envelopes.common.domain.models.SelectableEnvelope
import com.tubetoast.envelopes.common.domain.CategoryInteractor
import com.tubetoast.envelopes.common.domain.SnapshotsInteractor
import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.id
import com.tubetoast.envelopes.common.settings.MutableSettingsRepository
import com.tubetoast.envelopes.common.settings.Setting
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class SelectEnvelopeViewModel(
    private val categoryInteractor: CategoryInteractor,
    private val snapshotsInteractor: SnapshotsInteractor,
    settingsRepository: MutableSettingsRepository
) : ViewModel() {

    private val categoryFlow = MutableStateFlow(Category.EMPTY)
    private val envelopesFlow = mutableStateOf(emptyList<SelectableEnvelope>())
    val isColorful = settingsRepository.getSettingFlow(Setting.Key.COLORFUL)

    init {
        viewModelScope.launch {
            snapshotsInteractor.allEnvelopeSnapshotsFlow.combine(categoryFlow) { snapshots, category ->
                envelopesFlow.value = snapshots.map { snapshot ->
                    SelectableEnvelope(
                        snapshot.envelope,
                        snapshot.categories.find { it.category == category } != null
                    )
                }
            }.collect()
        }
    }

    fun envelopes(): State<List<SelectableEnvelope>> = envelopesFlow

    fun category(id: Int?): Flow<Category> {
        id?.let {
            viewModelScope.launch {
                categoryInteractor.getCategory(id.id())?.let {
                    categoryFlow.value = it
                } ?: throw IllegalStateException("Trying to set category id $id that doesn't exit")
            }
        }
        return categoryFlow
    }

    fun setNewChosenEnvelope(envelope: Envelope) {
        viewModelScope.launch {
            categoryInteractor.moveCategory(categoryFlow.value, envelope.id)
        }
    }
}
