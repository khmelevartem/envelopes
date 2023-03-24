package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.Transaction
import kotlinx.coroutines.flow.StateFlow

interface EnvelopesRepository {
    val envelopes: StateFlow<Set<Envelope>>
    fun addTransaction(spending: Transaction, category: Category, envelope: Envelope)
    fun addCategory(category: Category, envelope: Envelope)
    fun addEnvelope(envelope: Envelope)
}