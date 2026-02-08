package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.domain.models.Date
import com.tubetoast.envelopes.common.domain.models.DateRange
import com.tubetoast.envelopes.common.domain.models.Spending

interface SpendingInteractor {
    suspend fun getEarliestSpending(): Spending

    suspend fun getMonthsOfSpending(today: Date = Date.today()): MutableList<DateRange>
}
