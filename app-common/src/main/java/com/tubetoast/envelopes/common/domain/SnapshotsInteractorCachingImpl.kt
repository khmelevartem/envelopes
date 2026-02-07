package com.tubetoast.envelopes.common.domain

import SnapshotsInteractorImpl
import com.tubetoast.envelopes.common.domain.snapshots.EnvelopeSnapshot

class SnapshotsInteractorCachingImpl(
    envelopeSnapshotRepository: EnvelopeSnapshotRepository
) : SnapshotsInteractorImpl(envelopeSnapshotRepository) {

    private var isOutdated
        get() = snapshotsCache == null
        set(value) {
            if (value) snapshotsCache = null
        }

    private var snapshotsCache: Set<EnvelopeSnapshot>? = null

    @Suppress("RecursivePropertyAccessor")
    override val allEnvelopeSnapshots: Set<EnvelopeSnapshot>
        get() {
            if (isOutdated) {
                snapshotsCache = super.allEnvelopeSnapshots
            }
            return snapshotsCache ?: allEnvelopeSnapshots
        }
}
