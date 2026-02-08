package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.domain.models.Date
import com.tubetoast.envelopes.common.domain.models.DateRange
import com.tubetoast.envelopes.common.domain.models.Spending
import com.tubetoast.envelopes.common.domain.models.monthAsRange
import com.tubetoast.envelopes.common.domain.models.nextMonth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class SpendingInteractorImpl(
    private val repository: SpendingRepository
) : SpendingInteractor {
    override suspend fun getEarliestSpending(): Spending =
        withContext(Dispatchers.IO) {
            repository.getAll().associateBy { it.date }.let {
                it[it.keys.min()]!!
            }
        }

    override suspend fun getMonthsOfSpending(today: Date): MutableList<DateRange> {
        val months = mutableListOf<DateRange>()
        var earliest = getEarliestSpending().date.monthAsRange()
        while (true) {
            months.add(earliest)
            if (today in earliest) return months
            earliest = earliest.nextMonth()
        }
    }
}
