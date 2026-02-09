package com.tubetoast.envelopes.ui.presentation.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tubetoast.envelopes.common.domain.GoalInteractor
import com.tubetoast.envelopes.common.domain.GoalSnapshotInteractor
import com.tubetoast.envelopes.common.domain.models.Goal
import com.tubetoast.envelopes.common.settings.Setting
import com.tubetoast.envelopes.common.settings.SettingsRepository
import kotlinx.coroutines.launch

class GoalsListViewModel(
    private val goalInteractor: GoalInteractor,
    private val goalSnapshotInteractor: GoalSnapshotInteractor,
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    val goals = goalSnapshotInteractor.goalSnapshots

    val isColorful = settingsRepository.getSettingFlow(Setting.Key.COLORFUL)

    fun delete(goal: Goal) {
        viewModelScope.launch {
            goalInteractor.deleteGoal(goal)
        }
    }
}
