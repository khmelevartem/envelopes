package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Goal
import kotlinx.coroutines.flow.Flow

interface CategoryToGoalLinksRepository {
    val linksFlow: Flow<Map<Goal, Set<Category>>>

    suspend fun attachNewCategoriesToGoal(
        goal: Goal,
        categories: Set<Category>
    )

    suspend fun detachCategoriesFromGoal(
        goal: Goal,
        categories: Set<Category>
    )
}
