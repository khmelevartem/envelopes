package com.tubetoast.envelopes.android.di

import com.tubetoast.envelopes.common.di.domainModule
import com.tubetoast.envelopes.monefy.di.monefyParserModule

val applicationModules = arrayOf(
    appModule,
    domainModule,
    monefyParserModule,
)
