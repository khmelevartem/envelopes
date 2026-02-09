package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.domain.snapshots.EnvelopeSnapshot
import kotlinx.coroutines.flow.Flow

interface EnvelopeSnapshotRepository {
    fun getSnapshotsFlow(): Flow<Set<EnvelopeSnapshot>>
}
