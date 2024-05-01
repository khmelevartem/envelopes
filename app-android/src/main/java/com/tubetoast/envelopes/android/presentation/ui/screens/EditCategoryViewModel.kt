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
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch

class EditCategoryViewModel(
    private val categoryInteractor: CategoryInteractor,
    private val envelopeInteractor: EnvelopeInteractor,
    private val snapshotsInteractor: SnapshotsInteractor
) : ViewModel() {

    abstract class Mode(parentScope: CoroutineScope) {
        protected val scope = CoroutineScope(parentScope.coroutineContext)
        abstract suspend fun canConfirm(category: Category?): Boolean
        abstract suspend fun confirm(category: Category, envelope: Envelope)
        abstract fun canDelete(): Boolean
        abstract suspend fun delete()
        abstract fun envelope(id: Id<Envelope>?, change: (Envelope) -> Unit)
        abstract fun canChooseEnvelope(): Boolean
        fun destroy() = scope.coroutineContext.cancelChildren()
    }

    data class UIState(
        val draftCategory: Category,
        val canConfirm: Boolean,
        val canDelete: Boolean,
        val envelope: Envelope
    ) {
        companion object {
            val EMPTY = UIState(
                Category.EMPTY,
                canConfirm = false,
                canDelete = false,
                envelope = Envelope.EMPTY
            )
        }
    }

    private var mode: Mode =
        CreateCategoryMode(categoryInteractor, envelopeInteractor, viewModelScope)
        set(value) {
            field.destroy()
            field = value
        }

    private val uiState = mutableStateOf(UIState.EMPTY)

    fun uiState(id: Int?, envelopeId: Int?): State<UIState> {
        viewModelScope.launch {
            id?.let {
                categoryInteractor.getCategory(id.id())?.let { editedCategory ->
                    mode = EditCategoryMode(
                        categoryInteractor = categoryInteractor,
                        snapshotsInteractor = snapshotsInteractor,
                        editedCategory = editedCategory,
                        scope = viewModelScope
                    )
                    updateUIState(editedCategory)
                }
            } ?: reset()
            mode.envelope(envelopeId?.id()) { env ->
                updateEnvelope(envelope = env)
            }
        }
        return uiState
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
            mode.confirm(uiState.value.draftCategory, uiState.value.envelope)
            reset()
        }
    }

    fun delete() {
        viewModelScope.launch {
            mode.delete()
            reset()
        }
    }

    fun canChooseEnvelope(): Boolean = mode.canChooseEnvelope()

    override fun onCleared() {
        super.onCleared()
        mode.destroy()
    }

    private fun updateUIState(category: Category = uiState.value.draftCategory) {
        viewModelScope.launch {
            uiState.value = uiState.value.copy(
                draftCategory = category,
                canConfirm = mode.canConfirm(category),
                canDelete = mode.canDelete(),
            )
        }
    }

    private fun updateEnvelope(envelope: Envelope) {
        uiState.value = uiState.value.copy(envelope = envelope)
    }

    private fun reset() {
        uiState.value = UIState.EMPTY
        mode = CreateCategoryMode(categoryInteractor, envelopeInteractor, viewModelScope)
    }
}

class CreateCategoryMode(
    private val categoryInteractor: CategoryInteractor,
    private val envelopeInteractor: EnvelopeInteractor,
    scope: CoroutineScope
) : EditCategoryViewModel.Mode(scope) {
    override suspend fun canConfirm(category: Category?): Boolean {
        return category?.run {
            name.isNotBlank() && categoryInteractor.getCategoryByName(name) == null
        } ?: false
    }

    override suspend fun confirm(category: Category, envelope: Envelope) {
        categoryInteractor.addCategory(category, envelope.id)
    }

    override fun canDelete(): Boolean = false
    override suspend fun delete() = throw IllegalStateException("Cannot delete")
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
    private val editedCategory: Category,
    scope: CoroutineScope
) : EditCategoryViewModel.Mode(scope) {
    override suspend fun canConfirm(category: Category?): Boolean {
        return category?.run {
            name.isNotBlank() && this != editedCategory && notSameNameAsOtherExisting()
        } ?: false
    }

    private suspend fun Category.notSameNameAsOtherExisting() =
        name == editedCategory.name || categoryInteractor.getCategoryByName(name) == null

    override suspend fun confirm(category: Category, envelope: Envelope) {
        categoryInteractor.editCategory(editedCategory, category)
    }

    override fun canDelete(): Boolean = true
    override suspend fun delete() {
        categoryInteractor.deleteCategory(editedCategory)
    }

    override fun envelope(id: Id<Envelope>?, change: (Envelope) -> Unit) {
        scope.launch {
            snapshotsInteractor.allSnapshotsFlow.collect { set ->
                val envelope = set.find { snapshot ->
                    snapshot.categories.find { it.category == editedCategory } != null
                }?.envelope
                envelope?.let(change) ?: destroy()
            }
        }
    }

    override fun canChooseEnvelope() = true
}
