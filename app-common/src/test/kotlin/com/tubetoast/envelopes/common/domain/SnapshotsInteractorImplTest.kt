package com.tubetoast.envelopes.common.domain

import com.google.common.truth.Truth.assertThat
import com.tubetoast.envelopes.common.data.CategoriesRepositoryInMemoryBase
import com.tubetoast.envelopes.common.data.EnvelopesRepositoryInMemoryBase
import com.tubetoast.envelopes.common.data.SpendingRepositoryInMemoryBase
import com.tubetoast.envelopes.common.domain.models.Amount
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.settings.SettingsRepositoryDefaultImpl
import org.junit.jupiter.api.Test

class SnapshotsInteractorImplTest {

    private val spendingRepositoryImpl = SpendingRepositoryInMemoryBase()
    private val categoriesRepositoryImpl = CategoriesRepositoryInMemoryBase()
    private val envelopesRepositoryImpl = EnvelopesRepositoryInMemoryBase()
    private val selectedPeriodRepository = SelectedPeriodRepositoryImpl(
        SettingsRepositoryDefaultImpl()
    )
    private val interactor: SnapshotsInteractorImpl = SnapshotsInteractorImpl(
        spendingRepositoryImpl,
        categoriesRepositoryImpl,
        envelopesRepositoryImpl,
        selectedPeriodRepository
    )

    @Test
    fun testInitial() {
        assertThat(interactor.allSnapshots).isEmpty()
    }

    @Test
    fun testAdd() {
        assertThat(interactor.allSnapshots).isEmpty()
        envelopesRepositoryImpl.put(Envelope("empty", Amount.ZERO))
        envelopesRepositoryImpl.put(Envelope("empty", Amount.ZERO))
        envelopesRepositoryImpl.put(Envelope("not empty", Amount(5)))
        assertThat(interactor.allSnapshots).hasSize(2)
    }
}
