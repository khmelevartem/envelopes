package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.domain.models.Amount
import com.tubetoast.envelopes.common.domain.models.Date.Companion.toDate
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.sum
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Calendar

class AverageCalculator(
    private val snapshotsInteractor: SnapshotsInteractor
) {

    suspend fun calculateAverageForAll(months: Int): Amount = withContext(Dispatchers.IO) {
        val startDate = Calendar.getInstance().apply {
            add(Calendar.MONTH, -months)
        }.toDate()

        snapshotsInteractor.allSnapshots
            .flatMap { it.categories }
            .flatMap { it.transactions }
            .filter { it.date > startDate }
            .map { it.amount }
            .sum()
            .units
            .div(months)
            .let { Amount(it) }
    }

    suspend fun calculateAverageForEnvelope(months: Int, envelope: Envelope): Amount = withContext(Dispatchers.IO) {
        val startDate = Calendar.getInstance().apply {
            add(Calendar.MONTH, -months)
        }.toDate()

        snapshotsInteractor.allSnapshots
            .find { it.envelope == envelope }
            ?.categories
            ?.flatMap { it.transactions }
            ?.filter { it.date > startDate }
            ?.map { it.amount }
            ?.sum()
            ?.units
            ?.div(months)
            ?.let { Amount(it) }
            ?: Amount.ZERO
    }
}
