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
        setEditMode()
        Truth.assertThat(viewModel.canConfirm()).isFalse()
    }

    @Test
    fun testCreateWithUniqueName() {
        viewModel.uiState(null)
        viewModel.setName("new name")
        Truth.assertThat(viewModel.canConfirm()).isTrue()
    }

    @Test
    fun testCreateWithExistingName() {
        viewModel.uiState(null)
        viewModel.setName("test")
        Truth.assertThat(viewModel.canConfirm()).isFalse()
    }

    @Test
    fun testEditOnlyName() {
        setEditMode()
        viewModel.setName("new name")
        Truth.assertThat(viewModel.canConfirm()).isTrue()
    }

    @Test
    fun testEditOnlyNameToExistingOne() {
        interactor.addEnvelope(Envelope("new name", Amount(100)))
        setEditMode()
        viewModel.setName("new name")
        Truth.assertThat(viewModel.canConfirm()).isFalse()
    }

    @Test
    fun testEditToMatchExisting() {
        interactor.addEnvelope(Envelope("new name", Amount(100)))
        setEditMode()
        viewModel.setName("new name")
        viewModel.setLimit("100")
        Truth.assertThat(viewModel.canConfirm()).isFalse()
    }

    @Test
    fun testEditNameAndAmount() {
        setEditMode()
        viewModel.setName("new name")
        viewModel.setLimit("123")
        Truth.assertThat(viewModel.canConfirm()).isTrue()
    }

    @Test
    fun testEditOnlyAmount() {
        setEditMode()
        viewModel.setLimit("123")
        Truth.assertThat(viewModel.canConfirm()).isTrue()
    }

    @Test
    fun testCanDeleteNew() {
        Truth.assertThat(viewModel.canDelete()).isFalse()
        viewModel.uiState(null)
        Truth.assertThat(viewModel.canDelete()).isFalse()
    }

    @Test
    fun testCanDeleteExisting() {
        setEditMode()
        Truth.assertThat(viewModel.canDelete()).isTrue()
    }

    private fun setEditMode() {
        viewModel.uiState(interactor.envelopes.first().id.code)
    }
}
