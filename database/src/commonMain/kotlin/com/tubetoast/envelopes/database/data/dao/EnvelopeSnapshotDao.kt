package com.tubetoast.envelopes.database.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.tubetoast.envelopes.database.data.relations.EnvelopeWithCategories
import kotlinx.coroutines.flow.Flow

@Dao
interface EnvelopeSnapshotDao {
    @Transaction
    @Query("SELECT * FROM EnvelopeEntity")
    fun getFullSnapshotsFlow(): Flow<List<EnvelopeWithCategories>>
}
