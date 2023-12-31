package com.tubetoast.envelopes.android.presentation.ui.screens

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tubetoast.envelopes.common.domain.CategoryInteractor
import com.tubetoast.envelopes.common.domain.EnvelopeInteractor
import com.tubetoast.envelopes.common.domain.SnapshotsInteractor
import com.tubetoast.envelopes.common.domain.models.Amount
import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.Id
import com.tubetoast.envelopes.common.domain.models.id
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class EditCategoryViewModel(
    private val categoryInteractor: CategoryInteractor,
    private val envelopeInteractor: EnvelopeInteractor,
    private val snapshotsInteractor: SnapshotsInteractor
) : ViewModel() {

    sealed interface Mode {
        fun canConfirm(category: Category?): Boolean
        fun confirm(category: Category, envelope: Envelope)
        fun canDelete(): Boolean
        fun delete()
        fun envelope(id: Id<Envelope>?, change: (Envelope) -> Unit)
        fun canChooseEnvelope(): Boolean
    }

    private var mode: Mode = CreateCategoryMode(categoryInteractor, envelopeInteractor)

    private val draftCategory = mutableStateOf(Category.EMPTY)
    private var envelope = mutableStateOf(Envelope.EMPTY)

    fun category(id: Int?): State<Category> {
        id?.let {
            categoryInteractor.getCategory(id.id())?.let {
                draftCategory.value = it
                mode = EditCategoryMode(
                    categoryInteractor = categoryInteractor,
                    snapshotsInteractor = snapshotsInteractor,
                    scope = viewModelScope,
                    editedCategory = it
                )
            }
        } ?: reset()
        return draftCategory
    }

    fun envelope(id: Int?): State<Envelope> {
        mode.envelope(id?.id()) {
            envelope.value = it
        }
        return envelope
    }

    fun setName(input: String) {
        draftCategory.value = draftCategory.value.copy(name = input)
    }

    fun setLimit(input: String) {
        if (input.isBlank()) {
            draftCategory.value = draftCategory.value.copy(limit = Amount.ZERO)
        } else {
            input.toIntOrNull()?.let {
                draftCategory.value = draftCategory.value.copy(limit = Amount(it))
            }
        }
    }

    fun confirm() {
        if (!canConfirm()) throw IllegalStateException("Cannot confirm!")
        mode.confirm(draftCategory.value, envelope.value)
        reset()
    }

    fun canConfirm(): Boolean = mode.canConfirm(draftCategory.value)

    fun delete() {
        if (!canDelete()) throw IllegalStateException("Cannot delete!")
        mode.delete()
        reset()
    }

    fun canDelete(): Boolean = mode.canDelete()

    fun canChooseEnvelope(): Boolean = mode.canChooseEnvelope()

    private fun reset() {
        draftCategory.value = Category.EMPTY
        mode = CreateCategoryMode(categoryInteractor, envelopeInteractor)
    }
}

class CreateCategoryMode(
    private val categoryInteractor: CategoryInteractor,
    private val envelopeInteractor: EnvelopeInteractor
) : EditCategoryViewModel.Mode {
    override fun canConfirm(category: Category?): Boolean {
        return category?.run {
            name.isNotBlank() && categoryInteractor.getCategoryByName(name) == null
        } ?: false
    }

    override fun confirm(category: Category, envelope: Envelope) =
        categoryInteractor.addCategory(category, envelope.id)

    override fun canDelete(): Boolean = false
    override fun delete() = throw IllegalStateException("Cannot delete")
    override fun envelope(id: Id<Envelope>?, change: (Envelope) -> Unit) {
        id?.let {
            envelopeInteractor.getExactEnvelope(id)?.let { change(it) }
                ?: throw IllegalStateException("Trying to set category id $id that doesn't exit")
        }
    }

    override fun canChooseEnvelope() = false //TODO implement
}

class EditCategoryMode(
    private val categoryInteractor: CategoryInteractor,
    private val snapshotsInteractor: SnapshotsInteractor,
    private val scope: CoroutineScope,
    private val editedCategory: Category
) : EditCategoryViewModel.Mode {
    override fun canConfirm(category: Category?): Boolean {
        return category?.run {
            name.isNotBlank() && this != editedCategory && notSameNameAsOtherExisting()
        } ?: false
    }

    private fun Category.notSameNameAsOtherExisting() =
        name == editedCategory.name || categoryInteractor.getCategoryByName(name) == null

    override fun confirm(category: Category, envelope: Envelope) =
        categoryInteractor.editCategory(editedCategory, category)

    override fun canDelete(): Boolean = true
    override fun delete() = categoryInteractor.deleteCategory(editedCategory)
    override fun envelope(id: Id<Envelope>?, change: (Envelope) -> Unit) {
        scope.launch {
            snapshotsInteractor.envelopeSnapshotFlow.collect { set ->
                change(
                    set.find { snapshot ->
                        snapshot.categories.find { it.category == editedCategory } != null
                    }?.envelope ?: Envelope.EMPTY
                )
            }
        }
    }

    override fun canChooseEnvelope() = true

}