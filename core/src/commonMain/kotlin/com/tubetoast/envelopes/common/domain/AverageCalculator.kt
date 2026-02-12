package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.domain.models.Amount
import com.tubetoast.envelopes.common.domain.models.Date
import com.tubetoast.envelopes.common.domain.models.Date.Companion.toDate
import com.tubetoast.envelopes.common.domain.models.DateRange
import com.tubetoast.envelopes.common.domain.models.summarize
import com.tubetoast.envelopes.common.domain.snapshots.EnvelopeSnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.minus

class AverageCalculator(
    private val spendingCalculator: SpendingCalculator
) {
    suspend fun calculateAverage(
        months: Int = 12,
        filter: (EnvelopeSnapshot) -> Boolean = { true },
        today: Date = Date.today()
    ): Amount =
        withContext(Dispatchers.Default) {
            // Conversion from your custom Date to kotlinx.datetime.LocalDate
            val todayLocalDate = today.toLocalDate()

            // Subtracting months using kotlinx-datetime
            val startDateLocalDate = todayLocalDate.minus(months, DateTimeUnit.MONTH)

            // Converting back to your custom Date
            val startDate = startDateLocalDate.toDate()

            spendingCalculator
                .calculateSpendingSum(filter) {
                    it.date > startDate
                }.units
                .div(months)
                .let(::Amount)
        }

    suspend fun calculateMovingAverage(
        months: Int = 12,
        filter: (EnvelopeSnapshot) -> Boolean = { true },
        today: Date = Date.today()
    ): Map<DateRange, Amount> =
        withContext(Dispatchers.Default) {
            val spendingByMonths = spendingCalculator.calculateSpendingSumByMonths(filter, today)
            val monthsSorted = spendingByMonths.keys.sorted()
            val spendingSorted = monthsSorted.mapNotNull { spendingByMonths[it] }
            val moving = mutableMapOf<DateRange, Amount>()

            if (spendingSorted.size >= months) {
                for (firstMonthIndex in 0..(spendingSorted.size - months)) {
                    val lastMonthIndex = firstMonthIndex + months
                    moving[monthsSorted[lastMonthIndex - 1]] =
                        spendingSorted.subList(firstMonthIndex, lastMonthIndex).summarize()
                }
            }
            moving.entries.sortedBy { it.key }.associate { it.toPair() }
        }
}
