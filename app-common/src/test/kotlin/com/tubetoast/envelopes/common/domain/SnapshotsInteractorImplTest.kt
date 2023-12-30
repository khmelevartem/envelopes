package com.tubetoast.envelopes.common.domain

import com.google.common.truth.Truth.assertThat
import com.tubetoast.envelopes.common.data.CategoriesRepositoryBase
import com.tubetoast.envelopes.common.data.EnvelopesRepositoryBase
import com.tubetoast.envelopes.common.data.SpendingRepositoryBase
import com.tubetoast.envelopes.common.domain.models.Amount
import com.tubetoast.envelopes.common.domain.models.Envelope
import org.junit.jupiter.api.Test

class SnapshotsInteractorImplTest {

    private val spendingRepositoryImpl = SpendingRepositoryBase()
    private val categoriesRepositoryImpl = CategoriesRepositoryBase()
    private val envelopesRepositoryImpl = EnvelopesRepositoryBase()
    private val interactor: SnapshotsInteractorImpl = SnapshotsInteractorImpl(
        spendingRepositoryImpl,
        categoriesRepositoryImpl,
        envelopesRepositoryImpl,
    )

    @Test
    fun testInitial() {
        assertThat(interactor.envelopeSnapshotFlow.value).isEmpty()
    }

    @Test
    fun testAdd() {
        assertThat(interactor.envelopeSnapshotFlow.value).isEmpty()
        envelopesRepositoryImpl.put(Envelope("empty", Amount.ZERO))
        envelopesRepositoryImpl.put(Envelope("empty", Amount.ZERO))
        envelopesRepositoryImpl.put(Envelope("not empty", Amount(5)))
        assertThat(interactor.envelopeSnapshotFlow.value).hasSize(2)
    }
}
