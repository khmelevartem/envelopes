package com.tubetoast.envelopes.common.domain.models

fun DateRange.previousMonth() = run {
    anotherMonth(Date::minusMonth)
}

fun DateRange.nextMonth() = run {
    anotherMonth(Date::plusMonth)
}

private fun DateRange.anotherMonth(change: Date.((newMonth: Int, newYear: Int) -> /* newDay: */ Int) -> Date): DateRange {
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

fun DateRange.inMonths(): Int {
    require(endInclusive > start)
    val yearDiffers = endInclusive.year - start.year != 0
    val startYearDays = if (yearDiffers) {
        (start.year until endInclusive.year).fold(0) { acc, year ->
            acc + Calendar.daysOfYear(year)
        }
    } else {
        0
    }
    val diffInDays = endInclusive.inDaysOfYear() + startYearDays - start.inDaysOfYear()
    return diffInDays.div(365 / 12)
}
