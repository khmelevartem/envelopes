package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Goal
import com.tubetoast.envelopes.common.domain.snapshots.CategorySnapshot
import com.tubetoast.envelopes.common.domain.snapshots.GoalSnapshot
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext

class GoalSnapshotInteractorImpl(
    private val linksRepository: CategoryToGoalLinksRepository,
    private val goalRepository: UpdatingGoalsRepository,
    private val categoryRepository: UpdatingCategoriesRepository,
    private val spendingRepository: UpdatingSpendingRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val scope: CoroutineScope = CoroutineScope(dispatcher)
) : GoalSnapshotInteractor {

    override val goalSnapshots: StateFlow<Set<GoalSnapshot>> = linksRepository.linksFlow.map { idsMap ->
        idsMap.mapTo(mutableSetOf()) { (goalId, categoriesIds) ->
            GoalSnapshot(
                goal = goalRepository.get(goalId)
                    ?: throw IllegalStateException("Cannot find Goal for existing goal id $goalId"),
                categories =
                categoriesIds.mapTo(mutableSetOf()) { id ->
                    CategorySnapshot(
                        category = categoryRepository.get(id)
                            ?: throw IllegalStateException("Cannot find Category for existing category id $id"),
                        transactions = spendingRepository.getCollection(id)
                    )
                }
            )
        }
    }.stateIn(scope, SharingStarted.Eagerly, emptySet())

    override suspend fun attachNewCategoriesToGoal(
        goal: Goal,
        vararg categories: Category
    ) {
        withContext(dispatcher) {
            linksRepository.attachNewCategoriesToGoal(
                goal.id,
                categories.mapTo(mutableSetOf()) { it.id }
            )
        }
    }

    override suspend fun detachCategoriesFromGoal(
        goal: Goal,
        vararg categories: Category
    ) {
        withContext(dispatcher) {
            linksRepository.detachCategoriesFromGoal(
                goal.id,
                categories.mapTo(mutableSetOf()) { it.id }
            )
        }
    }
}
