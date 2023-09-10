package com.tubetoast.envelopes.android.presentation.ui.screens

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.tubetoast.envelopes.common.domain.EditInteractor
import com.tubetoast.envelopes.common.domain.models.Amount
import com.tubetoast.envelopes.common.domain.models.Envelope

class EditEnvelopeViewModel(
    private val editInteractor: EditInteractor,
//    private val repository: EnvelopesRepository
) : ViewModel() {

    val name = mutableStateOf("")
    val limit: MutableState<Amount?> = mutableStateOf(null)
    val envelope
        get() = Envelope(
            name.value,
            limit.value ?: Amount.ZERO,
        )

    fun setLimit(input: String) {
        limit.value = input.toIntOrNull()?.let { Amount(it) }
    }

    fun confirm() {
        editInteractor.addEnvelope(envelope)
        reset()
    }

    fun delete() {
        if (canDelete()) editInteractor.deleteEnvelope(name.value)
        reset()
    }

    fun canDelete(): Boolean {
        return true
//        repository.get()
    }

    private fun reset() {
        name.value = ""
        limit.value = Amount.ZERO
    }
}
