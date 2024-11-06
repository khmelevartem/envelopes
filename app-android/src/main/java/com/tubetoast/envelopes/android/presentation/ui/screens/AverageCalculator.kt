package com.tubetoast.envelopes.android.presentation.ui.screens

import com.tubetoast.envelopes.common.domain.SnapshotsInteractor
import com.tubetoast.envelopes.common.domain.models.Date.Companion.toDate
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.sum
import java.util.Calendar

class AverageCalculator(
    private val snapshotsInteractor: SnapshotsInteractor
) {
    fun calculateAverageFor(months: Int, envelope: Envelope): String {
        val startDate = Calendar.getInstance().apply {
            add(Calendar.MONTH, -months)
        }.toDate()

        return (
            snapshotsInteractor.allSnapshots.find {
                it.envelope == envelope
            }?.categories?.flatMap { it.transactions }?.filter {
                it.date > startDate
            }?.map { it.amount }?.sum()?.units?.div(months)
            )
            .toString()
    }
}
