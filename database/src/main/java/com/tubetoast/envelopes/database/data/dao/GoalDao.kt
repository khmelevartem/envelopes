package com.tubetoast.envelopes.database.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.tubetoast.envelopes.database.data.GoalEntity

@Dao
abstract class GoalDao : StandardDao<GoalEntity> {

    @Query("SELECT * from goalentity")
    abstract override fun getAll(): List<GoalEntity>

    override fun getCollection(foreignKey: Int): List<GoalEntity> =
        emptyList()

    @Query("SELECT * from goalentity WHERE valueId LIKE :valueId")
    abstract override fun get(valueId: Int): GoalEntity?

    @Query("SELECT * from goalentity WHERE primaryKey LIKE :primaryKey")
    abstract override fun getByKey(primaryKey: Int): GoalEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract override fun write(databaseEntity: GoalEntity)

    @Query("DELETE from goalentity WHERE valueId Like :valueId")
    abstract override fun delete(valueId: Int): Int

    @Query("DELETE from goalentity WHERE foreignKey LIKE :foreignKey")
    abstract override fun deleteCollection(foreignKey: Int): Int

    @Query("DELETE from goalentity")
    abstract override fun deleteAll(): Int

    @Update
    abstract override fun update(newValue: GoalEntity): Int
}
