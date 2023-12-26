package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.Hash

class EnvelopeInteractorImpl(
    private val repository: EnvelopesRepository,
) : EnvelopeInteractor {
    override fun getEnvelopeByName(name: String): Envelope? {
        return repository.getAll().singleOrNull { it.name == name }
    }

    override fun getExactEnvelope(hash: Hash<Envelope>): Envelope? {
        return repository.get(hash)
    }

    override fun addEnvelope(envelope: Envelope) {
        repository.put(envelope)
    }

    override fun deleteEnvelope(envelope: Envelope) {
        repository.delete(envelope)
    }

    override fun editEnvelope(old: Envelope, new: Envelope) {
        repository.edit(old, new)
    }
}
