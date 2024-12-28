package com.tubetoast.envelopes.android.presentation.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tubetoast.envelopes.android.presentation.models.ChoosableEnvelope
import com.tubetoast.envelopes.common.domain.AverageCalculator
import com.tubetoast.envelopes.common.domain.EnvelopeInteractor
import com.tubetoast.envelopes.common.domain.SpendingInteractor
import com.tubetoast.envelopes.common.domain.models.Date.Companion.today
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.inMonths
import com.tubetoast.envelopes.common.domain.models.rangeTo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch

class StatisticsScreenViewModel(
    private val averageCalculator: AverageCalculator,
    private val spendingInteractor: SpendingInteractor,
    private val envelopeInteractor: EnvelopeInteractor
) : ViewModel() {

    val displayedPeriod = MutableStateFlow("")
    val displayedAverageInMonth = MutableStateFlow(0L)
    val displayedAverageInYear = MutableStateFlow(0L)
    private val envelopes = MutableStateFlow(emptyList<ChoosableEnvelope>())
    private val showFilter = MutableStateFlow(false)
    val envelopesFilter = MutableStateFlow(emptyList<ChoosableEnvelope>())
    private val isPeriodInMonths = MutableStateFlow(true)
    private val periodInMonths = MutableStateFlow(6)
    private var maxMonths: Int = 0

    init {
        viewModelScope.launch {
            // showProgress()

            envelopes.value = envelopeInteractor.getAll().map {
                ChoosableEnvelope(it, true)
            }

            maxMonths = spendingInteractor.getEarliestSpending()
                .date
                .rangeTo(today())
                .inMonths()

            launch {
                merge(periodInMonths, isPeriodInMonths, envelopes).collect {
                    displayedPeriod.value = if (isPeriodInMonths.value) {
                        val months = periodInMonths.value
                        if (months > 1) "last $months months" else "last month"
                    } else {
                        val years = periodInMonths.value / 12
                        if (years > 1) "last $years years" else "last year"
                    }
                    displayedAverageInMonth.value =
                        averageCalculator.calculateAverage(periodInMonths.value) { snapshot ->
                            envelopes.value.find { it.envelope == snapshot.envelope }
                                ?.isChosen ?: false
                        }.units
                    displayedAverageInYear.value = displayedAverageInMonth.value * 12
                }
            }

            launch {
                merge(envelopes, showFilter).collect {
                    envelopesFilter.value = if (showFilter.value) envelopes.value else emptyList()
                }
            }

            // hideProgress()
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

    fun toggleEnvelopesFilter(envelope: Envelope) {
        envelopes.value = envelopes.value.map {
            if (it.envelope != envelope) it else it.copy(isChosen = it.isChosen.not())
        }
    }

    fun toggleShowFilter() {
        showFilter.value = !showFilter.value
    }
}
