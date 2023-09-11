package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.Hash

interface EnvelopeInteractor {
    fun getEnvelopeByName(name: String): Envelope?
    fun getExactEnvelope(hash: Hash<Envelope>): Envelope?
    fun addEnvelope(envelope: Envelope)
    fun deleteEnvelope(envelope: Envelope)
    fun editEnvelope(old: Envelope, new: Envelope)
}
