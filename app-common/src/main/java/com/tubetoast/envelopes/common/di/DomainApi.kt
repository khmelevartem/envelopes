package com.tubetoast.envelopes.common.di

import com.tubetoast.envelopes.common.domain.EnvelopeInteractor
import com.tubetoast.envelopes.common.domain.SnapshotsInteractor
import org.koin.java.KoinJavaComponent.inject

class DomainApi : Api {
    val envelopesInteractor: SnapshotsInteractor by inject(SnapshotsInteractor::class.java)
    val envelopeInteractor: EnvelopeInteractor by inject(EnvelopeInteractor::class.java)
}
