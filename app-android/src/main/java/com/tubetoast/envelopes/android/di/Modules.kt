package com.tubetoast.envelopes.android.di

import android.content.Context
import com.tubetoast.envelopes.common.di.domainModule
import com.tubetoast.envelopes.database.di.databaseModule
import com.tubetoast.envelopes.monefy.di.monefyParserModule

fun applicationModules(context: Context) = arrayOf(
    appModule,
    domainModule,
    monefyParserModule,
    databaseModule(context)
)
