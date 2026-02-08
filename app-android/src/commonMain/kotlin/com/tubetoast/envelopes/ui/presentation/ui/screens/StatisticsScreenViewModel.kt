package com.tubetoast.envelopes.ui.presentation.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tubetoast.envelopes.common.domain.AverageCalculator
import com.tubetoast.envelopes.common.domain.InflationCalculator
import com.tubetoast.envelopes.common.domain.SelectedEnvelopesRepository
import com.tubetoast.envelopes.common.domain.SpendingInteractor
import com.tubetoast.envelopes.common.domain.isChosen
import com.tubetoast.envelopes.common.domain.models.Date.Companion.today
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.SelectableEnvelope
import com.tubetoast.envelopes.common.domain.models.inMonths
import com.tubetoast.envelopes.common.settings.Setting
import com.tubetoast.envelopes.common.settings.SettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch

class InflationViewModel(
    private val selectedEnvelopesRepository: SelectedEnvelopesRepository,
    private val inflationCalculator: InflationCalculator,
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    private val defaultInflationData = listOf(0)
    val years = MutableStateFlow(listOf(today().year))
    val inflationByYearsData = MutableStateFlow(defaultInflationData)
    val inflationAverageByYears = MutableStateFlow(0)

    init {
        val percentCeiling = 500
        viewModelScope.launch {
            selectedEnvelopesRepository.items.collect {
                val limitInflation = settingsRepository.getSetting(Setting.Key.LIMIT_INFLATION).checked
                launch(Dispatchers.IO) {
                    val data = inflationCalculator.calculateInflationByYears(
                        filter = selectedEnvelopesRepository::isChosen
                    )
                    inflationByYearsData.value = data
                        .map { (it.second * 100).toInt() }
                        .map { if (limitInflation) it.coerceIn(-percentCeiling, percentCeiling) else it }
                        .ifEmpty { defaultInflationData }
                    years.value = data
                        .map { it.first.start.year }

                    inflationAverageByYears.value = inflationByYearsData.value.average().toInt()
                }
            }
        }
    }
}

class EnvelopesFilterViewModel(
    private val selectedEnvelopesRepository: SelectedEnvelopesRepository
) : ViewModel() {
    private val showFilter = MutableStateFlow(false)
    val displayedEnvelopes = MutableStateFlow(emptyList<SelectableEnvelope>())

    init {
        viewModelScope.launch {
            merge(selectedEnvelopesRepository.items, showFilter).collect {
                displayedEnvelopes.value = if (showFilter.value) {
                    selectedEnvelopesRepository.items.value.toList()
                } else {
                    emptyList()
                }
            }
        }
    }

    fun toggleEnvelopesFilter(envelope: Envelope) {
        selectedEnvelopesRepository.changeSelection {
            map {
                if (it.item != envelope) it else it.copy(isSelected = it.isSelected.not())
            }.toSet()
        }
    }

    fun toggleShowFilter() {
        showFilter.value = !showFilter.value
    }
}

class AverageViewViewModel(
    private val averageCalculator: AverageCalculator,
    private val spendingInteractor: SpendingInteractor,
    private val selectedEnvelopes: SelectedEnvelopesRepository
) : ViewModel() {
    val displayedPeriod = MutableStateFlow("")
    val displayedAverageInMonth = MutableStateFlow(0L)
    val displayedAverageInYear = MutableStateFlow(0L)
    val movingAverage = MutableStateFlow(listOf(0L))
    private val isPeriodInMonths = MutableStateFlow(true)
    private val periodInMonths = MutableStateFlow(12)
    private var maxMonths: Int = 0

    init {
        viewModelScope.launch {

            maxMonths = (spendingInteractor.getEarliestSpending().date..today()).inMonths()

            launch {
                merge(periodInMonths, isPeriodInMonths, selectedEnvelopes.items).collect {
                    displayedPeriod.value = if (isPeriodInMonths.value) {
                        val months = periodInMonths.value
                        if (months > 1) "last $months months" else "last month"
                    } else {
                        val years = periodInMonths.value / 12
                        if (years > 1) "last $years years" else "last year"
                    }
                    displayedAverageInMonth.value =
                        averageCalculator.calculateAverage(periodInMonths.value, selectedEnvelopes::isChosen).units
                    displayedAverageInYear.value = displayedAverageInMonth.value * 12

                    movingAverage.value = averageCalculator
                        .calculateMovingAverage(
                            months = periodInMonths.value,
                            filter = selectedEnvelopes::isChosen
                        ).map { it.value.units }
                }
            }

            launch {
            }
        }
    }

    fun minusPeriod() {
        if (isPeriodInMonths.value) {
            if (periodInMonths.value > 1) {
                periodInMonths.value -= 1
            }
        } else {
            if (periodInMonths.value > 12) {
                periodInMonths.value -= 12
            }
        }
    }

    fun plusPeriod() {
        if (isPeriodInMonths.value) {
            if (periodInMonths.value < maxMonths) {
                periodInMonths.value += 1
            }
        } else {
            if (periodInMonths.value < maxMonths - 12) {
                periodInMonths.value += 12
            }
        }
    }

    fun changePeriodType() {
        isPeriodInMonths.value = !isPeriodInMonths.value
    }
}
