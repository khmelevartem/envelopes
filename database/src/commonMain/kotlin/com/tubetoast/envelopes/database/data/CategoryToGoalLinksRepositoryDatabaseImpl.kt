package com.tubetoast.envelopes.database.data

import com.tubetoast.envelopes.common.domain.CategoryToGoalLinksRepository
import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Goal
import com.tubetoast.envelopes.database.data.dao.CategoryDao
import com.tubetoast.envelopes.database.data.dao.CategoryToGoalLinksDao
import com.tubetoast.envelopes.database.data.dao.GoalDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CategoryToGoalLinksRepositoryDatabaseImpl(
    private val dao: CategoryToGoalLinksDao,
    private val categoryDao: CategoryDao,
    private val categoryConverter: CategoryConverter,
    private val goalDao: GoalDao,
    private val goalConverter: GoalConverter
) : CategoryToGoalLinksRepository {
    override val linksFlow: Flow<Map<Goal, Set<Category>>> = dao.getAll().map {
        val map = mutableMapOf<Goal, MutableSet<Category>>()
        it.forEach { (_, categoryKey, goalKey) ->
            val goal = goalDao.getByKey(goalKey)?.let { goalEntity ->
                goalConverter.toDomainModel(goalEntity)
            } ?: throw IllegalStateException("Cannot find Goal for existing goal key $goalKey")

            val category = categoryDao.getByKey(categoryKey)?.let { categoryEntity ->
                categoryConverter.toDomainModel(categoryEntity)
            } ?: throw IllegalStateException("Cannot find Category for existing category key $categoryKey")

            map.getOrPut(goal) { mutableSetOf() }.add(category)
        }
        map
    }

    override suspend fun attachNewCategoriesToGoal(
        goal: Goal,
        categories: Set<Category>
    ) {
        forEachCategory(goal, categories) { goalKey, categoryKey ->
            if (dao.get(goalKey = goalKey, categoryKey = categoryKey) == null) {
                dao.write(CategoryToGoalLinkEntity(categoryKey = categoryKey, goalKey = goalKey))
            }
        }
    }

    override suspend fun detachCategoriesFromGoal(
        goal: Goal,
        categories: Set<Category>
    ) {
        forEachCategory(goal, categories) { goalKey, categoryKey ->
            dao.delete(goalKey = goalKey, categoryKey = categoryKey)
        }
    }

    private suspend fun forEachCategory(
        goal: Goal,
        categories: Set<Category>,
        action: suspend (goalKey: Int, categoryKey: Int) -> Unit
    ) {
        val goalKey = goalDao.get(goal.id.code)?.primaryKey
            ?: throw IllegalStateException("Trying to write unexisting goal $goal")
        categories.forEach {
            val categoryKey = categoryDao.get(it.id.code)?.primaryKey
                ?: throw IllegalStateException("Trying to write unexisting category $it")

            action(goalKey, categoryKey)
        }
    }
}
