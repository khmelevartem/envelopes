package com.tubetoast.envelopes.database.di

import android.content.Context
import com.tubetoast.envelopes.database.data.provideAndroidBuilder

fun databaseAndroidModule(context: Context) = databaseModule(provideAndroidBuilder(context))
