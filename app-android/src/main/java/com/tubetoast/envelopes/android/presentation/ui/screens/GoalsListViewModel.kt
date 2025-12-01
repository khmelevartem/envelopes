package com.tubetoast.envelopes.android.presentation.ui.screens

import androidx.lifecycle.ViewModel
import com.tubetoast.envelopes.common.domain.GoalInteractor

class GoalsListViewModel(
    private val goalInteractor: GoalInteractor
) : ViewModel() {
    val goals = goalInteractor.goalsFlow
}
