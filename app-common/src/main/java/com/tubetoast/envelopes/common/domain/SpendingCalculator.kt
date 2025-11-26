package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.domain.models.Amount
import com.tubetoast.envelopes.common.domain.models.Date
import com.tubetoast.envelopes.common.domain.models.DateRange
import com.tubetoast.envelopes.common.domain.models.Transaction
import com.tubetoast.envelopes.common.domain.models.sum
import com.tubetoast.envelopes.common.domain.snapshots.EnvelopeSnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SpendingCalculator(
    private val snapshotsInteractor: SnapshotsInteractor,
    private val spendingInteractor: SpendingInteractor
) {
    suspend fun calculateSpendingSum(
        envelopeFilter: (EnvelopeSnapshot) -> Boolean = { true },
        transactionFilter: (Transaction<*>) -> Boolean = { true }
    ): Amount = withContext(Dispatchers.Default) {
        snapshotsInteractor.allEnvelopeSnapshots
            .calculateSum(envelopeFilter, transactionFilter)
    }

    suspend fun calculateSpendingSumByMonths(
        envelopeFilter: (EnvelopeSnapshot) -> Boolean = { true },
        today: Date = Date.today()
    ): Map<DateRange, Amount> = withContext(Dispatchers.Default) {
        spendingInteractor.getMonthsOfSpending(today).associateWith {
            snapshotsInteractor.allEnvelopeSnapshots
                .calculateSum(it, envelopeFilter)
        }
    }

    private fun Iterable<EnvelopeSnapshot>.calculateSum(
        range: DateRange,
        envelopeFilter: (EnvelopeSnapshot) -> Boolean = { true }
    ) = calculateSum(envelopeFilter) { it.date in range }

    private fun Iterable<EnvelopeSnapshot>.calculateSum(
        envelopeFilter: (EnvelopeSnapshot) -> Boolean = { true },
        transactionFilter: (Transaction<*>) -> Boolean = { true }
    ) = asSequence()
        .filter(envelopeFilter)
        .flatMap { it.categories }
        .flatMap { it.transactions }
        .filter(transactionFilter)
        .map { it.amount }
        .sum()
}
