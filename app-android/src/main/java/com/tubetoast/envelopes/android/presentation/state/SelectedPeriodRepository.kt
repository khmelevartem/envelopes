package com.tubetoast.envelopes.android.presentation.state

import com.tubetoast.envelopes.common.domain.models.DateRange
import kotlinx.coroutines.flow.StateFlow

interface SelectedPeriodRepository {

    val selectedPeriodFlow: StateFlow<DateRange>

    fun changePeriod(change: DateRange.() -> DateRange)

}
