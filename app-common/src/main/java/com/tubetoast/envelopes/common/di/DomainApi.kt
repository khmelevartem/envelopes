package com.tubetoast.envelopes.common.di

import com.tubetoast.envelopes.common.domain.EnvelopesInteractor
import org.koin.java.KoinJavaComponent.inject

class DomainApi : Api {
    val envelopesInteractor: EnvelopesInteractor by inject(EnvelopesInteractor::class.java)
}