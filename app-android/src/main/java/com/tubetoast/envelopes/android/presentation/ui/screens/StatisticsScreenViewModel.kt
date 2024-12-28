package com.tubetoast.envelopes.android.presentation.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tubetoast.envelopes.common.domain.AverageCalculator
import com.tubetoast.envelopes.common.domain.SpendingInteractor
import com.tubetoast.envelopes.common.domain.models.Date.Companion.today
import com.tubetoast.envelopes.common.domain.models.inMonths
import com.tubetoast.envelopes.common.domain.models.rangeTo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch

class StatisticsScreenViewModel(
    private val averageCalculator: AverageCalculator,
    private val spendingInteractor: SpendingInteractor
) : ViewModel() {

    val displayedPeriod = MutableStateFlow("")
    val displayedAverageInMonth = MutableStateFlow(0L)
    val displayedAverageInYear = MutableStateFlow(0L)
    private val isPeriodInMonths = MutableStateFlow(true)
    private val periodInMonths = MutableStateFlow(6)
    private var maxMonths: Int = 0

    init {
        viewModelScope.launch {
            merge(periodInMonths, isPeriodInMonths).collect {
                displayedPeriod.value = if (isPeriodInMonths.value) {
                    val months = periodInMonths.value
                    if (months > 1) "last $months months" else "last month"
                } else {
                    val years = periodInMonths.value / 12
                    if (years > 1) "last $years years" else "last year"
                }
                displayedAverageInMonth.value = averageCalculator.calculateAverage(periodInMonths.value).units
                displayedAverageInYear.value = displayedAverageInMonth.value * 12
            }
        }
        viewModelScope.launch {
            maxMonths = spendingInteractor.getEarliestSpending()
                .date
                .rangeTo(today())
                .inMonths()
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
