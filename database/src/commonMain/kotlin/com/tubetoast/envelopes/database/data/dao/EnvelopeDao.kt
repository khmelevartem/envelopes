package com.tubetoast.envelopes.database.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.tubetoast.envelopes.database.data.EnvelopeEntity

@Dao
abstract class EnvelopeDao : StandardDao<EnvelopeEntity> {
    @Query("SELECT * from envelopeentity")
    abstract override suspend fun getAll(): List<EnvelopeEntity>

    override suspend fun getCollection(foreignKey: Int): List<EnvelopeEntity> = emptyList()

    @Query("SELECT * from envelopeentity WHERE valueId LIKE :valueId")
    abstract override suspend fun get(valueId: Int): EnvelopeEntity?

    @Query("SELECT * from envelopeentity WHERE primaryKey LIKE :primaryKey")
    abstract override suspend fun getByKey(primaryKey: Int): EnvelopeEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract override suspend fun write(databaseEntity: EnvelopeEntity)

    @Query("DELETE from envelopeentity WHERE valueId Like :valueId")
    abstract override suspend fun delete(valueId: Int): Int

    @Query("DELETE from envelopeentity WHERE foreignKey LIKE :foreignKey")
    abstract override suspend fun deleteCollection(foreignKey: Int): Int

    @Query("DELETE from envelopeentity")
    abstract override suspend fun deleteAll(): Int

    @Update
    abstract override suspend fun update(newValue: EnvelopeEntity): Int
}
