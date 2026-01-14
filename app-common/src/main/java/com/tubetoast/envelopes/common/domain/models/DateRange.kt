package com.tubetoast.envelopes.common.domain.models

data class DateRange(
    override val start: Date,
    override val endInclusive: Date
) : ClosedRange<Date>, Comparable<DateRange> {
    override fun compareTo(other: DateRange) = when { // less means earlier
        other.start == start && other.endInclusive == endInclusive -> 0
        other.start < start && other.endInclusive < endInclusive -> 1
        other.start > start && other.endInclusive > endInclusive -> -1
        else -> throw IllegalStateException("Cannot compare $this to $other")
    }
}
