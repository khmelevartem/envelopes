package com.tubetoast.envelopes.database.data

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

fun provideAndroidBuilder(context: Context): RoomDatabase.Builder<StandardDatabase> {
    val dbFile = context.getDatabasePath("standard_database.db")
    return Room.databaseBuilder<StandardDatabase>(
        context = context.applicationContext,
        name = dbFile.absolutePath
    )
}
