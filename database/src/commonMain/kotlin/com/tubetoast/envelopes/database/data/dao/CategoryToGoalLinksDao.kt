package com.tubetoast.envelopes.database.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.tubetoast.envelopes.database.data.CategoryToGoalLinkEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class CategoryToGoalLinksDao {
    @Query("SELECT * from categorytogoallinkentity")
    abstract fun getAll(): Flow<List<CategoryToGoalLinkEntity>>

    @Query("SELECT * from categorytogoallinkentity WHERE goalKey LIKE :goalKey AND categoryKey LIKE :categoryKey")
    abstract suspend fun get(
        goalKey: Int,
        categoryKey: Int
    ): CategoryToGoalLinkEntity?

    @Query("SELECT * from categorytogoallinkentity WHERE goalKey LIKE :goalKey")
    abstract suspend fun getForGoal(goalKey: Int): CategoryToGoalLinkEntity?

    @Query("SELECT * from categorytogoallinkentity WHERE categoryKey LIKE :categoryKey")
    abstract suspend fun getForCategory(categoryKey: Int): CategoryToGoalLinkEntity?

    @Insert
    abstract suspend fun write(link: CategoryToGoalLinkEntity)

    @Query("DELETE from categorytogoallinkentity WHERE goalKey LIKE :goalKey AND categoryKey LIKE :categoryKey")
    abstract suspend fun delete(
        goalKey: Int,
        categoryKey: Int
    )
}
