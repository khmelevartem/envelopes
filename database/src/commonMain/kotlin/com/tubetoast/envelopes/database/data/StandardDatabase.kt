package com.tubetoast.envelopes.database.data

import androidx.room.AutoMigration
import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import com.tubetoast.envelopes.database.data.dao.CategoryDao
import com.tubetoast.envelopes.database.data.dao.CategoryToGoalLinksDao
import com.tubetoast.envelopes.database.data.dao.EnvelopeDao
import com.tubetoast.envelopes.database.data.dao.EnvelopeSnapshotDao
import com.tubetoast.envelopes.database.data.dao.GoalDao
import com.tubetoast.envelopes.database.data.dao.GoalSnapshotDao
import com.tubetoast.envelopes.database.data.dao.SpendingDao

@Database(
    entities = [
        EnvelopeEntity::class,
        CategoryEntity::class,
        SpendingEntity::class,
        GoalEntity::class,
        CategoryToGoalLinkEntity::class
    ],
    autoMigrations = [
        AutoMigration(1, 2)
    ],
    version = 2
)
@ConstructedBy(DatabaseBuilder::class)
abstract class StandardDatabase : RoomDatabase() {
    abstract fun envelopeDao(): EnvelopeDao

    abstract fun categoryDao(): CategoryDao

    abstract fun spendingDao(): SpendingDao

    abstract fun goalDao(): GoalDao

    abstract fun linksDao(): CategoryToGoalLinksDao

    abstract fun envelopeSnapshotDao(): EnvelopeSnapshotDao

    abstract fun goalSnapshotDao(): GoalSnapshotDao
}

@Suppress("KotlinNoActualForExpect")
expect object DatabaseBuilder : RoomDatabaseConstructor<StandardDatabase> {
    override fun initialize(): StandardDatabase
}
