package com.tubetoast.envelopes.android.presentation.ui.screens

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tubetoast.envelopes.common.data.EnvelopesRepositoryWithUndefinedCategories.Companion.undefinedCategoriesEnvelope
import com.tubetoast.envelopes.common.domain.EnvelopeInteractor
import com.tubetoast.envelopes.common.domain.SnapshotsInteractor
import com.tubetoast.envelopes.common.domain.models.Date
import com.tubetoast.envelopes.common.domain.models.DateRange
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.snapshots.EnvelopeSnapshot
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class EnvelopesListViewModel(
    private val snapshotsInteractor: SnapshotsInteractor,
    private val envelopeInteractor: EnvelopeInteractor
) : ViewModel() {

    private var job: Job? = null
    private lateinit var producerScope: ProducerScope<List<EnvelopeSnapshot>>
    private val _displayedMonth = mutableStateOf(Date.currentMonth())
    val displayedMonth: State<DateRange> get() = _displayedMonth

    fun delete(envelope: Envelope) {
        viewModelScope.launch {
            envelopeInteractor.deleteEnvelope(envelope)
        }
    }

    fun changeMonth(change: DateRange.() -> DateRange) {
        stop()
        _displayedMonth.apply { value = value.change() }
        startCollecting()
    }

    val itemModels: Flow<List<EnvelopeSnapshot>> = callbackFlow {
        producerScope = this
        startCollecting()
        awaitClose {
            stop()
        }
    }

    private fun startCollecting(dateRange: DateRange = _displayedMonth.value) {
        job = producerScope.launch {
            snapshotsInteractor.snapshotsByDatesFlow(dateRange)
                .map { it.filterEmptyCategories() }.collect {
                    producerScope.trySendBlocking(it)
                }
        }
    }

    private fun stop() {
        job?.cancel()
        job = null
    }

    private fun Set<EnvelopeSnapshot>.filterEmptyCategories() = mapNotNull { snapshot ->
        val nonEmptyCategories = snapshot.categories.filter { category ->
            category.isNotEmpty()
        }
        if (nonEmptyCategories.size != snapshot.categories.size) {
            snapshot.copy(categories = nonEmptyCategories)
        } else if (snapshot.envelope.id == undefinedCategoriesEnvelope.id && nonEmptyCategories.isEmpty()) {
            null
        } else {
            snapshot
        }
    }
}
