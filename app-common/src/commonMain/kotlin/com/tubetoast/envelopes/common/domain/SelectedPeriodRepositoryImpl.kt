package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.domain.models.Date
import com.tubetoast.envelopes.common.domain.models.DateRange
import com.tubetoast.envelopes.common.domain.models.monthAsRange
import com.tubetoast.envelopes.common.domain.models.yearAsRange
import com.tubetoast.envelopes.common.settings.Setting
import com.tubetoast.envelopes.common.settings.SettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SelectedPeriodRepositoryImpl(
    private val settingsRepository: SettingsRepository
) : SelectedPeriodRepository {
    private val scope = CoroutineScope(Job())
    private val filterByYear get() = settingsRepository.getSetting(Setting.Key.FILTER_BY_YEAR).checked
    override val selectedPeriodFlow by lazy {
        MutableStateFlow(
            if (filterByYear) Date.currentYear() else Date.currentMonth()
        )
    }

    private var lastViewedMonth = selectedPeriodFlow.value.start.month

    override fun changePeriod(change: DateRange.() -> DateRange) {
        selectedPeriodFlow.apply { value = value.change() }
    }

    init {
        scope.launch {
            settingsRepository.getSettingFlow(Setting.Key.FILTER_BY_YEAR).collect { filterByYear ->
                selectedPeriodFlow.value = selectedPeriodFlow.value.start.let { current ->
                    if (filterByYear.checked) {
                        lastViewedMonth = current.month
                        current.yearAsRange()
                    } else {
                        Date(day = current.day, month = lastViewedMonth, year = current.year)
                            .monthAsRange()
                    }
                }
            }
        }
    }
}
