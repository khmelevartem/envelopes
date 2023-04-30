package com.tubetoast.envelopes.common.domain

import com.google.common.truth.Truth.assertThat
import com.tubetoast.envelopes.common.data.CategoriesRepositoryImpl
import com.tubetoast.envelopes.common.data.EnvelopesRepositoryImpl
import com.tubetoast.envelopes.common.data.SpendingRepositoryImpl
import com.tubetoast.envelopes.common.domain.models.Amount
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.Hash
import org.junit.jupiter.api.Test

class EnvelopesInteractorImplTest {

    private val spendingRepositoryImpl = SpendingRepositoryImpl()
    private val categoriesRepositoryImpl = CategoriesRepositoryImpl()
    private val envelopesRepositoryImpl = EnvelopesRepositoryImpl()
    private val interactor: EnvelopesInteractorImpl = EnvelopesInteractorImpl(
        spendingRepositoryImpl,
        categoriesRepositoryImpl,
        envelopesRepositoryImpl
    )

    @Test
    fun testInitial() {
        assertThat(interactor.envelopeSnapshotFlow.value).isEmpty()
    }

    @Test
    fun testAdd() {
        assertThat(interactor.envelopeSnapshotFlow.value).isEmpty()
        envelopesRepositoryImpl.add(Hash.any(), Envelope("empty", Amount(0)))
        envelopesRepositoryImpl.add(Hash.any(), Envelope("empty", Amount(0)))
        envelopesRepositoryImpl.add(Hash.any(), Envelope("not empty", Amount(5)))
        assertThat(interactor.envelopeSnapshotFlow.value).hasSize(2)
    }


}