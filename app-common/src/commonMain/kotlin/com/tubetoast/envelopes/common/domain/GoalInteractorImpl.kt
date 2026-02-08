package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.domain.models.Goal
import com.tubetoast.envelopes.common.domain.models.Id
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class GoalInteractorImpl(
    private val repository: GoalsRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : GoalInteractor {
    override suspend fun getAll(): Set<Goal> =
        withContext(dispatcher) {
            repository.getAll()
        }

    override suspend fun getExactGoal(id: Id<Goal>): Goal? =
        withContext(dispatcher) {
            repository.get(id)
        }

    override suspend fun getGoalByName(name: String): Goal? =
        withContext(dispatcher) {
            repository.getAll().find { it.name == name }
        }

    override suspend fun addGoal(goal: Goal) {
        withContext(dispatcher) {
            repository.put(goal)
        }
    }

    override suspend fun deleteGoal(goal: Goal) {
        withContext(dispatcher) {
            repository.delete(goal)
        }
    }

    override suspend fun editGoal(
        old: Goal,
        new: Goal
    ) {
        withContext(dispatcher) {
            repository.edit(old, new)
        }
    }
}
