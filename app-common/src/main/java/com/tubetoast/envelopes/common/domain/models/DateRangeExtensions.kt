package com.tubetoast.envelopes.common.domain.models

fun DateRange.previousMonth() = run {
    changeMonth(Date::minusMonth)
}

fun DateRange.nextMonth() = run {
    changeMonth(Date::plusMonth)
}

private fun DateRange.changeMonth(change: Date.((month: Int, year: Int) -> Int) -> Date): DateRange {
    require(start.month == endInclusive.month && start.year == endInclusive.year) {
        this.toString()
    }
    val newStart = start.change { _, _ -> 1 }
    val newEnd = endInclusive.change { m, y -> Calendar.daysByMonths(y)[m]!! }
    return copy(endInclusive = newEnd, start = newStart)
}

fun DateRange.previousYear() = run {
    changeYear(start.year - 1)
}

fun DateRange.nextYear() = run {
    changeYear(start.year + 1)
}

private fun DateRange.changeYear(year: Int): DateRange {
    require(start.year == endInclusive.year)
    return Date(1, 1, year)..Date(day = 31, month = 12, year = year)
}

private fun Date.minusMonth(day: (month: Int, year: Int) -> Int) =
    when (month) {
        1 -> Date(
            day = day(12, year - 1),
            month = 12,
            year = year - 1
        )

        else -> Date(
            day = day(month - 1, year),
            month = month - 1,
            year = year
        )
    }

private fun Date.plusMonth(day: (month: Int, year: Int) -> Int) =
    when (month) {
        12 -> Date(
            day = day(1, year + 1),
            month = 1,
            year = year + 1
        )

        else -> Date(
            day = day(month + 1, year),
            month = month + 1,
            year = year
        )
    }
