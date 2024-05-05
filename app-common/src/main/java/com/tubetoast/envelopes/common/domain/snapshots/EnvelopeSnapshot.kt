package com.tubetoast.envelopes.common.domain.snapshots

import com.tubetoast.envelopes.common.domain.models.Amount
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.Transaction
import com.tubetoast.envelopes.common.domain.models.sum

data class EnvelopeSnapshot(
    val envelope: Envelope,
    val categories: Set<CategorySnapshot>
) {

    val transactions: List<Amount>
        get() = categories.flatMap {
            it.transactions.map(Transaction<*>::amount)
        }

    val sum: Amount
        get() = transactions.sum()

    val percentage: Float
        get() = if (envelope.limit.units != 0) sum / envelope.limit else 0f

    val yearPercentage: Float
        get() = if (envelope.yearLimit.units != 0) sum / envelope.yearLimit else 0f
}
