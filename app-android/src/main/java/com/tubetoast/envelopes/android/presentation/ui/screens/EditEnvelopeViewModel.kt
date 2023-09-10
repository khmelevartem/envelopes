package com.tubetoast.envelopes.android.presentation.ui.screens

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.tubetoast.envelopes.common.domain.EditInteractor
import com.tubetoast.envelopes.common.domain.EnvelopesRepository
import com.tubetoast.envelopes.common.domain.models.Amount
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.hash

class EditEnvelopeViewModel(
    private val editInteractor: EditInteractor,
    private val repository: EnvelopesRepository,
) : ViewModel() {

    val envelope = mutableStateOf(Envelope.EMPTY)

    fun setEnvelopeHash(hash: Int) {
        repository.get(hash.hash<Envelope>())?.let {
            envelope.value = it
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
            editInteractor.editEnvelope(old, new)
        } ?: editInteractor.addEnvelope(new)
        reset()
    }

    fun delete() {
        val existingEnvelope = getExistingEnvelope()
        if (canDelete(existingEnvelope)) editInteractor.deleteEnvelope(existingEnvelope!!)
        reset()
    }

    fun canDelete(envelope: Envelope? = getExistingEnvelope()): Boolean {
        return envelope != null
    }

    private fun getExistingEnvelope(): Envelope? = repository.get(envelope.value.hash)

    private fun reset() {
        envelope.value = Envelope.EMPTY
    }
}
