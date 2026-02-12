package com.tubetoast.envelopes.ui.presentation.ui.screens.models

import androidx.compose.ui.graphics.Color
import com.tubetoast.envelopes.common.domain.models.Amount
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.snapshots.EnvelopeSnapshot
import com.tubetoast.envelopes.ui.presentation.ui.theme.EColor

sealed class PaceStatus {
    data class Exceeded(
        val amount: Long
    ) : PaceStatus()

    object AbovePace : PaceStatus()

    object OnTrack : PaceStatus()

    data class Remaining(
        val amount: Long
    ) : PaceStatus()
}

data class EnvelopePaceInfo(
    val envelope: Envelope,
    val sum: Amount,
    val limit: Amount,
    val percentage: Float,
    val elapsedPercentage: Float,
    val paceColor: Color,
    val status: PaceStatus
)

data class MainSummaryInfo(
    val totalSpent: Amount,
    val totalLimit: Amount,
    val spentPercentage: Float,
    val elapsedPercentage: Float,
    val paceColor: Color,
    val remaining: Long
)

fun EnvelopeSnapshot.toPaceInfo(
    elapsedPercentage: Float,
    paceMint: Color,
    paceAmber: Color,
    paceCoral: Color
): EnvelopePaceInfo {
    val limit = envelope.limit
    val percentage = if (limit.units != 0L) sum / limit else 0f
    val paceColor = getPaceColor(percentage, elapsedPercentage, paceMint, paceAmber, paceCoral)
    val remaining = limit.units - sum.units

    val status = when {
        percentage > 1.0f -> PaceStatus.Exceeded(-remaining)
        percentage > elapsedPercentage + 0.1f -> PaceStatus.AbovePace
        percentage >= elapsedPercentage - 0.05f -> PaceStatus.OnTrack
        else -> PaceStatus.Remaining(remaining)
    }

    return EnvelopePaceInfo(
        envelope = envelope,
        sum = sum,
        limit = limit,
        percentage = percentage,
        elapsedPercentage = elapsedPercentage,
        paceColor = paceColor,
        status = status
    )
}

fun getPaceColor(
    spendingPercentage: Float,
    elapsedPercentage: Float
): Color = getPaceColor(spendingPercentage, elapsedPercentage, EColor.PaceMint, EColor.PaceAmber, EColor.PaceCoral)

private fun getPaceColor(
    spendingPercentage: Float,
    elapsedPercentage: Float,
    paceMint: Color,
    paceAmber: Color,
    paceCoral: Color
): Color =
    when {
        spendingPercentage > 1.0f || spendingPercentage > elapsedPercentage + 0.15f -> paceCoral
        spendingPercentage > elapsedPercentage -> paceAmber
        else -> paceMint
    }
