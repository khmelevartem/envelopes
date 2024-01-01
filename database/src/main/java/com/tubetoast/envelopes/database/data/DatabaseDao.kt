package com.tubetoast.envelopes.database.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
abstract class EnvelopeDao {

    @Query("SELECT * from envelopeentity")
    abstract fun getAll(): List<EnvelopeEntity>

    @Query("SELECT * from envelopeentity WHERE primaryKey LIKE :key")
    abstract fun get(key: Int): EnvelopeEntity?

    @Insert
    abstract fun write(databaseEntity: EnvelopeEntity)

    @Query("DELETE from envelopeentity WHERE primaryKey Like :key")
    abstract fun delete(key: Int)
}

@Dao
abstract class CategoryDao {

    @Query("SELECT * from categoryentity")
    abstract fun getAll(): List<CategoryEntity>

    @Query("SELECT * from categoryentity WHERE foreignKey LIKE :parentKey")
    abstract fun getCollection(parentKey: Int): List<CategoryEntity>

    @Query("SELECT * from categoryentity WHERE primaryKey LIKE :key")
    abstract fun get(key: Int): CategoryEntity?

    @Insert
    abstract fun write(databaseEntity: CategoryEntity)

    @Query("DELETE from categoryentity WHERE primaryKey Like :key")
    abstract fun delete(key: Int)
}

@Dao
abstract class SpendingDao {

    @Query("SELECT * from spendingentity")
    abstract fun getAll(): List<SpendingEntity>

    @Query("SELECT * from spendingentity WHERE foreignKey LIKE :parentKey")
    abstract fun getCollection(parentKey: Int): List<SpendingEntity>

    @Query("SELECT * from spendingentity WHERE primaryKey LIKE :key")
    abstract fun get(key: Int): SpendingEntity?

    @Insert
    abstract fun write(databaseEntity: SpendingEntity)

    @Query("DELETE from spendingentity WHERE primaryKey Like :key")
    abstract fun delete(key: Int)
}