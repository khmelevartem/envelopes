package com.tubetoast.envelopes.common.domain.models

data class DateRange(
    override val endInclusive: Date,
    override val start: Date
) : ClosedRange<Date>
