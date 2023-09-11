package com.tubetoast.envelopes.android.presentation.ui.screens

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.tubetoast.envelopes.common.domain.EnvelopeInteractor
import com.tubetoast.envelopes.common.domain.models.Amount
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.hash

class EditEnvelopeViewModel(
    private val envelopeInteractor: EnvelopeInteractor,
) : ViewModel() {

    val envelope = mutableStateOf(Envelope.EMPTY)
    private var old: Envelope? = null

    fun setEnvelopeHash(hash: Int) {
        envelopeInteractor.getExactEnvelope(hash.hash())?.let {
            envelope.value = it
            old = it
        } ?: throw IllegalStateException("Trying to set envelope hash $hash that doesn't exit")
    }

    fun setName(input: String) {
        envelope.value = envelope.value.copy(name = input)
    }

    fun setLimit(input: String) {
        input.toIntOrNull()?.let {
            envelope.value = envelope.value.copy(limit = Amount(it))
        }
    }

    fun confirm() {
        val new = envelope.value
        getExistingEnvelope()?.let { old ->
            envelopeInteractor.editEnvelope(old, new)
        } ?: envelopeInteractor.addEnvelope(new)
        reset()
    }

    fun delete() {
        val existingEnvelope = getExistingEnvelope()
        if (canDelete(existingEnvelope)) envelopeInteractor.deleteEnvelope(existingEnvelope!!)
        reset()
    }

    fun canDelete(envelope: Envelope? = getExistingEnvelope()): Boolean {
        return envelope != null
    }

    private fun getExistingEnvelope(): Envelope? =
        old?.name?.let {
            envelopeInteractor.getEnvelopeByName(it)
        } ?: envelopeInteractor.getEnvelopeByName(envelope.value.name)

    private fun reset() {
        envelope.value = Envelope.EMPTY
        old = null
    }
}
