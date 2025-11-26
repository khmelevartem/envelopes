package com.tubetoast.envelopes.android.presentation.ui.screens

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tubetoast.envelopes.common.domain.EnvelopeInteractor
import com.tubetoast.envelopes.common.domain.SelectedPeriodRepository
import com.tubetoast.envelopes.common.domain.SnapshotsInteractor
import com.tubetoast.envelopes.common.domain.models.Amount
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.id
import com.tubetoast.envelopes.common.domain.snapshots.CategorySnapshot
import com.tubetoast.envelopes.common.domain.snapshots.sum
import kotlinx.coroutines.launch

class EditEnvelopeViewModel(
    private val envelopeInteractor: EnvelopeInteractor,
    private val snapshotsInteractor: SnapshotsInteractor,
    private val selectedPeriodRepository: SelectedPeriodRepository
) : ViewModel() {

    sealed interface Mode {
        suspend fun canConfirm(envelope: Envelope?): Boolean
        suspend fun confirm(envelope: Envelope)
        suspend fun canDelete(): Boolean
        suspend fun delete()
    }

    data class EnvelopeOperations(
        val canConfirm: Boolean,
        val canDelete: Boolean
    ) {
        companion object {
            val EMPTY = EnvelopeOperations(canConfirm = false, canDelete = false)
        }
    }

    private var mode: Mode = CreateEnvelopeMode(envelopeInteractor)
        set(value) {
            field = value
            isNewEnvelope.value = value is CreateEnvelopeMode
        }
    private val _operations = mutableStateOf(EnvelopeOperations.EMPTY)
    private val draftEnvelope = mutableStateOf(Envelope.EMPTY)
    private val _categories = mutableStateOf(listOf<CategorySnapshot>())

    val operations: State<EnvelopeOperations> = _operations
    val categories: State<List<CategorySnapshot>> = _categories
    val isNewEnvelope = mutableStateOf(true)

    fun envelope(envelopeId: Int?): State<Envelope> {
        envelopeId?.let { id ->
            viewModelScope.launch {
                envelopeInteractor.getExactEnvelope(id.id())?.let {
                    mode = EditEnvelopeMode(envelopeInteractor, it)
                    collectEnvelopeCategories(it)
                    updateEnvelope(it)
                } ?: throw IllegalStateException("Trying to set envelope id $id that doesn't exit")
            }
        } ?: reset()
        return draftEnvelope
    }

    fun setName(input: String) {
        updateEnvelope(draftEnvelope.value.copy(name = input))
    }

    fun setLimit(input: String) {
        val limit = input.toLongOrNull() ?: 0
        require(limit >= 0) { "seems that u need Long for that" }
        updateEnvelope(draftEnvelope.value.copy(limit = Amount(limit)))
    }

    fun confirm() {
        viewModelScope.launch { mode.confirm(draftEnvelope.value) }
        reset()
    }

    fun delete() {
        viewModelScope.launch { mode.delete() }
        reset()
    }

    private fun collectEnvelopeCategories(envelope: Envelope) {
        viewModelScope.launch {
            snapshotsInteractor.envelopeSnapshots(selectedPeriodRepository.selectedPeriodFlow)
                .collect { set ->
                    set.find {
                        it.envelope == envelope
                    }?.let { found ->
                        _categories.value = found.categories
                            .sortedByDescending { it.sum() }
                    }
                }
        }
    }

    private fun updateEnvelope(envelope: Envelope) {
        draftEnvelope.value = envelope
        viewModelScope.launch {
            _operations.value = EnvelopeOperations(
                canConfirm = mode.canConfirm(envelope),
                canDelete = mode.canDelete()
            )
        }
    }

    private fun reset() {
        mode = CreateEnvelopeMode(envelopeInteractor)
        draftEnvelope.value = Envelope.EMPTY
        _operations.value = EnvelopeOperations.EMPTY
        _categories.value = emptyList()
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
