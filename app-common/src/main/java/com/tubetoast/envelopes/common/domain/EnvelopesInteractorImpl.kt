package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.domain.models.*

class EnvelopesInteractorImpl(
    private val repository: EnvelopesRepository
) : EnvelopesInteractor {

    override val envelopes get() = repository.envelopes

    override fun addSpending(spending: Transaction, category: Category) {
        val envelope = envelopes.value.find { it.categories.contains(category) }
            ?: throw DomainException("$category is not known to be in any envelope")
        repository.addTransaction(spending, category, envelope)
    }

    override fun addCategory(category: Category, envelope: Envelope) {
        repository.addCategory(category, envelope)
    }

    override fun addEnvelope(envelope: Envelope) {
        repository.addEnvelope(envelope)
    }
}