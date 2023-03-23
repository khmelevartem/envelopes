package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.Transaction
import kotlinx.coroutines.flow.StateFlow

interface AddSpendingInteractor {
    fun getEnvelopes() : StateFlow<Set<Envelope>>
    fun addSpending(spending: Transaction, category: Category)
}