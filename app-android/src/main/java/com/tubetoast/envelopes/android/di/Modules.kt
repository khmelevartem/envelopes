package com.tubetoast.envelopes.android.di

import com.tubetoast.envelopes.common.di.domainModule
import com.tubetoast.envelopes.database.di.databaseModule
import com.tubetoast.envelopes.monefy.di.monefyParserModule

val applicationModules = arrayOf(
    appModule,
    monefyParserModule,
    databaseModule,
    repositoriesModule,
    domainModule
)
