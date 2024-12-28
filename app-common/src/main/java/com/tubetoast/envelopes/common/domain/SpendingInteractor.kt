package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.domain.models.Spending

interface SpendingInteractor {

    suspend fun getEarliestSpending(): Spending
}
