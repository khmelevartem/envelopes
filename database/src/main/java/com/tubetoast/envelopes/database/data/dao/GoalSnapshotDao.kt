package com.tubetoast.envelopes.database.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.tubetoast.envelopes.database.data.relations.GoalWithCategories
import kotlinx.coroutines.flow.Flow

@Dao
interface GoalSnapshotDao {
    @Transaction
    @Query("SELECT * FROM GoalEntity")
    fun getGoalSnapshotsFlow(): Flow<List<GoalWithCategories>>
}
