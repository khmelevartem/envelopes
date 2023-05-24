@file:Suppress("FunctionName")

package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.domain.models.*
import com.tubetoast.envelopes.common.domain.snapshots.CategorySnapshot
import com.tubetoast.envelopes.common.domain.snapshots.EnvelopeSnapshot
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf


class SampleEnvelopesInteractor(
    override val envelopeSnapshot: Set<EnvelopeSnapshot> = create()
) : EnvelopesInteractor {
    override val envelopeSnapshotFlow: Flow<Set<EnvelopeSnapshot>> = flowOf(envelopeSnapshot)
}

fun create() = setOf(
    envelope("first", Amount(170),
        category("food", null,
            Spending(Amount(20), randomDate()),
            Spending(Amount(4), randomDate())
        ),
        category("drive", Amount(90),
            Spending(Amount(42), randomDate()),
            Spending(Amount(43), randomDate()),
            Spending(Amount(41), randomDate()),
        )
    ),
    envelope("second", Amount(20),
        category("web", null,
            Spending(Amount(2), randomDate()),
            Spending(Amount(4), randomDate())
        ),
        category("study", null,
            Spending(Amount(1), randomDate()),
            Spending(Amount(3), randomDate()),
            Spending(Amount(1), randomDate())
        )
    )
)


fun envelope(
    name: String,
    limit: Amount,
    producer: MutableSet<CategorySnapshot>.() -> Unit
) = EnvelopeSnapshot(Envelope(name, limit), mutableSetOf<CategorySnapshot>().apply { producer() })

fun envelope(
    name: String,
    limit: Amount,
    vararg categories: CategorySnapshot
) = EnvelopeSnapshot(Envelope(name, limit), setOf(*categories))

fun category(
    name: String,
    limit: Amount? = null,
    producer: MutableSet<Spending>.() -> Unit
) = CategorySnapshot(Category(name, limit), mutableSetOf<Spending>().apply { producer() })


fun category(
    name: String,
    limit: Amount? = null,
    vararg spending: Spending
) = CategorySnapshot(Category(name, limit), setOf(*spending))