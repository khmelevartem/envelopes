package com.tubetoast.envelopes.ui.di

import com.tubetoast.envelopes.common.di.domainModule
import com.tubetoast.envelopes.monefy.di.monefyParserModule

fun applicationModules() =
    arrayOf(
        appModule,
        monefyParserModule,
        domainModule
    )
