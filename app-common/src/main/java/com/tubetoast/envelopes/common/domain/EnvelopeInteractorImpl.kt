package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.Id

class EnvelopeInteractorImpl(
    private val repository: UpdatingEnvelopesRepository,
) : EnvelopeInteractor {
    override fun getEnvelopeByName(name: String): Envelope? {
        return repository.getAll().singleOrNull { it.name == name }
    }

    override fun getExactEnvelope(id: Id<Envelope>): Envelope? {
        return repository.get(id)
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
