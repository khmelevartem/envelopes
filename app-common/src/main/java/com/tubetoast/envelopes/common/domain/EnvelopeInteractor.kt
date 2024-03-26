package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.domain.models.Envelope

interface EnvelopeInteractor {
    suspend fun getEnvelopeByName(name: String): Envelope?
    suspend fun getExactEnvelope(id: String): Envelope?
    suspend fun addEnvelope(envelope: Envelope)
    suspend fun deleteEnvelope(envelope: Envelope)
    suspend fun editEnvelope(old: Envelope, new: Envelope)
}
