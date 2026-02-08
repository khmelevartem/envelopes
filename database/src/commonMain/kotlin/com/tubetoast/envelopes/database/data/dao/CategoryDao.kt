package com.tubetoast.envelopes.database.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.tubetoast.envelopes.database.data.CategoryEntity

@Dao
abstract class CategoryDao : StandardDao<CategoryEntity> {
    @Query("SELECT * from categoryentity")
    abstract override suspend fun getAll(): List<CategoryEntity>

    @Query("SELECT * from categoryentity WHERE valueId LIKE :valueId")
    abstract override suspend fun get(valueId: Int): CategoryEntity?

    @Query("SELECT * from categoryentity WHERE primaryKey LIKE :primaryKey")
    abstract override suspend fun getByKey(primaryKey: Int): CategoryEntity?

    @Query("SELECT * from categoryentity WHERE foreignKey LIKE :foreignKey")
    abstract override suspend fun getCollection(foreignKey: Int): List<CategoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract override suspend fun write(databaseEntity: CategoryEntity)

    @Query("DELETE from categoryentity WHERE valueId Like :valueId")
    abstract override suspend fun delete(valueId: Int): Int

    @Query("DELETE from categoryentity WHERE foreignKey LIKE :foreignKey")
    abstract override suspend fun deleteCollection(foreignKey: Int): Int

    @Query("DELETE from categoryentity")
    abstract override suspend fun deleteAll(): Int

    @Update
    abstract override suspend fun update(newValue: CategoryEntity): Int
}
