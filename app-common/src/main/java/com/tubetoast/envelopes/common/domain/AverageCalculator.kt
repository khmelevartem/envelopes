package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.domain.models.Amount
import com.tubetoast.envelopes.common.domain.models.Date.Companion.toDate
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.sum
import com.tubetoast.envelopes.common.domain.snapshots.EnvelopeSnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Calendar

class AverageCalculator(
    private val snapshotsInteractor: SnapshotsInteractor
) {

    suspend fun calculateAverage(months: Int, filter: (EnvelopeSnapshot) -> Boolean = { true }): Amount =
        withContext(Dispatchers.IO) {
            val startDate = Calendar.getInstance().apply {
                add(Calendar.MONTH, -months)
            }.toDate()

            snapshotsInteractor.allSnapshots
                .filter(filter)
                .flatMap { it.categories }
                .flatMap { it.transactions }
                .filter { it.date > startDate }
                .map { it.amount }
                .sum()
                .units
                .div(months)
                .let { Amount(it) }
        }

    suspend fun calculateAverageForEnvelope(months: Int, envelope: Envelope): Amount =
        calculateAverage(months) {
            it.envelope == envelope
        }
}
