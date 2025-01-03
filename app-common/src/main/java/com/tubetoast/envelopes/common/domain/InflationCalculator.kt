package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.domain.models.Date
import com.tubetoast.envelopes.common.domain.models.DateRange
import com.tubetoast.envelopes.common.domain.models.previousMonth
import com.tubetoast.envelopes.common.domain.models.previousYear
import com.tubetoast.envelopes.common.domain.models.yearAsRange
import com.tubetoast.envelopes.common.domain.snapshots.EnvelopeSnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class InflationCalculator(
    private val spendingCalculator: SpendingCalculator
) {

    suspend fun calculateInflationByMonths(
        filter: (EnvelopeSnapshot) -> Boolean = { true },
        today: Date = Date.today()
    ): Map<DateRange, Float> = withContext(Dispatchers.Default) {
        val spending = spendingCalculator.calculateSpendingSumByMonths(filter, today)

        spending.mapValues { (month, sum) ->
            spending[month.previousMonth()]?.let {
                calculateInflationRate(sum.units, it.units)
            } ?: 1f
        }
    }

    suspend fun calculateInflationByYears(
        filter: (EnvelopeSnapshot) -> Boolean = { true },
        today: Date = Date.today()
    ): List<Pair<DateRange, Float>> = withContext(Dispatchers.Default) {
        val inflationRates = mutableListOf<Pair<DateRange, Float>>()
        var year = today.yearAsRange()
        var previousYear = year.previousYear()
        var baseSum = getSum(previousYear, filter)

        while (baseSum > 0L) {
            val newSum = getSum(year, filter)
            inflationRates.add(0, year to calculateInflationRate(newSum, baseSum))
            year = previousYear
            previousYear = year.previousYear()
            baseSum = getSum(previousYear, filter)
        }

        inflationRates
    }

    suspend fun calculateInflation(
        baseRange: DateRange,
        newRange: DateRange,
        filter: (EnvelopeSnapshot) -> Boolean = { true }
    ): Float = withContext(Dispatchers.IO) {
        val baseSum = getSum(baseRange, filter)
        val newSum = getSum(newRange, filter)

        calculateInflationRate(newSum, baseSum)
    }

    private suspend fun getSum(
        range: DateRange,
        filter: (EnvelopeSnapshot) -> Boolean = { true }
    ): Long =
        spendingCalculator.calculateSpendingSum(filter) {
            it.date in range
        }.units

    private fun calculateInflationRate(newSum: Long, baseSum: Long) =
        (newSum - baseSum) / baseSum.toFloat()
}
