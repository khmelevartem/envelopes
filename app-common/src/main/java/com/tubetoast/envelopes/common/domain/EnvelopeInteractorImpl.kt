package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.Id
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EnvelopeInteractorImpl(
    private val repository: EnvelopesRepository
) : EnvelopeInteractor {

    private val dispatcher = Dispatchers.IO

    override suspend fun getAll(): Set<Envelope> = withContext(dispatcher) {
        repository.getAll()
    }

    override suspend fun getEnvelopeByName(name: String) = withContext(dispatcher) {
        repository.getAll().singleOrNull { it.name == name }
    }

    override suspend fun getExactEnvelope(id: Id<Envelope>): Envelope? = withContext(dispatcher) {
        repository.get(id)
    }

    override suspend fun addEnvelope(envelope: Envelope) = withContext(dispatcher) {
        repository.put(envelope)
    }

    override suspend fun deleteEnvelope(envelope: Envelope) = withContext(dispatcher) {
        repository.delete(envelope)
    }

    override suspend fun editEnvelope(old: Envelope, new: Envelope) = withContext(dispatcher) {
        repository.edit(old, new)
    }
}
