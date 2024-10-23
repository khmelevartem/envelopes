package com.tubetoast.envelopes.database.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

interface StandardDao<DE : DatabaseEntity> {

    fun getAll(): List<DE>

    fun getCollection(foreignKey: Int): List<DE>

    fun get(valueId: Int): DE?

    fun getByKey(primaryKey: Int): DE?

    fun write(databaseEntity: DE)

    fun delete(valueId: Int): Int

    fun deleteCollection(foreignKey: Int): Int

    fun update(newValue: DE): Int
}

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

    @Update
    abstract override fun update(newValue: EnvelopeEntity): Int
}

@Dao
abstract class CategoryDao : StandardDao<CategoryEntity> {

    @Query("SELECT * from categoryentity")
    abstract override fun getAll(): List<CategoryEntity>

    @Query("SELECT * from categoryentity WHERE foreignKey LIKE :foreignKey")
    abstract override fun getCollection(foreignKey: Int): List<CategoryEntity>

    @Query("SELECT * from categoryentity WHERE valueId LIKE :valueId")
    abstract override fun get(valueId: Int): CategoryEntity?

    @Query("SELECT * from categoryentity WHERE primaryKey LIKE :primaryKey")
    abstract override fun getByKey(primaryKey: Int): CategoryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract override fun write(databaseEntity: CategoryEntity)

    @Query("DELETE from categoryentity WHERE valueId Like :valueId")
    abstract override fun delete(valueId: Int): Int

    @Query("DELETE from categoryentity WHERE foreignKey LIKE :foreignKey")
    abstract override fun deleteCollection(foreignKey: Int): Int

    @Update
    abstract override fun update(newValue: CategoryEntity): Int
}

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

    @Update
    abstract override fun update(newValue: SpendingEntity): Int
}
