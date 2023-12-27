package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.Id

interface EnvelopeInteractor {
    fun getEnvelopeByName(name: String): Envelope?
    fun getExactEnvelope(id: Id<Envelope>): Envelope?
    fun addEnvelope(envelope: Envelope)
    fun deleteEnvelope(envelope: Envelope)
    fun editEnvelope(old: Envelope, new: Envelope)
}
