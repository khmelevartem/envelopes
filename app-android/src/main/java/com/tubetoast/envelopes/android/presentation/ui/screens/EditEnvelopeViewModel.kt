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

    sealed interface Mode {
        fun canConfirm(envelope: Envelope?): Boolean
        fun confirm(envelope: Envelope)
        fun canDelete(): Boolean
        fun delete()
    }

    private var mode: Mode = Create(envelopeInteractor)

    private val draftEnvelope = mutableStateOf(Envelope.EMPTY)

    fun envelope(envelopeId: Int?): State<Envelope> {
        envelopeId?.let { id ->
            envelopeInteractor.getExactEnvelope(id.id())?.let {
                draftEnvelope.value = it
                mode = Edit(editedEnvelope = it, envelopeInteractor = envelopeInteractor)
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

    fun canConfirm(): Boolean = mode.canConfirm(draftEnvelope.value)

    fun confirm() {
        if (!canConfirm()) throw IllegalStateException("Cannot confirm!")
        mode.confirm(draftEnvelope.value)
        reset()
    }

    fun canDelete(): Boolean = mode.canDelete()

    fun delete() {
        if (!canDelete()) throw IllegalStateException("Cannot delete!")
        mode.delete()
        reset()
    }

    private fun reset() {
        mode = Create(envelopeInteractor)
        draftEnvelope.value = Envelope.EMPTY
    }
}


class Create(
    private val envelopeInteractor: EnvelopeInteractor,
) : EditEnvelopeViewModel.Mode {
    override fun canConfirm(envelope: Envelope?) = envelope?.run {
        name.isNotBlank() && envelopeInteractor.getEnvelopeByName(name) == null
    } ?: false

    override fun canDelete() = false
    override fun delete() = throw IllegalStateException("Cannot delete what is not created")
    override fun confirm(envelope: Envelope) = envelopeInteractor.addEnvelope(envelope)
}

class Edit(
    private val envelopeInteractor: EnvelopeInteractor,
    private val editedEnvelope: Envelope
) : EditEnvelopeViewModel.Mode {

    override fun canConfirm(envelope: Envelope?) = envelope?.run {
        name.isNotBlank() && this != editedEnvelope && notSameNameAsOtherExisting()
    } ?: false

    private fun Envelope.notSameNameAsOtherExisting() =
        name == editedEnvelope.name || envelopeInteractor.getEnvelopeByName(name) == null

    override fun canDelete() = true
    override fun delete() = envelopeInteractor.deleteEnvelope(editedEnvelope)
    override fun confirm(envelope: Envelope) =
        envelopeInteractor.editEnvelope(editedEnvelope, envelope)
}
