package com.tubetoast.envelopes.database.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

interface StandardDao<DE : DatabaseEntity> {

    fun getAll(): List<DE>

    fun getCollection(parentKey: Int): List<DE>

    fun get(valueId: Int): DE?

    fun write(databaseEntity: DE)

    fun delete(valueId: Int): Int

    fun deleteCollection(parentKey: Int): Int

//    fun update(oldValueId: Int, value: M): Boolean
}

@Dao
abstract class EnvelopeDao : StandardDao<EnvelopeEntity> {

    @Query("SELECT * from envelopeentity")
    abstract override fun getAll(): List<EnvelopeEntity>

    override fun getCollection(parentKey: Int): List<EnvelopeEntity> =
        emptyList()

    @Query("SELECT * from envelopeentity WHERE primaryKey LIKE :valueId")
    abstract override fun get(valueId: Int): EnvelopeEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract override fun write(databaseEntity: EnvelopeEntity)

    @Query("DELETE from envelopeentity WHERE primaryKey Like :valueId")
    abstract override fun delete(valueId: Int): Int

    @Query("DELETE from envelopeentity WHERE foreignKey LIKE :parentKey")
    abstract override fun deleteCollection(parentKey: Int): Int
//

//    @Query("UPDATE envelopeentity SET value = :value WHERE primaryKey LIKE :oldValueId")
//    abstract override fun update(oldValueId: Int, value: Envelope): Boolean
}

@Dao
abstract class CategoryDao : StandardDao<CategoryEntity> {

    @Query("SELECT * from categoryentity")
    abstract override fun getAll(): List<CategoryEntity>

    @Query("SELECT * from categoryentity WHERE foreignKey LIKE :parentKey")
    abstract override fun getCollection(parentKey: Int): List<CategoryEntity>

    @Query("SELECT * from categoryentity WHERE primaryKey LIKE :valueId")
    abstract override fun get(valueId: Int): CategoryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract override fun write(databaseEntity: CategoryEntity)

    @Query("DELETE from categoryentity WHERE primaryKey Like :valueId")
    abstract override fun delete(valueId: Int): Int

    @Query("DELETE from categoryentity WHERE foreignKey LIKE :parentKey")
    abstract override fun deleteCollection(parentKey: Int): Int
//
//    @Query("UPDATE categoryentity SET value = :value WHERE primaryKey LIKE :oldValueId")
//    abstract override fun update(oldValueId: Int, value: Category): Boolean
}

@Dao
abstract class SpendingDao : StandardDao<SpendingEntity> {

    @Query("SELECT * from spendingentity")
    abstract override fun getAll(): List<SpendingEntity>

    @Query("SELECT * from spendingentity WHERE foreignKey LIKE :parentKey")
    abstract override fun getCollection(parentKey: Int): List<SpendingEntity>

    @Query("SELECT * from spendingentity WHERE primaryKey LIKE :valueId")
    abstract override fun get(valueId: Int): SpendingEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract override fun write(databaseEntity: SpendingEntity)

    @Query("DELETE from spendingentity WHERE primaryKey Like :valueId")
    abstract override fun delete(valueId: Int): Int

    @Query("DELETE from spendingentity WHERE foreignKey LIKE :parentKey")
    abstract override fun deleteCollection(parentKey: Int): Int
//
//    @Query("UPDATE spendingentity SET value = :value WHERE primaryKey LIKE :oldValueId")
//    abstract override fun update(oldValueId: Int, value: Spending): Boolean
}