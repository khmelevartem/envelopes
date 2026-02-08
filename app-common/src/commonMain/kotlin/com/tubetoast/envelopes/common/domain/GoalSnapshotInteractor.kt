package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Goal
import com.tubetoast.envelopes.common.domain.snapshots.GoalSnapshot
import kotlinx.coroutines.flow.StateFlow

interface GoalSnapshotInteractor {
    val goalSnapshots: StateFlow<Set<GoalSnapshot>>

    suspend fun attachNewCategoriesToGoal(
        goal: Goal,
        vararg categories: Category
    )

    suspend fun detachCategoriesFromGoal(
        goal: Goal,
        vararg categories: Category
    )
}
