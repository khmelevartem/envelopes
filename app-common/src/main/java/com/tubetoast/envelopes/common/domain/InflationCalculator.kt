package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.domain.models.Date
import com.tubetoast.envelopes.common.domain.models.DateRange
import com.tubetoast.envelopes.common.domain.models.monthAsRange
import com.tubetoast.envelopes.common.domain.models.previousMonth
import com.tubetoast.envelopes.common.domain.models.previousYear
import com.tubetoast.envelopes.common.domain.models.sum
import com.tubetoast.envelopes.common.domain.models.yearAsRange
import com.tubetoast.envelopes.common.domain.snapshots.EnvelopeSnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class InflationCalculator(
    private val snapshotsInteractor: SnapshotsInteractor
) {

    suspend fun calculateInflationByMonths(
        filter: (EnvelopeSnapshot) -> Boolean = { true },
        today: Date = Date.today()
    ): List<Pair<DateRange, Float>> = withContext(Dispatchers.Default) {
        val snapshots = snapshotsInteractor.allSnapshots
        val inflationRates = mutableListOf<Pair<DateRange, Float>>()
        var month = today.monthAsRange()

        while (inflationRates.size < 12) {
            val previousMonth = month.previousMonth()
            val baseSum = snapshots.getSum(filter, previousMonth)
            val newSum = snapshots.getSum(filter, month)
            inflationRates.add(0, month to calculateInflationRate(newSum, baseSum))
            month = previousMonth
        }

        inflationRates
    }

    suspend fun calculateInflationByYears(
        filter: (EnvelopeSnapshot) -> Boolean = { true },
        today: Date = Date.today()
    ): List<Pair<DateRange, Float>> = withContext(Dispatchers.Default) {
        val snapshots = snapshotsInteractor.allSnapshots
        val inflationRates = mutableListOf<Pair<DateRange, Float>>()
        var year = today.yearAsRange()
        var previousYear = year.previousYear()
        var baseSum = snapshots.getSum(filter, previousYear)

        while (baseSum > 0L) {
            val newSum = snapshots.getSum(filter, year)
            inflationRates.add(0, year to calculateInflationRate(newSum, baseSum))
            year = previousYear
            previousYear = year.previousYear()
            baseSum = snapshots.getSum(filter, previousYear)
        }

        inflationRates
    }

    suspend fun calculateInflation(
        baseRange: DateRange,
        newRange: DateRange,
        filter: (EnvelopeSnapshot) -> Boolean = { true }
    ): Float = withContext(Dispatchers.IO) {
        val snapshots = snapshotsInteractor.allSnapshots
        val baseSum = snapshots.getSum(filter, baseRange)
        val newSum = snapshots.getSum(filter, newRange)

        calculateInflationRate(newSum, baseSum)
    }

    private fun Set<EnvelopeSnapshot>.getSum(
        filter: (EnvelopeSnapshot) -> Boolean,
        range: DateRange
    ): Long = filter(filter)
        .flatMap { it.categories }
        .flatMap { it.transactions }
        .filter { it.date in range }
        .map { it.amount }
        .sum()
        .units

    private fun calculateInflationRate(newSum: Long, baseSum: Long) =
        (newSum - baseSum) / baseSum.toFloat()
}
