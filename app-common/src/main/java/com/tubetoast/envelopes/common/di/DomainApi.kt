package com.tubetoast.envelopes.common.di

import com.tubetoast.envelopes.common.domain.EditInteractor
import com.tubetoast.envelopes.common.domain.SnapshotsInteractor
import org.koin.java.KoinJavaComponent.inject

class DomainApi : Api {
    val envelopesInteractor: SnapshotsInteractor by inject(SnapshotsInteractor::class.java)
    val editInteractor: EditInteractor by inject(EditInteractor::class.java)
}