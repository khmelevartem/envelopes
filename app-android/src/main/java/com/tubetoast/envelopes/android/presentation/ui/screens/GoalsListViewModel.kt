package com.tubetoast.envelopes.android.presentation.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tubetoast.envelopes.common.domain.GoalInteractor
import com.tubetoast.envelopes.common.domain.models.Goal
import kotlinx.coroutines.launch

class GoalsListViewModel(
    private val goalInteractor: GoalInteractor
) : ViewModel() {
    val goals = goalInteractor.goalsFlow

    fun delete(goal: Goal) {
        viewModelScope.launch {
            goalInteractor.deleteGoal(goal)
        }
    }
}
