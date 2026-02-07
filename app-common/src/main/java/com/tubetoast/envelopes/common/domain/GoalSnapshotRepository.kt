package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.domain.snapshots.GoalSnapshot
import kotlinx.coroutines.flow.Flow

interface GoalSnapshotRepository {
    fun getSnapshotsFlow(): Flow<Set<GoalSnapshot>>
}
