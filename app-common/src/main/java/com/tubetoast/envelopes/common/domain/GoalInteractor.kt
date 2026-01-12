package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.domain.models.Goal
import com.tubetoast.envelopes.common.domain.models.Id

interface GoalInteractor {
    suspend fun getAll(): Set<Goal>
    suspend fun getExactGoal(id: Id<Goal>): Goal?
    suspend fun getGoalByName(name: String): Goal?
    suspend fun addGoal(goal: Goal)
    suspend fun deleteGoal(goal: Goal)
    suspend fun editGoal(old: Goal, new: Goal)
}
