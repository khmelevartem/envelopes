package com.tubetoast.envelopes.database.data

import com.tubetoast.envelopes.common.domain.CategoryToGoalLinksRepository
import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Goal
import com.tubetoast.envelopes.common.domain.models.Id
import kotlinx.coroutines.flow.StateFlow

class CategoryToGoalLinksRepositoryDatabaseImpl : CategoryToGoalLinksRepository {
    override val linksFlow: StateFlow<Map<Id<Goal>, Set<Id<Category>>>>
        get() = TODO("Not yet implemented")

    override suspend fun attachNewCategoriesToGoal(
        goal: Id<Goal>,
        categories: Set<Id<Category>>
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun detachCategoriesFromGoal(
        goal: Id<Goal>,
        categories: Set<Id<Category>>
    ) {
        TODO("Not yet implemented")
    }
}
