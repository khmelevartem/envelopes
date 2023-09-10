package com.tubetoast.envelopes.common.domain

import com.google.common.truth.Truth.assertThat
import com.tubetoast.envelopes.common.data.CategoriesRepositoryImpl
import com.tubetoast.envelopes.common.data.EnvelopesRepositoryImpl
import com.tubetoast.envelopes.common.data.SpendingRepositoryImpl
import com.tubetoast.envelopes.common.domain.models.Amount
import com.tubetoast.envelopes.common.domain.models.Envelope
import org.junit.jupiter.api.Test

class SnapshotsInteractorImplTest {

    private val spendingRepositoryImpl = SpendingRepositoryImpl()
    private val categoriesRepositoryImpl = CategoriesRepositoryImpl()
    private val envelopesRepositoryImpl = EnvelopesRepositoryImpl()
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
