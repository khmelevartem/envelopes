package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.domain.models.Amount
import com.tubetoast.envelopes.common.domain.models.Date
import com.tubetoast.envelopes.common.domain.models.Date.Companion.toCalendar
import com.tubetoast.envelopes.common.domain.models.Date.Companion.toDate
import com.tubetoast.envelopes.common.domain.models.DateRange
import com.tubetoast.envelopes.common.domain.models.sum
import com.tubetoast.envelopes.common.domain.snapshots.EnvelopeSnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Calendar

class AverageCalculator(
    private val spendingCalculator: SpendingCalculator
) {

    suspend fun calculateAverage(
        months: Int = 12,
        filter: (EnvelopeSnapshot) -> Boolean = { true },
        today: Date = Date.today()
    ): Amount =
        withContext(Dispatchers.IO) {
            val startDate = today.toCalendar().apply {
                add(Calendar.MONTH, -months)
            }.toDate()

            spendingCalculator.calculateSpendingSum(filter) {
                it.date > startDate
            }.units.div(months).let(::Amount)
        }

    suspend fun calculateMovingAverage(
        months: Int = 12,
        filter: (EnvelopeSnapshot) -> Boolean = { true },
        today: Date = Date.today()
    ): Map<DateRange, Amount> =
        withContext(Dispatchers.IO) {
            val spendingByMonths = spendingCalculator.calculateSpendingSumByMonths(filter, today)
            val monthsSorted = spendingByMonths.keys.sorted()
            val spendingSorted = monthsSorted.mapNotNull { spendingByMonths[it] }
            val moving = sortedMapOf<DateRange, Amount>()
            for (firstMonthIndex in 0..(spendingSorted.size - months)) {
                val lastMonthIndex = firstMonthIndex + months
                moving[monthsSorted[lastMonthIndex - 1]] =
                    spendingSorted.subList(firstMonthIndex, lastMonthIndex).sum()
            }
            moving
        }
}
