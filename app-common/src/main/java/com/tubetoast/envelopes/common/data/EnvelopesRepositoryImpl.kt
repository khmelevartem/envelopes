package com.tubetoast.envelopes.common.data

import com.tubetoast.envelopes.common.domain.EnvelopesRepository
import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.DomainException
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.Transaction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class EnvelopesRepositoryImpl: EnvelopesRepository {

    private val _envelopes: MutableStateFlow<Set<Envelope>> = MutableStateFlow(emptySet())
    override val envelopes: StateFlow<Set<Envelope>>
        get() = _envelopes.asStateFlow()

    override fun addTransaction(spending: Transaction, category: Category, envelope: Envelope) {
        val newEnvelopes = _envelopes.value.toMutableSet()
        if (!newEnvelopes.remove(envelope)) throw DomainException()

        val newCategoriesInEnvelope = envelope.categories.toMutableSet()
        if (!newCategoriesInEnvelope.remove(category)) throw DomainException()

        val newSpending = category.spending.toMutableList().also { it.add(spending) }

        newCategoriesInEnvelope.add(
            category.copy(
                spending = newSpending
            )
        )

        newEnvelopes.add(
            envelope.copy(
                categories = newCategoriesInEnvelope
            )
        )
        
        _envelopes.value = newEnvelopes
    }

    override fun addCategory(category: Category, envelope: Envelope) {
        val newEnvelopes = _envelopes.value.toMutableSet()
        if (!newEnvelopes.remove(envelope)) throw DomainException()

        val newCategoriesInEnvelope = envelope.categories.toMutableSet()
        if (newCategoriesInEnvelope.contains(category)) throw DomainException("Already have $category")

        newCategoriesInEnvelope.add(category)

        newEnvelopes.add(
            envelope.copy(
                categories = newCategoriesInEnvelope
            )
        )

        _envelopes.value = newEnvelopes
    }

    override fun addEnvelope(envelope: Envelope) {
        val newEnvelopes = _envelopes.value.toMutableSet()
        if (newEnvelopes.contains(envelope)) throw DomainException("Already have $envelope")

        newEnvelopes.add(envelope)

        _envelopes.value = newEnvelopes
    }

}