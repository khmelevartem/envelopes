package com.tubetoast.envelopes.database.data

import com.tubetoast.envelopes.common.domain.GoalSnapshotRepository
import com.tubetoast.envelopes.common.domain.models.Goal
import com.tubetoast.envelopes.common.domain.snapshots.CategorySnapshot
import com.tubetoast.envelopes.common.domain.snapshots.GoalSnapshot
import com.tubetoast.envelopes.database.data.dao.GoalSnapshotDao
import com.tubetoast.envelopes.database.data.relations.CategoryWithSpending
import com.tubetoast.envelopes.database.data.relations.GoalWithCategories
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GoalSnapshotDatabaseRepository(
    private val dao: GoalSnapshotDao,
    private val goalConverter: GoalConverter,
    private val categoryConverter: CategoryConverter,
    private val spendingConverter: SpendingConverter
) : GoalSnapshotRepository {
    override fun getSnapshotsFlow(): Flow<Set<GoalSnapshot>> =
        dao.getGoalSnapshotsFlow().map { entities ->
            entities.map { it.toSnapshot() }.toSet()
        }

    private fun GoalWithCategories.toSnapshot(): GoalSnapshot {
        val domainGoal = goalConverter.toDomainModel(goal)

        return GoalSnapshot(
            goal = domainGoal,
            categories = categories.map { it.toSnapshot(domainGoal) }.toSet()
        )
    }

    private fun CategoryWithSpending.toSnapshot(goal: Goal): CategorySnapshot =
        CategorySnapshot(
            category = categoryConverter.toDomainModel(category),
            transactions = spending
                .map { spendingConverter.toDomainModel(it) }
                .filter { transaction ->
                    (goal.start?.let { transaction.date > it } ?: true) &&
                        (goal.finish?.let { transaction.date < it } ?: true)
                }.toSet()
        )
}
