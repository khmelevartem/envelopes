package com.tubetoast.envelopes.ui.presentation.ui.screens

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tubetoast.envelopes.common.domain.EnvelopeInteractor
import com.tubetoast.envelopes.common.domain.SelectedPeriodRepository
import com.tubetoast.envelopes.common.domain.SnapshotsInteractor
import com.tubetoast.envelopes.common.domain.models.Amount
import com.tubetoast.envelopes.common.domain.models.Date
import com.tubetoast.envelopes.common.domain.models.DateRange
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.summarize
import com.tubetoast.envelopes.common.domain.snapshots.EnvelopeSnapshot
import com.tubetoast.envelopes.common.settings.MutableSettingsRepository
import com.tubetoast.envelopes.common.settings.Setting
import com.tubetoast.envelopes.ui.presentation.ui.screens.models.EnvelopePaceInfo
import com.tubetoast.envelopes.ui.presentation.ui.screens.models.MainSummaryInfo
import com.tubetoast.envelopes.ui.presentation.ui.screens.models.PaceStatus
import com.tubetoast.envelopes.ui.presentation.ui.theme.EColor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EnvelopesListViewModel(
    snapshotsInteractor: SnapshotsInteractor,
    private val envelopeInteractor: EnvelopeInteractor,
    settingsRepository: MutableSettingsRepository,
    selectedPeriodRepository: SelectedPeriodRepository
) : ViewModel() {
    private val today: Date
        get() = Date.today()

    val filterByYear = settingsRepository
        .getSettingFlow(Setting.Key.FILTER_BY_YEAR)
        .map { it.checked }
        .stateIn(
            viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = settingsRepository.getSetting(Setting.Key.FILTER_BY_YEAR).checked
        )

    val snapshotFlow: Flow<Iterable<EnvelopeSnapshot>> = snapshotsInteractor
        .envelopeSnapshots(selectedPeriodRepository.selectedPeriodFlow)

    val elapsedPercentage = selectedPeriodRepository.selectedPeriodFlow
        .map { period ->
            calculateElapsedPercentage(period, today)
        }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            initialValue = 0f
        )

    private val mainSummaryFlow = combine(
        snapshotFlow,
        filterByYear,
        elapsedPercentage
    ) { snapshots, filterByYear, elapsedPercentage ->
        calculateMainSummary(snapshots, filterByYear, elapsedPercentage)
    }

    val mainSummary = mainSummaryFlow.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        initialValue = createInitialMainSummary()
    )

    private val envelopePaceInfosFlow = combine(
        snapshotFlow,
        filterByYear,
        elapsedPercentage
    ) { snapshots, filterByYear, elapsedPercentage ->
        snapshots
            .map { snapshot ->
                calculateEnvelopePaceInfo(snapshot, filterByYear, elapsedPercentage)
            }.sortedByDescending { it.paceColor.value.toLong() }
    }

    val envelopePaceInfos = envelopePaceInfosFlow.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        initialValue = emptyList()
    )

    private fun createInitialMainSummary(): MainSummaryInfo {
        val mainSummaryInfo = MainSummaryInfo(
            totalSpent = Amount.ZERO,
            totalLimit = Amount.ZERO,
            spentPercentage = 0f,
            elapsedPercentage = 0f,
            paceColor = EColor.PaceMint,
            remaining = 0
        )
        return mainSummaryInfo
    }

    private fun calculateElapsedPercentage(
        period: DateRange,
        today: Date
    ): Float {
        val start = period.start.toLocalDate()
        val end = period.endInclusive.toLocalDate()
        val now = today.toLocalDate()

        if (now < start) return 0f
        if (now > end) return 1f

        val totalDuration = end.toEpochDays() - start.toEpochDays()
        val elapsedDuration = now.toEpochDays() - start.toEpochDays()

        return if (totalDuration > 0) {
            (elapsedDuration.toFloat() / totalDuration).coerceIn(0f, 1f)
        } else {
            1f
        }
    }

    private fun calculateMainSummary(
        snapshots: Iterable<EnvelopeSnapshot>,
        filterByYear: Boolean,
        elapsedPercentage: Float
    ): MainSummaryInfo {
        val totalSpent = snapshots.map { it.sum }.summarize()
        val totalLimit = snapshots.map { if (filterByYear) it.envelope.yearLimit else it.envelope.limit }.summarize()
        val spentPercentage = if (totalLimit.units > 0) totalSpent / totalLimit else 0f
        val paceColor = getPaceColor(spentPercentage, elapsedPercentage)
        val remaining = (totalLimit.units - totalSpent.units).coerceAtLeast(0)

        return MainSummaryInfo(
            totalSpent = totalSpent,
            totalLimit = totalLimit,
            spentPercentage = spentPercentage,
            elapsedPercentage = elapsedPercentage,
            paceColor = paceColor,
            remaining = remaining
        )
    }

    private fun calculateEnvelopePaceInfo(
        snapshot: EnvelopeSnapshot,
        filterByYear: Boolean,
        elapsedPercentage: Float
    ): EnvelopePaceInfo {
        val limit = if (filterByYear) snapshot.envelope.yearLimit else snapshot.envelope.limit
        val percentage = if (limit.units != 0L) snapshot.sum / limit else 0f
        val paceColor = getPaceColor(percentage, elapsedPercentage)

        val status = when {
            percentage > 1.0f -> PaceStatus.Exceeded(-(limit.units - snapshot.sum.units))
            percentage > elapsedPercentage + 0.1f -> PaceStatus.AbovePace
            percentage >= elapsedPercentage - 0.05f -> PaceStatus.OnTrack
            else -> PaceStatus.Remaining(limit.units - snapshot.sum.units)
        }

        return EnvelopePaceInfo(
            envelope = snapshot.envelope,
            sum = snapshot.sum,
            limit = limit,
            percentage = percentage,
            elapsedPercentage = elapsedPercentage,
            paceColor = paceColor,
            status = status
        )
    }

    private fun getPaceColor(
        spendingPercentage: Float,
        elapsedPercentage: Float
    ): Color =
        when {
            spendingPercentage > 1.0f || spendingPercentage > elapsedPercentage + 0.15f -> EColor.PaceCoral
            spendingPercentage > elapsedPercentage -> EColor.PaceAmber
            else -> EColor.PaceMint
        }

    fun delete(envelope: Envelope) {
        viewModelScope.launch {
            envelopeInteractor.deleteEnvelope(envelope)
        }
    }
}
