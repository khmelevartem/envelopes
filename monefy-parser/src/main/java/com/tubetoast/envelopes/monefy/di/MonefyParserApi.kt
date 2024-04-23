package com.tubetoast.envelopes.monefy.di

import com.tubetoast.envelopes.common.di.Api
import com.tubetoast.envelopes.monefy.data.MonefyInteractor
import org.koin.java.KoinJavaComponent.inject

class MonefyParserApi : Api {
    val monefyInteractor: MonefyInteractor by inject(MonefyInteractor::class.java)
}
