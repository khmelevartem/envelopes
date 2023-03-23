package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.DomainException
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.Transaction
import kotlinx.coroutines.flow.StateFlow

class AddSpendingInteractorImpl(
    private val repository: SpendingRepository
) : AddSpendingInteractor {

    override fun getEnvelopes() : StateFlow<Set<Envelope>> {
        return repository.envelopes
    }

    override fun addSpending(spending: Transaction, category: Category) {
        val envelope = getEnvelopes().value.find { it.categories.contains(category) }
            ?: throw DomainException("$category is not known to be in any envelope")
        repository.addTransaction(spending, category, envelope)
    }
}