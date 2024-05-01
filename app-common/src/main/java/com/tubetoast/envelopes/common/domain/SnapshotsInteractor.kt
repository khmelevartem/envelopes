package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.domain.models.DateRange
import com.tubetoast.envelopes.common.domain.snapshots.EnvelopeSnapshot
import kotlinx.coroutines.flow.Flow

interface SnapshotsInteractor {
    val allSnapshots: Set<EnvelopeSnapshot>
    val allSnapshotsFlow: Flow<Set<EnvelopeSnapshot>>
    fun snapshotsByDatesFlow(dateRange: DateRange): Flow<Set<EnvelopeSnapshot>>
}
