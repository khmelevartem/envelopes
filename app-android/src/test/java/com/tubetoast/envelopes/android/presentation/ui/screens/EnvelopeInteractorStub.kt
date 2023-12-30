package com.tubetoast.envelopes.android.presentation.ui.screens

import com.tubetoast.envelopes.common.domain.EnvelopeInteractor
import com.tubetoast.envelopes.common.domain.models.Amount
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.Id

class EnvelopeInteractorStub(
    val envelopes: MutableList<Envelope> = mutableListOf(
        Envelope("test", Amount(10))
    )
) : EnvelopeInteractor {
    override fun getEnvelopeByName(name: String): Envelope? {
        return envelopes.find { it.name == name }
    }

    override fun getExactEnvelope(id: Id<Envelope>): Envelope? {
        return envelopes.find { it.id == id }
    }

    override fun addEnvelope(envelope: Envelope) {
        envelopes.add(envelope)
    }

    override fun deleteEnvelope(envelope: Envelope) {
        envelopes.remove(envelope)
    }

    override fun editEnvelope(old: Envelope, new: Envelope) {
        envelopes.run {
            remove(old)
            add(new)
        }
    }

}