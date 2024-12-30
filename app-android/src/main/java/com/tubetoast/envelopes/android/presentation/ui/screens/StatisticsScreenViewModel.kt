package com.tubetoast.envelopes.android.presentation.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tubetoast.envelopes.android.presentation.models.ChoosableEnvelope
import com.tubetoast.envelopes.common.domain.AverageCalculator
import com.tubetoast.envelopes.common.domain.InflationCalculator
import com.tubetoast.envelopes.common.domain.SpendingInteractor
import com.tubetoast.envelopes.common.domain.models.Date.Companion.today
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.inMonths
import com.tubetoast.envelopes.common.domain.models.rangeTo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch

class InflationViewModel(
    private val selectedEnvelopesRepository: SelectedEnvelopesRepository,
    private val inflationCalculator: InflationCalculator
) : ViewModel() {

    private val defaultInflationData = listOf(0)
    val years = MutableStateFlow(listOf(today().year))
    val months = MutableStateFlow(listOf(today().month))
    val inflationByYearsData = MutableStateFlow(defaultInflationData)
    val inflationAverageByYears = MutableStateFlow(0)
    val inflationAverageByMonths = MutableStateFlow(0)
    val inflationByMonthsData = MutableStateFlow(defaultInflationData)

    init {
        viewModelScope.launch {
            selectedEnvelopesRepository.selectedEnvelopes.collect {
                launch(Dispatchers.IO) {
                    val data = inflationCalculator.calculateInflationByYears(
                        filter = selectedEnvelopesRepository::isChosen
                    )
                    inflationByYearsData.value = data
                        .map { (it.second * 100).toInt() }.ifEmpty { defaultInflationData }
                    years.value = data
                        .map { it.first.start.year }

                    inflationAverageByYears.value = inflationByYearsData.value.average().toInt()
                }
                launch(Dispatchers.IO) {

                    val data = inflationCalculator.calculateInflationByMonths(
                        filter = selectedEnvelopesRepository::isChosen
                    )
                    inflationByMonthsData.value = data
                        .map { (it.second * 100).toInt() }

                    months.value = data
                        .map { it.first.start.month }

                    inflationAverageByMonths.value = inflationByMonthsData.value.average().toInt()
                }
            }
        }
    }
}

class EnvelopesFilterViewModel(
    private val selectedEnvelopesRepository: SelectedEnvelopesRepository
) : ViewModel() {
    private val showFilter = MutableStateFlow(false)
    val displayedEnvelopes = MutableStateFlow(emptyList<ChoosableEnvelope>())

    init {
        viewModelScope.launch {
            merge(selectedEnvelopesRepository.selectedEnvelopes, showFilter).collect {
                displayedEnvelopes.value = if (showFilter.value) {
                    selectedEnvelopesRepository.selectedEnvelopes.value.toList()
                } else {
                    emptyList()
                }
            }
        }
    }

    fun toggleEnvelopesFilter(envelope: Envelope) {
        selectedEnvelopesRepository.changeSelection {
            map {
                if (it.envelope != envelope) it else it.copy(isChosen = it.isChosen.not())
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
    private val isPeriodInMonths = MutableStateFlow(true)
    private val periodInMonths = MutableStateFlow(6)
    private var maxMonths: Int = 0

    init {
        viewModelScope.launch {

            maxMonths = spendingInteractor.getEarliestSpending()
                .date
                .rangeTo(today())
                .inMonths()

            launch {
                merge(periodInMonths, isPeriodInMonths, selectedEnvelopes.selectedEnvelopes).collect {
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
                }
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
