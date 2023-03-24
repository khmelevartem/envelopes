package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.Transaction
import kotlinx.coroutines.flow.StateFlow

interface EnvelopesInteractor {
    val envelopes: StateFlow<Set<Envelope>>

    fun addSpending(spending: Transaction, category: Category)
    fun addCategory(category: Category, envelope: Envelope)
    fun addEnvelope(envelope: Envelope)
}