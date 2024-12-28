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
import com.tubetoast.envelopes.common.domain.models.monthAsRange
import com.tubetoast.envelopes.common.domain.models.rangeTo
import com.tubetoast.envelopes.common.domain.models.yearAsRange
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch

class InflationViewModel(
    private val selectedEnvelopesRepository: SelectedEnvelopesRepository,
    private val inflationCalculator: InflationCalculator
) : ViewModel() {
    var yearlyInflation = MutableStateFlow(0)
    var monthlyInflation = MutableStateFlow(0)

    init {
        viewModelScope.launch {
            selectedEnvelopesRepository.selectedEnvelopes.collect {
                monthlyInflation.value = (
                    inflationCalculator.calculateInflation(
                        today().monthAsRange(),
                        today().run {
                            if (month > 1) {
                                copy(month = month - 1)
                            } else {
                                copy(year = year - 1, month = 12)
                            }
                        }.monthAsRange()
                    ) { snapshot ->
                        selectedEnvelopesRepository.selectedEnvelopes.value.find { it.envelope == snapshot.envelope }
                            ?.isChosen ?: false
                    } * 100
                    ).toInt()

                yearlyInflation.value = (
                    inflationCalculator.calculateInflation(
                        today().yearAsRange(),
                        today().run { copy(year = year - 1) }.yearAsRange()
                    ) { snapshot ->
                        selectedEnvelopesRepository.selectedEnvelopes.value.find { it.envelope == snapshot.envelope }
                            ?.isChosen ?: false
                    } * 100
                    ).toInt()
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
                        averageCalculator.calculateAverage(periodInMonths.value) { snapshot ->
                            selectedEnvelopes.selectedEnvelopes.value.find { it.envelope == snapshot.envelope }
                                ?.isChosen ?: false
                        }.units
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
