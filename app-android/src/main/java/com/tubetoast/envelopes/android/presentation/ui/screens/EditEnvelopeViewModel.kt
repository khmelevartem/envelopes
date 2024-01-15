package com.tubetoast.envelopes.android.presentation.ui.screens

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tubetoast.envelopes.common.domain.EnvelopeInteractor
import com.tubetoast.envelopes.common.domain.models.Amount
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.id
import kotlinx.coroutines.launch

class EditEnvelopeViewModel(
    private val envelopeInteractor: EnvelopeInteractor
) : ViewModel() {

    sealed interface Mode {
        suspend fun canConfirm(envelope: Envelope?): Boolean
        suspend fun confirm(envelope: Envelope)
        suspend fun canDelete(): Boolean
        suspend fun delete()
    }

    data class UIState(
        val draftEnvelope: Envelope,
        val canConfirm: Boolean,
        val canDelete: Boolean
    ) {
        companion object {
            val EMPTY = UIState(Envelope.EMPTY, canConfirm = false, canDelete = false)
        }
    }

    private var mode: Mode = CreateEnvelopeMode(envelopeInteractor)

    private val uiState = mutableStateOf(UIState.EMPTY)

    fun uiState(envelopeId: Int?): State<UIState> {
        envelopeId?.let { id ->
            viewModelScope.launch {
                envelopeInteractor.getExactEnvelope(id.id())?.let {
                    updateUIState(it)
                    mode = EditEnvelopeMode(
                        editedEnvelope = it,
                        envelopeInteractor = envelopeInteractor
                    )
                } ?: throw IllegalStateException("Trying to set envelope id $id that doesn't exit")
            }
        } ?: reset()
        return uiState
    }

    private fun updateUIState(envelope: Envelope = uiState.value.draftEnvelope) {
        viewModelScope.launch {
            uiState.value = UIState(envelope, mode.canConfirm(envelope), mode.canDelete())
        }
    }

    fun setName(input: String) {
        updateUIState(uiState.value.draftEnvelope.copy(name = input))
    }

    fun setLimit(input: String) {
        if (input.isBlank()) {
            updateUIState(uiState.value.draftEnvelope.copy(limit = Amount.ZERO))
        } else {
            input.toIntOrNull()?.let {
                updateUIState(uiState.value.draftEnvelope.copy(limit = Amount(it)))
            }
        }
    }

    fun confirm() {
        viewModelScope.launch { mode.confirm(uiState.value.draftEnvelope) }
        reset()
    }

    fun delete() {
        viewModelScope.launch { mode.delete() }
        reset()
    }

    private fun reset() {
        mode = CreateEnvelopeMode(envelopeInteractor)
        uiState.value = UIState.EMPTY
    }
}

class CreateEnvelopeMode(
    private val envelopeInteractor: EnvelopeInteractor
) : EditEnvelopeViewModel.Mode {
    override suspend fun canConfirm(envelope: Envelope?) = envelope?.run {
        name.isNotBlank() && envelopeInteractor.getEnvelopeByName(name) == null
    } ?: false

    override suspend fun canDelete() = false
    override suspend fun delete() = throw IllegalStateException("Cannot delete what is not created")
    override suspend fun confirm(envelope: Envelope) = envelopeInteractor.addEnvelope(envelope)
}

class EditEnvelopeMode(
    private val envelopeInteractor: EnvelopeInteractor,
    private val editedEnvelope: Envelope
) : EditEnvelopeViewModel.Mode {

    override suspend fun canConfirm(envelope: Envelope?) = envelope?.run {
        name.isNotBlank() && this != editedEnvelope && notSameNameAsOtherExisting()
    } ?: false

    private suspend fun Envelope.notSameNameAsOtherExisting() =
        name == editedEnvelope.name || envelopeInteractor.getEnvelopeByName(name) == null

    override suspend fun canDelete() = true
    override suspend fun delete() = envelopeInteractor.deleteEnvelope(editedEnvelope)
    override suspend fun confirm(envelope: Envelope) =
        envelopeInteractor.editEnvelope(editedEnvelope, envelope)
}
