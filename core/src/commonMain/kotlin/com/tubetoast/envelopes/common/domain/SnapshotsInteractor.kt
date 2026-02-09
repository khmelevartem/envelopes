package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.domain.models.DateRange
import com.tubetoast.envelopes.common.domain.snapshots.EnvelopeSnapshot
import kotlinx.coroutines.flow.Flow

interface SnapshotsInteractor {
    val allEnvelopeSnapshots: Set<EnvelopeSnapshot>
    val allEnvelopeSnapshotsFlow: Flow<Set<EnvelopeSnapshot>>

    fun envelopeSnapshots(period: Flow<DateRange>): Flow<Set<EnvelopeSnapshot>>
}
