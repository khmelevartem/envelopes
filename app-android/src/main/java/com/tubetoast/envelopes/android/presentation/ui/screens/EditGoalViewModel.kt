package com.tubetoast.envelopes.android.presentation.ui.screens

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tubetoast.envelopes.android.domain.SelectedCategoryRepository
import com.tubetoast.envelopes.common.domain.GoalInteractor
import com.tubetoast.envelopes.common.domain.GoalSnapshotInteractor
import com.tubetoast.envelopes.common.domain.models.Amount
import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Date
import com.tubetoast.envelopes.common.domain.models.Goal
import com.tubetoast.envelopes.common.domain.models.id
import kotlinx.coroutines.launch

class EditGoalViewModel(
    private val selectedCategoryRepository: SelectedCategoryRepository,
    private val goalInteractor: GoalInteractor,
    private val goalSnapshotInteractor: GoalSnapshotInteractor
) : ViewModel() {

    sealed interface Mode {
        suspend fun canConfirm(goal: Goal?, categories: Set<Category>): Boolean
        suspend fun confirm(goal: Goal, categories: Set<Category>)
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

    private var mode: Mode = CreateGoalMode(goalInteractor, goalSnapshotInteractor)
        set(value) {
            field = value
            _isNewGoal.value = value is CreateGoalMode
        }
    private val draftGoal = mutableStateOf(Goal.EMPTY)
    private val _operations = mutableStateOf(GoalOperations.EMPTY)
    private val _isNewGoal = mutableStateOf(true)

    val operations: State<GoalOperations> = _operations
    val isNewGoal: State<Boolean> = _isNewGoal

    fun goal(goalId: Int?): State<Goal> {
        goalId?.let { id ->
            viewModelScope.launch {
                goalInteractor.getExactGoal(id.id())?.let { foundGoal ->
                    val foundCategories =
                        goalSnapshotInteractor.goalSnapshots.value.find { it.goal == foundGoal }?.categories
                            ?.mapTo(mutableSetOf()) { it.category }.orEmpty()
                    mode = EditGoalMode(foundGoal, foundCategories, goalInteractor, goalSnapshotInteractor)
                    selectedCategoryRepository.changeSelection {
                        map {
                            it.copy(
                                item = it.item,
                                isSelected = foundCategories.contains(it.item)
                            )
                        }.toSet()
                    }
                    update { foundGoal }
                } ?: throw IllegalStateException("Trying to set goal id $id that doesn't exit")
            }
        } ?: reset()
        collectCategories()
        return draftGoal
    }

    fun setName(input: String) {
        update { copy(name = input) }
    }

    fun setTarget(input: String) {
        val target = input.toLongOrNull() ?: 0
        require(target >= 0) { "seems that u need Long for that" }
        update { copy(target = Amount(target)) }
    }

    fun confirm() {
        viewModelScope.launch { mode.confirm(draftGoal.value, selectedCategoryRepository.selectedModels) }
//        reset() // for no flicker on confirm
    }

    fun delete() {
        viewModelScope.launch { mode.delete() }
        reset()
    }

    private fun collectCategories() {
        viewModelScope.launch {
            selectedCategoryRepository.items.collect { selectableCategories ->
                update()
            }
        }
    }

    private fun update(
        update: Goal.() -> Goal = { this }
    ) {
        val newValue = draftGoal.value.update()
        draftGoal.value = newValue
        viewModelScope.launch {
            _operations.value = GoalOperations(
                canConfirm = mode.canConfirm(newValue, selectedCategoryRepository.selectedModels),
                canDelete = mode.canDelete()
            )
        }
    }

    private fun reset() {
        mode = CreateGoalMode(goalInteractor, goalSnapshotInteractor)
        draftGoal.value = Goal.EMPTY
        _operations.value = GoalOperations.EMPTY
    }

    fun setStart(start: Date?) {
        update { copy(start = start) }
    }

    fun setFinish(finish: Date?) {
        update { copy(finish = finish) }
    }
}

private class CreateGoalMode(
    private val goalInteractor: GoalInteractor,
    private val linksInteractor: GoalSnapshotInteractor
) : EditGoalViewModel.Mode {
    override suspend fun canConfirm(goal: Goal?, categories: Set<Category>) = goal?.run {
        isNotEmpty() && categories.isNotEmpty() && hasValidDateRange() && goalInteractor.getGoalByName(name) == null
    } ?: false

    override suspend fun canDelete() = false
    override suspend fun delete() = throw IllegalStateException("Cannot delete what is not created")
    override suspend fun confirm(goal: Goal, categories: Set<Category>) {
        goalInteractor.addGoal(goal)
        linksInteractor.attachNewCategoriesToGoal(goal, *categories.toTypedArray())
    }
}

private class EditGoalMode(
    private val editedGoal: Goal,
    private val foundCategories: Set<Category>,
    private val goalInteractor: GoalInteractor,
    private val linksInteractor: GoalSnapshotInteractor
) : EditGoalViewModel.Mode {
    override suspend fun canConfirm(goal: Goal?, categories: Set<Category>) = goal?.run {
        isNotEmpty() && hasValidDateRange() && (this != editedGoal || foundCategories != categories) &&
            notSameNameAsOtherExisting() && categories.isNotEmpty()
    } ?: false

    private suspend fun Goal.notSameNameAsOtherExisting() =
        name == editedGoal.name || goalInteractor.getGoalByName(name) == null

    override suspend fun canDelete() = true

    override suspend fun delete() = goalInteractor.deleteGoal(editedGoal)
    override suspend fun confirm(goal: Goal, categories: Set<Category>) {
        goalInteractor.editGoal(editedGoal, goal)
        val detachedCategories = foundCategories - categories
        linksInteractor.detachCategoriesFromGoal(goal, *detachedCategories.toTypedArray())
        val newlyAttachedCategories = categories - foundCategories
        linksInteractor.attachNewCategoriesToGoal(goal, *newlyAttachedCategories.toTypedArray())
    }
}

private fun Goal.isNotEmpty(): Boolean = name.isNotBlank() && target != Amount.ZERO

private fun Goal.hasValidDateRange(): Boolean = start == null || finish == null || start!! < finish!!
