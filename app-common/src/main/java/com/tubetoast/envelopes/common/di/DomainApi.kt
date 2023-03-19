package com.tubetoast.envelopes.common.di

import com.tubetoast.envelopes.common.domain.Interactor
import org.koin.java.KoinJavaComponent.inject

class DomainApi : Api {
    val interactor: Interactor by inject(Interactor::class.java)
}