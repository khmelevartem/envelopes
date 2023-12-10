package com.tubetoast.envelopes.common.domain.snapshots

import com.tubetoast.envelopes.common.domain.models.Amount
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.Transaction
import com.tubetoast.envelopes.common.domain.models.sum

data class EnvelopeSnapshot(
    val envelope: Envelope,
    val categories: Set<CategorySnapshot>,
) {

    val spending: List<Amount>
        get() = categories.flatMap {
            it.transactions.map(Transaction::amount)
        }

    val sum: Amount
        get() = spending.sum()

    val percentage: Float
        get() = sum / envelope.limit
}
