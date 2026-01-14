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

    data class CategoryOperations(
        val canConfirm: Boolean,
        val canDelete: Boolean,
        val canChooseEnvelope: Boolean
    ) {
        companion object {
            val EMPTY = CategoryOperations(
                canConfirm = false,
                canDelete = false,
                canChooseEnvelope = false
            )
        }
    }

    private var mode: Mode = CreateCategoryMode(categoryInteractor, envelopeInteractor, viewModelScope)
        set(value) {
            field.destroy()
            field = value
            isNewCategory.value = value is CreateCategoryMode
        }

    val isNewCategory = mutableStateOf(mode is CreateCategoryMode)

    private val _categoryOperations = mutableStateOf(CategoryOperations.EMPTY)
    private val _envelope = mutableStateOf(Envelope.EMPTY)
    private val _draftCategory = mutableStateOf(Category.EMPTY)

    val categoryOperations: State<CategoryOperations> get() = _categoryOperations
    val envelope: State<Envelope> get() = _envelope

    fun init(categoryId: Int?, envelopeId: Int?): State<Category> {
        viewModelScope.launch {
            categoryId?.let {
                categoryInteractor.getCategory(categoryId.id())?.let { editedCategory ->
                    mode = EditCategoryMode(
                        categoryInteractor = categoryInteractor,
                        snapshotsInteractor = snapshotsInteractor,
                        editedCategory = editedCategory,
                        scope = viewModelScope
                    )
                    updateCategory(editedCategory)
                }
            } ?: reset()
            mode.envelope(envelopeId?.id()) { env ->
                _envelope.value = env
            }
        }
        return _draftCategory
    }

    fun setName(input: String) {
        updateCategory(_draftCategory.value.copy(name = input))
    }

    fun setLimit(input: String) {
        updateCategory(_draftCategory.value.copy(limit = Amount(input.toLongOrNull() ?: 0)))
    }

    fun confirm() {
        viewModelScope.launch {
            mode.confirm(_draftCategory.value, _envelope.value)
            reset()
        }
    }

    fun delete() {
        viewModelScope.launch {
            mode.delete()
            reset()
        }
    }

    override fun onCleared() {
        super.onCleared()
        mode.destroy()
    }

    private fun updateCategory(category: Category) {
        _draftCategory.value = category
        viewModelScope.launch {
            _categoryOperations.value = CategoryOperations(
                canConfirm = mode.canConfirm(category),
                canDelete = mode.canDelete(),
                canChooseEnvelope = mode.canChooseEnvelope()
            )
        }
    }

    private fun reset() {
        _draftCategory.value = Category.EMPTY
        _envelope.value = Envelope.EMPTY
        _categoryOperations.value = CategoryOperations.EMPTY
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
            snapshotsInteractor.allEnvelopeSnapshotsFlow.collect { set ->
                val envelope = set.find { snapshot ->
                    snapshot.categories.find { it.category == editedCategory } != null
                }?.envelope
                envelope?.let(change) ?: destroy()
            }
        }
    }

    override fun canChooseEnvelope() = true
}
