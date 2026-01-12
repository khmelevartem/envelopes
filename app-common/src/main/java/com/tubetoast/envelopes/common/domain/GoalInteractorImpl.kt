package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.domain.models.Goal
import com.tubetoast.envelopes.common.domain.models.Id
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GoalInteractorImpl(
    private val repository: UpdatingGoalsRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : GoalInteractor {

    override suspend fun getAll(): Set<Goal> {
        return withContext(dispatcher) {
            repository.getAll()
        }
    }

    override suspend fun getExactGoal(id: Id<Goal>): Goal? {
        return withContext(dispatcher) {
            repository.get(id)
        }
    }

    override suspend fun getGoalByName(name: String): Goal? {
        return withContext(dispatcher) {
            repository.getAll().find { it.name == name }
        }
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

    override suspend fun editGoal(old: Goal, new: Goal) {
        withContext(dispatcher) {
            repository.edit(old, new)
        }
    }
}
