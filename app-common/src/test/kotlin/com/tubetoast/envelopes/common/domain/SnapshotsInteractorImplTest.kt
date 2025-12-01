package com.tubetoast.envelopes.common.domain

import com.google.common.truth.Truth.assertThat
import com.tubetoast.envelopes.common.data.CategoriesRepositoryInMemoryImpl
import com.tubetoast.envelopes.common.data.EnvelopesRepositoryInMemoryImpl
import com.tubetoast.envelopes.common.data.SpendingRepositoryInMemoryImpl
import com.tubetoast.envelopes.common.domain.models.Amount
import com.tubetoast.envelopes.common.domain.models.Envelope
import org.junit.jupiter.api.Test

class SnapshotsInteractorImplTest {

    private val spendingRepositoryImpl = SpendingRepositoryInMemoryImpl()
    private val categoriesRepositoryImpl = CategoriesRepositoryInMemoryImpl()
    private val envelopesRepositoryImpl = EnvelopesRepositoryInMemoryImpl()
    private val interactor: SnapshotsInteractorImpl = SnapshotsInteractorImpl(
        spendingRepositoryImpl,
        categoriesRepositoryImpl,
        envelopesRepositoryImpl
    )

    @Test
    fun testInitial() {
        assertThat(interactor.allEnvelopeSnapshots).isEmpty()
    }

    @Test
    fun testAdd() {
        assertThat(interactor.allEnvelopeSnapshots).isEmpty()
        envelopesRepositoryImpl.put(Envelope("empty", Amount.ZERO))
        envelopesRepositoryImpl.put(Envelope("empty", Amount.ZERO))
        envelopesRepositoryImpl.put(Envelope("not empty", Amount(5)))
        assertThat(interactor.allEnvelopeSnapshots).hasSize(2)
    }
}
