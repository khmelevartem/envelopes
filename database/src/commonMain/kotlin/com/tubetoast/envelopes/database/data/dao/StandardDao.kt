package com.tubetoast.envelopes.database.data.dao

import androidx.room.Update
import com.tubetoast.envelopes.database.data.DatabaseEntity

interface StandardDao<DE : DatabaseEntity> {
    suspend fun getAll(): List<DE>

    suspend fun getCollection(foreignKey: Int): List<DE>

    suspend fun get(valueId: Int): DE?

    suspend fun getByKey(primaryKey: Int): DE?

    suspend fun write(databaseEntity: DE)

    suspend fun delete(valueId: Int): Int

    suspend fun deleteCollection(foreignKey: Int): Int

    suspend fun deleteAll(): Int

    @Update
    suspend fun update(newValue: DE): Int
}
