package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Goal
import com.tubetoast.envelopes.common.domain.snapshots.GoalSnapshot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext

class GoalSnapshotInteractorImpl(
    private val repository: GoalSnapshotRepository,
    private val linksRepository: CategoryToGoalLinksRepository // Kept for write operations
) : GoalSnapshotInteractor {

    override val goalSnapshots: StateFlow<Set<GoalSnapshot>> =
        repository.getSnapshotsFlow()
            .stateIn(
                scope = CoroutineScope(Dispatchers.IO),
                started = SharingStarted.Eagerly,
                initialValue = emptySet()
            )

    override suspend fun attachNewCategoriesToGoal(goal: Goal, vararg categories: Category) {
        withContext(Dispatchers.IO) {
            linksRepository.attachNewCategoriesToGoal(goal, categories.toSet())
        }
    }

    override suspend fun detachCategoriesFromGoal(goal: Goal, vararg categories: Category) {
        withContext(Dispatchers.IO) {
            linksRepository.detachCategoriesFromGoal(goal, categories.toSet())
        }
    }
}
