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
    abstract fun get(goalKey: Int, categoryKey: Int): CategoryToGoalLinkEntity?

    @Query("SELECT * from categorytogoallinkentity WHERE goalKey LIKE :goalKey")
    abstract fun getForGoal(goalKey: Int): CategoryToGoalLinkEntity?

    @Query("SELECT * from categorytogoallinkentity WHERE categoryKey LIKE :categoryKey")
    abstract fun getForCategory(categoryKey: Int): CategoryToGoalLinkEntity?

    @Insert
    abstract fun write(link: CategoryToGoalLinkEntity)

    @Query("DELETE from categorytogoallinkentity WHERE goalKey LIKE :goalKey AND categoryKey LIKE :categoryKey")
    abstract fun delete(goalKey: Int, categoryKey: Int)
}
