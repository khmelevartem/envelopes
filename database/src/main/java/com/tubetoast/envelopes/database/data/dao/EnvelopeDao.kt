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
    abstract override fun getAll(): List<EnvelopeEntity>

    override fun getCollection(foreignKey: Int): List<EnvelopeEntity> =
        emptyList()

    @Query("SELECT * from envelopeentity WHERE valueId LIKE :valueId")
    abstract override fun get(valueId: Int): EnvelopeEntity?

    @Query("SELECT * from envelopeentity WHERE primaryKey LIKE :primaryKey")
    abstract override fun getByKey(primaryKey: Int): EnvelopeEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract override fun write(databaseEntity: EnvelopeEntity)

    @Query("DELETE from envelopeentity WHERE valueId Like :valueId")
    abstract override fun delete(valueId: Int): Int

    @Query("DELETE from envelopeentity WHERE foreignKey LIKE :foreignKey")
    abstract override fun deleteCollection(foreignKey: Int): Int

    @Query("DELETE from envelopeentity")
    abstract override fun deleteAll(): Int

    @Update
    abstract override fun update(newValue: EnvelopeEntity): Int
}
