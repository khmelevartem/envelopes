package com.tubetoast.envelopes.database.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [EnvelopeEntity::class, CategoryEntity::class, SpendingEntity::class],
    version = 1
)
abstract class StandardDatabase : RoomDatabase() {
    abstract fun envelopeDao(): EnvelopeDao
    abstract fun categoryDao(): CategoryDao
    abstract fun spendingDao(): SpendingDao
}