package com.tubetoast.envelopes.android.presentation.ui.screens

import com.google.common.truth.Truth
import com.tubetoast.envelopes.common.domain.models.Amount
import com.tubetoast.envelopes.common.domain.models.Envelope
import org.junit.Test

class EditEnvelopeViewModelTest {
    private val interactor = EnvelopeInteractorStub()
    private val viewModel = EditEnvelopeViewModel(interactor)

    @Test
    fun testInitial() {
        Truth.assertThat(viewModel.canConfirm()).isFalse()
        viewModel.envelope(interactor.envelopes.first().id.code)
        Truth.assertThat(viewModel.canConfirm()).isFalse()
    }

    @Test
    fun testEditOnlyName() {
        viewModel.envelope(interactor.envelopes.first().id.code)
        viewModel.setName("new name")
        Truth.assertThat(viewModel.canConfirm()).isTrue()
    }

    @Test
    fun testEditOnlyNameToExistingOne() {
        interactor.addEnvelope(Envelope("new name", Amount(100)))
        viewModel.envelope(interactor.envelopes.first().id.code)
        viewModel.setName("new name")
        Truth.assertThat(viewModel.canConfirm()).isFalse()
    }

    @Test
    fun testEditToMatchExisting() {
        interactor.addEnvelope(Envelope("new name", Amount(100)))
        viewModel.envelope(interactor.envelopes.first().id.code)
        viewModel.setName("new name")
        viewModel.setLimit("100")
        Truth.assertThat(viewModel.canConfirm()).isFalse()
    }

    @Test
    fun testEditNameAndAmount() {
        viewModel.envelope(interactor.envelopes.first().id.code)
        viewModel.setName("new name")
        viewModel.setLimit("123")
        Truth.assertThat(viewModel.canConfirm()).isTrue()
    }

    @Test
    fun testEditOnlyAmount() {
        viewModel.envelope(interactor.envelopes.first().id.code)
        viewModel.setLimit("123")
        Truth.assertThat(viewModel.canConfirm()).isTrue()
    }
}