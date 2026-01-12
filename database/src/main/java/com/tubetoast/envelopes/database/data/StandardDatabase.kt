package com.tubetoast.envelopes.database.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tubetoast.envelopes.database.data.dao.CategoryDao
import com.tubetoast.envelopes.database.data.dao.EnvelopeDao
import com.tubetoast.envelopes.database.data.dao.GoalDao
import com.tubetoast.envelopes.database.data.dao.SpendingDao

@Database(
    entities = [EnvelopeEntity::class, CategoryEntity::class, SpendingEntity::class, GoalEntity::class],
    version = 1
)
abstract class StandardDatabase : RoomDatabase() {
    abstract fun envelopeDao(): EnvelopeDao
    abstract fun categoryDao(): CategoryDao
    abstract fun spendingDao(): SpendingDao
    abstract fun goalDao(): GoalDao

    companion object Factory {
        fun create(context: Context) =
            Room.databaseBuilder(
                context = context,
                klass = StandardDatabase::class.java,
                name = "standard database"
            ).build()
    }
}
