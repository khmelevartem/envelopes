package com.tubetoast.envelopes.common.domain.snapshots

import com.tubetoast.envelopes.common.domain.models.Amount
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.Transaction
import com.tubetoast.envelopes.common.domain.models.sum

data class EnvelopeSnapshot(
    val envelope: Envelope,
    val categories: Set<CategorySnapshot>
) {

    val transactions: List<Transaction<*>>
        get() = categories.flatMap {
            it.transactions
        }

    val sum: Amount
        get() = transactions.map(Transaction<*>::amount)
            .sum()

    val percentage: Float
        get() = if (envelope.limit.units != 0L) sum / envelope.limit else 0f

    val yearPercentage: Float
        get() = if (envelope.yearLimit.units != 0L) sum / envelope.yearLimit else 0f
}
