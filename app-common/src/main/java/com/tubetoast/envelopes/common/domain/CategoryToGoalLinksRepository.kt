package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Goal
import com.tubetoast.envelopes.common.domain.models.Id
import kotlinx.coroutines.flow.StateFlow

interface CategoryToGoalLinksRepository {
    val linksFlow: StateFlow<Map<Id<Goal>, Set<Id<Category>>>>
    suspend fun attachNewCategoriesToGoal(goal: Id<Goal>, categories: Set<Id<Category>>)
    suspend fun detachCategoriesFromGoal(goal: Id<Goal>, categories: Set<Id<Category>>)
}
