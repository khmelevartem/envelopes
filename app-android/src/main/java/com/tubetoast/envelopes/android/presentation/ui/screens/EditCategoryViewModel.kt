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
        suspend fun canConfirm(category: Category?): Boolean
        fun confirm(category: Category, envelope: Envelope)
        fun canDelete(): Boolean
        fun delete()
        fun envelope(id: Id<Envelope>?, change: (Envelope) -> Unit)
        fun canChooseEnvelope(): Boolean
    }

    data class UIState(
        val draftCategory: Category,
        val canConfirm: Boolean,
        val canDelete: Boolean
    ) {
        companion object {
            val EMPTY = UIState(Category.EMPTY, canConfirm = false, canDelete = false)
        }
    }

    private var mode: Mode =
        CreateCategoryMode(categoryInteractor, envelopeInteractor, viewModelScope)

    private val uiState = mutableStateOf(UIState.EMPTY)
    private var envelope = mutableStateOf(Envelope.EMPTY)

    fun uiState(id: Int?): State<UIState> {
        id?.let {
            viewModelScope.launch {
                categoryInteractor.getCategory(id.id())?.let {
                    updateUIState(it)
                    mode = EditCategoryMode(
                        categoryInteractor = categoryInteractor,
                        snapshotsInteractor = snapshotsInteractor,
                        scope = viewModelScope,
                        editedCategory = it
                    )
                }
            }
        } ?: reset()
        return uiState
    }

    fun envelope(id: Int?): State<Envelope> {
        mode.envelope(id?.id()) {
            envelope.value = it
        }
        return envelope
    }

    fun setName(input: String) {
        updateUIState(uiState.value.draftCategory.copy(name = input))
    }

    fun setLimit(input: String) {
        if (input.isBlank()) {
            updateUIState(uiState.value.draftCategory.copy(limit = Amount.ZERO))
        } else {
            input.toIntOrNull()?.let {
                updateUIState(uiState.value.draftCategory.copy(limit = Amount(it)))
            }
        }
    }

    fun confirm() {
        viewModelScope.launch {
            mode.confirm(uiState.value.draftCategory, envelope.value)
        }
        reset()
    }

    fun delete() {
        mode.delete()
        reset()
    }

    fun canChooseEnvelope(): Boolean = mode.canChooseEnvelope()

    private fun updateUIState(category: Category = uiState.value.draftCategory) {
        viewModelScope.launch {
            uiState.value = UIState(category, canConfirm = mode.canConfirm(category), canDelete = mode.canDelete())
        }
    }

    private fun reset() {
        uiState.value = UIState.EMPTY
        envelope.value = Envelope.EMPTY
        mode = CreateCategoryMode(categoryInteractor, envelopeInteractor, viewModelScope)
    }
}

class CreateCategoryMode(
    private val categoryInteractor: CategoryInteractor,
    private val envelopeInteractor: EnvelopeInteractor,
    private val scope: CoroutineScope
) : EditCategoryViewModel.Mode {
    override suspend fun canConfirm(category: Category?): Boolean {
        return category?.run {
            name.isNotBlank() && categoryInteractor.getCategoryByName(name) == null
        } ?: false
    }

    override fun confirm(category: Category, envelope: Envelope) {
        scope.launch {
            categoryInteractor.addCategory(category, envelope.id)
        }
    }

    override fun canDelete(): Boolean = false
    override fun delete() = throw IllegalStateException("Cannot delete")
    override fun envelope(id: Id<Envelope>?, change: (Envelope) -> Unit) {
        id?.let {
            scope.launch {
                envelopeInteractor.getExactEnvelope(id)?.let { change(it) }
                    ?: throw IllegalStateException("Trying to set category id $id that doesn't exit")
            }
        }
    }

    override fun canChooseEnvelope() = false // TODO implement
}

class EditCategoryMode(
    private val categoryInteractor: CategoryInteractor,
    private val snapshotsInteractor: SnapshotsInteractor,
    private val scope: CoroutineScope,
    private val editedCategory: Category
) : EditCategoryViewModel.Mode {
    override suspend fun canConfirm(category: Category?): Boolean {
        return category?.run {
            name.isNotBlank() && this != editedCategory && notSameNameAsOtherExisting()
        } ?: false
    }

    private suspend fun Category.notSameNameAsOtherExisting() =
        name == editedCategory.name || categoryInteractor.getCategoryByName(name) == null

    override fun confirm(category: Category, envelope: Envelope) {
        scope.launch {
            categoryInteractor.editCategory(editedCategory, category)
        }
    }

    override fun canDelete(): Boolean = true
    override fun delete() {
        scope.launch {
            categoryInteractor.deleteCategory(editedCategory)
        }
    }

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
