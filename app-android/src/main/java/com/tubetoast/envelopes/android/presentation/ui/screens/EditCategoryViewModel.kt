package com.tubetoast.envelopes.android.presentation.ui.screens

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.tubetoast.envelopes.common.domain.CategoryInteractor
import com.tubetoast.envelopes.common.domain.EnvelopeInteractor
import com.tubetoast.envelopes.common.domain.models.Amount
import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.id

class EditCategoryViewModel(
    private val categoryInteractor: CategoryInteractor,
    private val envelopeInteractor: EnvelopeInteractor,
) : ViewModel() {

    val category = mutableStateOf(Category.EMPTY)
    private var editedCategory: Category? = null
    private var envelope: Envelope? = null

    fun setEditedCategoryId(id: Int) {
        categoryInteractor.getCategory(id.id())?.let {
            category.value = it
            editedCategory = it
        } ?: throw IllegalStateException("Trying to set envelope id $id that doesn't exit")
    }

    fun setEnvelopeId(id: Int) {
        envelopeInteractor.getExactEnvelope(id.id())?.let {
            envelope = it
        } ?: throw IllegalStateException("Trying to set envelope id $id that doesn't exit")
    }

    fun setName(input: String) {
        category.value = category.value.copy(name = input)
    }

    fun setLimit(input: String) {
        if (input.isBlank()) {
            category.value = category.value.copy(limit = Amount.ZERO)
        } else {
            input.toIntOrNull()?.let {
                category.value = category.value.copy(limit = Amount(it))
            }
        }
    }

    fun confirm() {
        if (!canConfirm()) throw IllegalStateException("Cannot confirm!")
        val new = category.value
        getExistingCategory()?.let { old ->
            categoryInteractor.editCategory(old, new)
        } ?: categoryInteractor.addCategory(new, envelope!!.id)
        reset()
    }

    fun canConfirm(): Boolean {
        return category.value.run {
            name.isNotBlank() &&
                notSameAsOld &&
                (notSameNameAsExistingIfNew || notSameAsExistingIfEdit) &&
                envelope != null
        }
    }

    private val Category.notSameAsExistingIfEdit get() = isEditMode() && getExistingCategory() != this

    private val Category.notSameNameAsExistingIfNew get() = !isEditMode() && getExistingCategory()?.name != this.name

    private val Category.notSameAsOld get() = editedCategory != this

    fun delete() {
        val existingCategory = getExistingCategory()
        if (canDelete(existingCategory)) categoryInteractor.deleteCategory(existingCategory!!)
        reset()
    }

    fun canDelete(category: Category? = getExistingCategory()): Boolean {
        return category != null && isEditMode()
    }

    private fun isEditMode() = editedCategory != null

    private fun getExistingCategory(): Category? =
        editedCategory?.name?.let {
            categoryInteractor.getCategoryByName(it)
        } ?: categoryInteractor.getCategoryByName(category.value.name)

    private fun reset() {
        category.value = Category.EMPTY
        editedCategory = null
    }
}
