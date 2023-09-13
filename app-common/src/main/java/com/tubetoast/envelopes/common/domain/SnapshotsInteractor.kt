package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.domain.snapshots.EnvelopeSnapshot
import kotlinx.coroutines.flow.Flow

interface SnapshotsInteractor {
    val envelopeSnapshot: Set<EnvelopeSnapshot>
    val envelopeSnapshotFlow: Flow<Set<EnvelopeSnapshot>>
}
