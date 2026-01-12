package com.tubetoast.envelopes.database.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.tubetoast.envelopes.database.data.SpendingEntity

@Dao
abstract class SpendingDao : StandardDao<SpendingEntity> {

    @Query("SELECT * from spendingentity")
    abstract override fun getAll(): List<SpendingEntity>

    @Query("SELECT * from spendingentity WHERE foreignKey LIKE :foreignKey")
    abstract override fun getCollection(foreignKey: Int): List<SpendingEntity>

    @Query("SELECT * from spendingentity WHERE valueId LIKE :valueId")
    abstract override fun get(valueId: Int): SpendingEntity?

    @Query("SELECT * from spendingentity WHERE primaryKey LIKE :primaryKey")
    abstract override fun getByKey(primaryKey: Int): SpendingEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract override fun write(databaseEntity: SpendingEntity)

    @Query("DELETE from spendingentity WHERE valueId Like :valueId")
    abstract override fun delete(valueId: Int): Int

    @Query("DELETE from spendingentity WHERE foreignKey LIKE :foreignKey")
    abstract override fun deleteCollection(foreignKey: Int): Int

    @Query("DELETE from spendingentity")
    abstract override fun deleteAll(): Int

    @Update
    abstract override fun update(newValue: SpendingEntity): Int
}
