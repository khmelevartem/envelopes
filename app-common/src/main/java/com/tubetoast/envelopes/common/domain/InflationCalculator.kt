package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.domain.models.Date
import com.tubetoast.envelopes.common.domain.models.DateRange
import com.tubetoast.envelopes.common.domain.models.Transaction
import com.tubetoast.envelopes.common.domain.models.monthAsRange
import com.tubetoast.envelopes.common.domain.models.nextMonth
import com.tubetoast.envelopes.common.domain.models.previousMonth
import com.tubetoast.envelopes.common.domain.models.previousYear
import com.tubetoast.envelopes.common.domain.models.sum
import com.tubetoast.envelopes.common.domain.models.yearAsRange
import com.tubetoast.envelopes.common.domain.snapshots.CategorySnapshot
import com.tubetoast.envelopes.common.domain.snapshots.EnvelopeSnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class InflationCalculator(
    private val snapshotsInteractor: SnapshotsInteractor,
    private val spendingInteractor: SpendingInteractor
) {

    suspend fun calculateInflationByMonths(
        filter: (EnvelopeSnapshot) -> Boolean = { true },
        today: Date = Date.today()
    ): List<Pair<DateRange, Float>> = withContext(Dispatchers.Default) {
        val snapshots = snapshotsInteractor.allSnapshots.filter(filter)
        val inflationRates = mutableListOf<Pair<DateRange, Float>>()
        var month = today.monthAsRange()
        val earliest = spendingInteractor.getEarliestSpending().date
        var newSum = snapshots.getSum(month)

        while (earliest !in month) {
            month = month.previousMonth()
            val baseSum = snapshots.getSum(month)
            inflationRates.add(month.nextMonth() to calculateInflationRate(newSum, baseSum))
            newSum = baseSum
        }

        inflationRates.reversed()
    }

    suspend fun calculateInflationByYears(
        filter: (EnvelopeSnapshot) -> Boolean = { true },
        today: Date = Date.today()
    ): List<Pair<DateRange, Float>> = withContext(Dispatchers.Default) {
        val snapshots = snapshotsInteractor.allSnapshots.filter(filter)
        val inflationRates = mutableListOf<Pair<DateRange, Float>>()
        var year = today.yearAsRange()
        var previousYear = year.previousYear()
        var baseSum = snapshots.getSum(previousYear)

        while (baseSum > 0L) {
            val newSum = snapshots.getSum(year)
            inflationRates.add(0, year to calculateInflationRate(newSum, baseSum))
            year = previousYear
            previousYear = year.previousYear()
            baseSum = snapshots.getSum(previousYear)
        }

        inflationRates
    }

    suspend fun calculateInflation(
        baseRange: DateRange,
        newRange: DateRange,
        filter: (EnvelopeSnapshot) -> Boolean = { true }
    ): Float = withContext(Dispatchers.IO) {
        val snapshots = snapshotsInteractor.allSnapshots.filter(filter)
        val baseSum = snapshots.getSum(baseRange)
        val newSum = snapshots.getSum(newRange)

        calculateInflationRate(newSum, baseSum)
    }

    private fun Collection<EnvelopeSnapshot>.getSum(range: DateRange): Long =
        flatMap(EnvelopeSnapshot::categories)
            .flatMap(CategorySnapshot::transactions)
            .filter { it.date in range }
            .map(Transaction<*>::amount)
            .sum()
            .units

    private fun calculateInflationRate(newSum: Long, baseSum: Long) =
        (newSum - baseSum) / baseSum.toFloat()
}
