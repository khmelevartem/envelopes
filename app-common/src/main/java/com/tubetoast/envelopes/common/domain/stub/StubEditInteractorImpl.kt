package com.tubetoast.envelopes.common.domain.stub

import com.tubetoast.envelopes.common.domain.EditInteractor
import com.tubetoast.envelopes.common.domain.EnvelopesRepository
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.put

class StubEditInteractorImpl(
    private val envelopesRepository: EnvelopesRepository,
) : EditInteractor {
    override fun addEnvelope(envelope: Envelope) {
        envelopesRepository.put(envelope)
    }

    override fun deleteEnvelope(envelope: Envelope) {
        envelopesRepository.delete(envelope)
    }

    override fun editEnvelope(old: Envelope, new: Envelope) {
        envelopesRepository.edit(old, new)
    }
}
