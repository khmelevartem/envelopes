package com.tubetoast.envelopes.monefy.di

import com.tubetoast.envelopes.monefy.data.MonefyDataParser
import org.koin.dsl.module

val monefyParserModule = module {
    val parser = MonefyDataParser()
}
