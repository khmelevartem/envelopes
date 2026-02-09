package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.domain.models.DateRange
import com.tubetoast.envelopes.common.domain.snapshots.EnvelopeSnapshot
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

// In commonTest: Fakes.kt
class FakeSnapshotsInteractor(
    var snapshots: Set<EnvelopeSnapshot> = emptySet()
) : SnapshotsInteractor {
    override val allEnvelopeSnapshots: Set<EnvelopeSnapshot> get() = snapshots
    override val allEnvelopeSnapshotsFlow: Flow<Set<EnvelopeSnapshot>> = MutableStateFlow(snapshots)

    override fun envelopeSnapshots(period: Flow<DateRange>): Flow<Set<EnvelopeSnapshot>> {
        TODO("Not yet implemented")
    }
}
