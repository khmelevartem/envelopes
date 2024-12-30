package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.domain.models.DateRange
import com.tubetoast.envelopes.common.domain.models.sum
import com.tubetoast.envelopes.common.domain.snapshots.EnvelopeSnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class InflationCalculator(
    private val snapshotsInteractor: SnapshotsInteractor
) {
    suspend fun calculateInflation(
        baseRange: DateRange,
        newRange: DateRange,
        filter: (EnvelopeSnapshot) -> Boolean = { true }
    ): Float = withContext(Dispatchers.IO) {
        val baseSum = getSum(filter, baseRange)
        val newSum = getSum(filter, newRange)

        (newSum - baseSum) / baseSum.toFloat()
    }

    private fun getSum(
        filter: (EnvelopeSnapshot) -> Boolean,
        baseRange: DateRange
    ) = snapshotsInteractor.allSnapshots
        .filter(filter)
        .flatMap { it.categories }
        .flatMap { it.transactions }
        .filter { it.date in baseRange }
        .map { it.amount }
        .sum()
        .units
}
