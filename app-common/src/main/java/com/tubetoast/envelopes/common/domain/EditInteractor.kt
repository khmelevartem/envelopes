package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.domain.models.Envelope

interface EditInteractor {
    fun addEnvelope(envelope: Envelope)
    fun deleteEnvelope(envelope: Envelope)
    fun deleteEnvelope(envelopeName: String)
}
