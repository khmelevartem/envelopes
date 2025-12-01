package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.domain.models.Goal
import com.tubetoast.envelopes.common.domain.models.Id
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class GoalInteractorImpl : GoalInteractor {
    override val goalsFlow = MutableStateFlow(emptySet<Goal>())

    override suspend fun getAll(): Set<Goal> {
        return goalsFlow.value
    }

    override suspend fun getExactGoal(id: Id<Goal>): Goal? {
        return goalsFlow.value.find { it.id == id }
    }

    override suspend fun getGoalByName(name: String): Goal? {
        return goalsFlow.value.find { it.name == name }
    }

    override suspend fun addGoal(goal: Goal) {
        goalsFlow.update {
            it + goal
        }
    }

    override suspend fun deleteGoal(goal: Goal) {
        goalsFlow.update {
            it - goal
        }
    }

    override suspend fun editGoal(old: Goal, new: Goal) {
        goalsFlow.update {
            if (it.contains(old)) it - old + new else it
        }
    }
}
