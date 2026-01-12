package com.tubetoast.envelopes.database.data.dao

import com.tubetoast.envelopes.database.data.DatabaseEntity

interface StandardDao<DE : DatabaseEntity> {

    fun getAll(): List<DE>

    fun getCollection(foreignKey: Int): List<DE>

    fun get(valueId: Int): DE?

    fun getByKey(primaryKey: Int): DE?

    fun write(databaseEntity: DE)

    fun delete(valueId: Int): Int

    fun deleteCollection(foreignKey: Int): Int

    fun deleteAll(): Int

    fun update(newValue: DE): Int
}
