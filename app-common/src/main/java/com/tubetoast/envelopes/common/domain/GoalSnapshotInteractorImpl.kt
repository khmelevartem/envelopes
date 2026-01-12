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
    private val spendingRepository: UpdatingSpendingRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val scope: CoroutineScope = CoroutineScope(dispatcher)
) : GoalSnapshotInteractor {

    override val goalSnapshots: StateFlow<Set<GoalSnapshot>> = linksRepository.linksFlow.map { map ->
        map.mapTo(mutableSetOf()) { (goal, categories) ->
            GoalSnapshot(
                goal = goal,
                categories =
                categories.mapTo(mutableSetOf()) { category ->
                    CategorySnapshot(
                        category = category,
                        transactions = spendingRepository.getCollection(category.id)
                            .filterTo(mutableSetOf()) { transaction ->
                                (goal.start?.let { transaction.date > it } ?: true) &&
                                    (goal.finish?.let { transaction.date < it } ?: true)
                            }
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
                goal,
                categories.mapTo(mutableSetOf()) { it }
            )
        }
    }

    override suspend fun detachCategoriesFromGoal(
        goal: Goal,
        vararg categories: Category
    ) {
        withContext(dispatcher) {
            linksRepository.detachCategoriesFromGoal(
                goal,
                categories.mapTo(mutableSetOf()) { it }
            )
        }
    }
}
