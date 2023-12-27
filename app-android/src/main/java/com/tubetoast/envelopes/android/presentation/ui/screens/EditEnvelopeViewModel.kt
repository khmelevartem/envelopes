package com.tubetoast.envelopes.android.presentation.ui.screens

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.tubetoast.envelopes.common.domain.EnvelopeInteractor
import com.tubetoast.envelopes.common.domain.models.Amount
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.hash

class EditEnvelopeViewModel(
    private val envelopeInteractor: EnvelopeInteractor,
) : ViewModel() {

    /** for display, draft of changes */
    private val draftEnvelope = mutableStateOf(Envelope.EMPTY)

    /** opened for editing */
    private var editedEnvelope: Envelope? = null

    fun envelope(envelopeHash: Int?): State<Envelope> {
        envelopeHash?.let { hash ->
            envelopeInteractor.getExactEnvelope(hash.hash())?.let {
                draftEnvelope.value = it
                editedEnvelope = it
            } ?: throw IllegalStateException("Trying to set envelope hash $hash that doesn't exit")
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

    fun canConfirm(): Boolean {
        return draftEnvelope.value.run {
            name.isNotBlank() && notSameAsOld && (notSameNameAsExistingIfNew || notSameAsExistingIfEdit)
        }
    }

    private val Envelope.notSameAsExistingIfEdit get() = editedEnvelope != null && getExistingEnvelope() != this

    private val Envelope.notSameNameAsExistingIfNew get() = editedEnvelope == null && getExistingEnvelope()?.name != this.name

    private val Envelope.notSameAsOld get() = editedEnvelope != this

    fun delete() {
        val existingEnvelope = getExistingEnvelope()
        if (canDelete(existingEnvelope)) envelopeInteractor.deleteEnvelope(existingEnvelope!!)
        reset()
    }

    fun canDelete(envelope: Envelope? = getExistingEnvelope()): Boolean {
        return envelope != null && editedEnvelope != null
    }

    private fun getExistingEnvelope(): Envelope? =
        editedEnvelope?.name?.let {
            envelopeInteractor.getEnvelopeByName(it)
        } ?: envelopeInteractor.getEnvelopeByName(draftEnvelope.value.name)

    private fun reset() {
        draftEnvelope.value = Envelope.EMPTY
        editedEnvelope = null
    }
}
