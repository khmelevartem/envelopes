package com.tubetoast.envelopes.android.presentation.ui.screens

import com.google.common.truth.Truth
import com.tubetoast.envelopes.common.domain.models.Amount
import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Envelope
import org.junit.Test

class EditCategoryViewModelTest {
    private val envelopeInteractorStub = EnvelopeInteractorStub()
    private val categoryInteractorStub = CategoryInteractorStub()
    private val viewModel = EditCategoryViewModel(categoryInteractorStub, envelopeInteractorStub)

    @Test
    fun testInitial() {
        Truth.assertThat(viewModel.canConfirm()).isFalse()
        setEditMode()
        Truth.assertThat(viewModel.canConfirm()).isFalse()
    }

    @Test
    fun testCreateWithUniqueName() {
        viewModel.category(null)
        viewModel.setName("new name")
        Truth.assertThat(viewModel.canConfirm()).isTrue()
    }

    @Test
    fun testCreateWithExistingName() {
        viewModel.category(null)
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
        categoryInteractorStub.addCategory(Category("new name", Amount(100)), Envelope("test", Amount(100)).id)
        setEditMode()
        viewModel.setName("new name")
        Truth.assertThat(viewModel.canConfirm()).isFalse()
    }

    @Test
    fun testEditToMatchExisting() {
        categoryInteractorStub.addCategory(Category("new name", Amount(100)), Envelope("test", Amount(100)).id)
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
        viewModel.envelope(null)
        Truth.assertThat(viewModel.canDelete()).isFalse()
    }

    @Test
    fun testCanDeleteExisting() {
        setEditMode()
        Truth.assertThat(viewModel.canDelete()).isTrue()
    }

    private fun setEditMode() {
        viewModel.category(categoryInteractorStub.categories.first().id.code)
    }
}