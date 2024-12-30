package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.domain.models.Amount
import com.tubetoast.envelopes.common.domain.models.Date
import com.tubetoast.envelopes.common.domain.models.Date.Companion.toCalendar
import com.tubetoast.envelopes.common.domain.models.Date.Companion.toDate
import com.tubetoast.envelopes.common.domain.models.sum
import com.tubetoast.envelopes.common.domain.snapshots.EnvelopeSnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Calendar

class AverageCalculator(
    private val snapshotsInteractor: SnapshotsInteractor
) {

    suspend fun calculateAverage(
        months: Int,
        filter: (EnvelopeSnapshot) -> Boolean = { true },
        today: Date = Date.today()
    ): Amount =
        withContext(Dispatchers.IO) {
            val startDate = today.toCalendar().apply {
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
}
