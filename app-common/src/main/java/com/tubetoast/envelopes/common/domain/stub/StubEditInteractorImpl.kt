package com.tubetoast.envelopes.common.domain.stub

import com.tubetoast.envelopes.common.domain.EnvelopeInteractor
import com.tubetoast.envelopes.common.domain.EnvelopesRepository
import com.tubetoast.envelopes.common.domain.getAll
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.Hash
import com.tubetoast.envelopes.common.domain.put

class StubEditInteractorImpl(
    private val envelopesRepository: EnvelopesRepository,
) : EnvelopeInteractor {
    override fun getEnvelopeByName(name: String): Envelope? {
        return envelopesRepository.getAll().singleOrNull { it.name == name }
    }

    override fun getExactEnvelope(hash: Hash<Envelope>): Envelope? {
        return envelopesRepository.get(hash)
    }

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
