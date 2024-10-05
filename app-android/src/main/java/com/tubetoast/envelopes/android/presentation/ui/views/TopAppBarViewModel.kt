package com.tubetoast.envelopes.android.presentation.ui.views

import androidx.lifecycle.ViewModel
import com.tubetoast.envelopes.android.presentation.state.SelectedPeriodRepository
import com.tubetoast.envelopes.common.domain.models.Date.Companion.today
import com.tubetoast.envelopes.common.domain.models.nextMonth
import com.tubetoast.envelopes.common.domain.models.nextYear
import com.tubetoast.envelopes.common.domain.models.previousMonth
import com.tubetoast.envelopes.common.domain.models.previousYear
import com.tubetoast.envelopes.common.settings.MutableSettingsRepository
import com.tubetoast.envelopes.common.settings.Setting
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TopAppBarViewModel(
    private val settingsRepository: MutableSettingsRepository,
    private val selectedPeriodRepository: SelectedPeriodRepository
) : ViewModel() {

    private val _filterByYear = settingsRepository.getSettingFlow(Setting.Key.FILTER_BY_YEAR)
    private val filterByYear get() = _filterByYear.value.checked

    private val _displayedPeriod by lazy { selectedPeriodRepository.selectedPeriodFlow }

    val displayedPeriod: Flow<String>
        get() = _displayedPeriod.map {
            if (filterByYear) {
                it.start.year.mod(2000).toString()
            } else {
                it.start.run { "$month/${year.mod(2000)}" }
            }
        }

    fun changePeriodType() {
        settingsRepository.saveChanges(
            listOf(
                _filterByYear.value.run { copy(checked = !checked) }
            )
        )
    }

    fun nextPeriod() = selectedPeriodRepository.run {
        val today = today()
        _displayedPeriod.value.apply {
            if (filterByYear) {
                if (start.year < today.year) changePeriod { nextYear() }
            } else {
                if (start.year < today.year || start.month < today.month) changePeriod { nextMonth() }
            }
        }
    }


    fun previousPeriod() {
        selectedPeriodRepository.changePeriod {
            if (filterByYear) previousYear() else previousMonth()
        }
    }

}
