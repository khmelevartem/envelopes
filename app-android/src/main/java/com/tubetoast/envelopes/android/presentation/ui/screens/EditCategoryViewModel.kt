package com.tubetoast.envelopes.android.presentation.ui.screens

import androidx.compose.runtime.State
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

    private val category = mutableStateOf(Category.EMPTY)
    private var editedCategory: Category = Category.EMPTY
    private var envelope = mutableStateOf(Envelope.EMPTY)

    fun category(id: Int?): State<Category> {
        getCategoryOrEmpty(id).let {
            category.value = it
            editedCategory = it
        }
        return category
    }

    private fun getCategoryOrEmpty(id: Int?) = id?.let {
        categoryInteractor.getCategory(id.id())
            ?: throw IllegalStateException("Trying to set category id $id that doesn't exit")
    } ?: Category.EMPTY

    fun envelope(id: Int?): State<Envelope> {
        id?.let {
            envelopeInteractor.getExactEnvelope(id.id())?.let {
                envelope.value = it
            } ?: throw IllegalStateException("Trying to set envelope id $id that doesn't exit")
        }
        return envelope
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
        } ?: categoryInteractor.addCategory(new, envelope.value.id)
        reset()
    }

    fun canConfirm(): Boolean {
        return category.value.run {
            name.isNotBlank() && (notSameNameAsExistingIfNew || notSameAsExistingIfEdit)
        }
    }

    private val Category.notSameAsExistingIfEdit get() = isEditMode() && getExistingCategory() != this

    private val Category.notSameNameAsExistingIfNew get() = !isEditMode() && getExistingCategory()?.name != this.name

    fun delete() {
        val existingCategory = getExistingCategory()
        if (canDelete(existingCategory)) categoryInteractor.deleteCategory(existingCategory!!)
        reset()
    }

    fun canDelete(category: Category? = getExistingCategory()): Boolean {
        return category != null && isEditMode()
    }

    fun canChooseEnvelope(): Boolean {
        return isEditMode()
    }

    private fun isEditMode() = editedCategory != Category.EMPTY

    private fun getExistingCategory(): Category? =
        categoryInteractor.getCategoryByName(editedCategory.name)
            ?: categoryInteractor.getCategoryByName(category.value.name)

    private fun reset() {
        category.value = Category.EMPTY
        editedCategory = Category.EMPTY
    }
}
