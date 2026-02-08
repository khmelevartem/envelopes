package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.Id

interface EnvelopeInteractor {
    suspend fun getAll(): Set<Envelope>

    suspend fun getEnvelopeByName(name: String): Envelope?

    suspend fun getExactEnvelope(id: Id<Envelope>): Envelope?

    suspend fun addEnvelope(envelope: Envelope)

    suspend fun deleteEnvelope(envelope: Envelope)

    suspend fun editEnvelope(
        old: Envelope,
        new: Envelope
    )
}
