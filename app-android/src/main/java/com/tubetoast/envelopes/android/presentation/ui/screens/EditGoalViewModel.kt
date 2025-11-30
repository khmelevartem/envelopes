package com.tubetoast.envelopes.android.presentation.ui.screens

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tubetoast.envelopes.common.domain.models.Amount
import com.tubetoast.envelopes.common.domain.models.Date
import com.tubetoast.envelopes.common.domain.models.Goal
import com.tubetoast.envelopes.common.domain.snapshots.CategorySnapshot
import kotlinx.coroutines.launch

class EditGoalViewModel() : ViewModel() {

    sealed interface Mode {
        suspend fun canConfirm(goal: Goal?): Boolean
        suspend fun confirm(goal: Goal)
        suspend fun canDelete(): Boolean
        suspend fun delete()
    }

    data class GoalOperations(
        val canConfirm: Boolean,
        val canDelete: Boolean
    ) {
        companion object {
            val EMPTY = GoalOperations(canConfirm = false, canDelete = false)
        }
    }

    private var mode: Mode = CreateGoalMode()
        set(value) {
            field = value
            _isNewGoal.value = value is CreateGoalMode
        }
    private val _operations = mutableStateOf(GoalOperations.EMPTY)
    private val draftGoal = mutableStateOf(Goal.EMPTY)
    private val _categories = mutableStateOf(listOf<CategorySnapshot>())
    private val _isNewGoal = mutableStateOf(true)

    val operations: State<GoalOperations> = _operations
    val categories: State<List<CategorySnapshot>> = _categories
    val isNewGoal: State<Boolean> = _isNewGoal

    fun goal(goalId: Int?): State<Goal> {
        goalId?.let { id ->
//            viewModelScope.launch {
//                envelopeInteractor.getExactEnvelope(id.id())?.let {
//                    mode = EditGoalMode(envelopeInteractor, it)
//                    collectEnvelopeCategories(it)
//                    updateGoal(it)
//                } ?: throw IllegalStateException("Trying to set envelope id $id that doesn't exit")
//            }
        } ?: reset()
        return draftGoal
    }

    fun setName(input: String) {
        updateGoal { copy(name = input) }
    }

    fun setTarget(input: String) {
        val target = input.toLongOrNull() ?: 0
        require(target >= 0) { "seems that u need Long for that" }
        updateGoal { copy(target = Amount(target)) }
    }

    fun confirm() {
        viewModelScope.launch { mode.confirm(draftGoal.value) }
        reset()
    }

    fun delete() {
        viewModelScope.launch { mode.delete() }
        reset()
    }

    private fun collectEnvelopeCategories(goal: Goal) {
        viewModelScope.launch {
//            snapshotsInteractor.envelopeSnapshots(selectedPeriodRepository.selectedPeriodFlow)
//                .collect { set ->
//                    set.find {
//                        it.envelope == envelope
//                    }?.let { found ->
//                        _categories.value = found.categories
//                            .sortedByDescending { it.sum() }
//                    }
//                }
        }
    }

    private fun updateGoal(update: Goal.() -> Goal) {
        val newValue = draftGoal.value.update()
        draftGoal.value = newValue
        viewModelScope.launch {
            _operations.value = GoalOperations(
                canConfirm = mode.canConfirm(newValue),
                canDelete = mode.canDelete()
            )
        }
    }

    private fun reset() {
        mode = CreateGoalMode()
        draftGoal.value = Goal.EMPTY
        _operations.value = GoalOperations.EMPTY
        _categories.value = emptyList()
    }

    fun setStart(start: Date?) {
        updateGoal { copy(start = start) }
    }

    fun setFinish(finish: Date?) {
        updateGoal { copy(finish = finish) }
    }
}

private class CreateGoalMode() : EditGoalViewModel.Mode {
    override suspend fun canConfirm(goal: Goal?) = goal?.run {
        isNotEmpty() && hasValidDateRange() // && envelopeInteractor.getEnvelopeByName(name) == null
    } ?: false

    override suspend fun canDelete() = false
    override suspend fun delete() = throw IllegalStateException("Cannot delete what is not created")
    override suspend fun confirm(goal: Goal) = Unit // envelopeInteractor.addEnvelope(goal)
}

private class EditGoalMode(
    private val editedGoal: Goal
) : EditGoalViewModel.Mode {
    override suspend fun canConfirm(goal: Goal?) = goal?.run {
        isNotEmpty() && hasValidDateRange() && this != editedGoal && notSameNameAsOtherExisting()
    } ?: false

    private suspend fun Goal.notSameNameAsOtherExisting() =
        name == editedGoal.name // || envelopeInteractor.getEnvelopeByName(name) == null

    override suspend fun canDelete() = true

    override suspend fun delete() = Unit // envelopeInteractor.deleteEnvelope(editedGoal)
    override suspend fun confirm(goal: Goal) = Unit // envelopeInteractor.editEnvelope(editedGoal, goal)
}

private fun Goal.isNotEmpty(): Boolean = name.isNotBlank() && target != Amount.ZERO // && categories.isNotEmpty()

private fun Goal.hasValidDateRange(): Boolean = start == null || finish == null || start!! < finish!!
