package com.tubetoast.envelopes.android.presentation.ui.screens

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.tubetoast.envelopes.common.domain.EnvelopeInteractor
import com.tubetoast.envelopes.common.domain.models.Amount
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.id

class EditEnvelopeViewModel(
    private val envelopeInteractor: EnvelopeInteractor,
) : ViewModel() {

    /** for display, draft of changes */
    private val draftEnvelope = mutableStateOf(Envelope.EMPTY)

    /** opened for editing */
    private var editedEnvelope: Envelope? = null

    fun envelope(envelopeId: Int?): State<Envelope> {
        envelopeId?.let { id ->
            envelopeInteractor.getExactEnvelope(id.id())?.let {
                draftEnvelope.value = it
                editedEnvelope = it
            } ?: throw IllegalStateException("Trying to set envelope id $id that doesn't exit")
        } ?: reset()
        return draftEnvelope
    }

    fun setName(input: String) {
        draftEnvelope.value = draftEnvelope.value.copy(name = input)
    }

    fun setLimit(input: String) {
        if (input.isBlank()) {
            draftEnvelope.value = draftEnvelope.value.copy(limit = Amount.ZERO)
        } else {
            input.toIntOrNull()?.let {
                draftEnvelope.value = draftEnvelope.value.copy(limit = Amount(it))
            }
        }
    }

    fun confirm() {
        if (!canConfirm()) throw IllegalStateException("Cannot confirm!")
        val new = draftEnvelope.value
        getExistingEnvelope()?.let { old ->
            envelopeInteractor.editEnvelope(old, new)
        } ?: envelopeInteractor.addEnvelope(new)
        reset()
    }

    fun canConfirm(): Boolean = draftEnvelope.value.run {
        name.isNotBlank() &&
                (notSameAsExistingIfEdit(this) || notSameNameAsExistingIfNew(this))
    }

    private fun notSameNameAsExistingIfNew(draftEnvelope: Envelope) = editedEnvelope == null
            && envelopeInteractor.getEnvelopeByName(draftEnvelope.name) == null

    private fun notSameAsExistingIfEdit(draftEnvelope: Envelope) = editedEnvelope != null
            && editedEnvelope != draftEnvelope

    fun delete() {
        val existingEnvelope = getExistingEnvelope()
        if (canDelete(existingEnvelope)) envelopeInteractor.deleteEnvelope(existingEnvelope!!)
        reset()
    }

    fun canDelete(envelope: Envelope? = getExistingEnvelope()): Boolean {
        return envelope != null && editedEnvelope != null
    }

    private fun getExistingEnvelope(): Envelope? =
        editedEnvelope?.name?.let { envelopeInteractor.getEnvelopeByName(it) }
            ?: envelopeInteractor.getEnvelopeByName(draftEnvelope.value.name)

    private fun reset() {
        draftEnvelope.value = Envelope.EMPTY
        editedEnvelope = null
    }
}
