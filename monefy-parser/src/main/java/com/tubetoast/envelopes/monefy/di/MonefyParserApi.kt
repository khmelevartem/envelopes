package com.tubetoast.envelopes.monefy.di

import com.tubetoast.envelopes.common.di.Api
import com.tubetoast.envelopes.monefy.data.MonefyDataParser
import org.koin.java.KoinJavaComponent.inject

class MonefyParserApi : Api {
    val parser : MonefyDataParser by inject(MonefyDataParser::class.java)
}
