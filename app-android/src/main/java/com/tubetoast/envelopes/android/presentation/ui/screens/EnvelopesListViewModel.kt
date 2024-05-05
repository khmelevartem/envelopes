package com.tubetoast.envelopes.android.presentation.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tubetoast.envelopes.common.domain.EnvelopeInteractor
import com.tubetoast.envelopes.common.domain.SnapshotsInteractor
import com.tubetoast.envelopes.common.domain.models.Date
import com.tubetoast.envelopes.common.domain.models.DateRange
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.nextMonth
import com.tubetoast.envelopes.common.domain.models.nextYear
import com.tubetoast.envelopes.common.domain.models.previousMonth
import com.tubetoast.envelopes.common.domain.models.previousYear
import com.tubetoast.envelopes.common.domain.snapshots.EnvelopeSnapshot
import com.tubetoast.envelopes.common.settings.Setting
import com.tubetoast.envelopes.common.settings.SettingsRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class EnvelopesListViewModel(
    private val snapshotsInteractor: SnapshotsInteractor,
    private val envelopeInteractor: EnvelopeInteractor,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private var job: Job? = null
    private lateinit var producerScope: ProducerScope<Iterable<EnvelopeSnapshot>>

    private val _displayedPeriod =
        MutableStateFlow(if (filterByYear) Date.currentYear() else Date.currentMonth())

    val filterByYear get() = settingsRepository.getSetting(Setting.Key.FILTER_BY_YEAR).checked

    val displayedPeriod: Flow<String>
        get() = _displayedPeriod.map {
            if (filterByYear) {
                it.start.year.mod(2000).toString()
            } else {
                it.start.run { "$month/${year.mod(2000)}" }
            }
        }

    val itemModels: Flow<Iterable<EnvelopeSnapshot>> = callbackFlow {
        producerScope = this
        startCollecting()
        awaitClose {
            stop()
        }
    }

    fun delete(envelope: Envelope) {
        viewModelScope.launch {
            envelopeInteractor.deleteEnvelope(envelope)
        }
    }

    fun nextPeriod() {
        changePeriod {
            if (filterByYear) nextYear() else nextMonth()
        }
    }

    fun previousPeriod() {
        changePeriod {
            if (filterByYear) previousYear() else previousMonth()
        }
    }

    private fun changePeriod(change: DateRange.() -> DateRange) {
        stop()
        _displayedPeriod.apply { value = value.change() }
        startCollecting()
    }

    private fun stop() {
        job?.cancel()
        job = null
    }

    private fun startCollecting(dateRange: DateRange = _displayedPeriod.value) {
        job = producerScope.launch {
            snapshotsInteractor.snapshotsByDatesFlow(dateRange)
                .collect { producerScope.trySendBlocking(it) }
        }
    }
}
