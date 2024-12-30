package com.tubetoast.envelopes.common.domain.models

fun DateRange.previousMonth() = run {
    anotherMonth { minusMonth() }
}

fun DateRange.nextMonth() = run {
    anotherMonth { plusMonth() }
}

private fun DateRange.anotherMonth(change: Date.() -> Date): DateRange {
    require(start.month == endInclusive.month && start.year == endInclusive.year) {
        this.toString()
    }
    val aDayInOtherMonth = start.change()
    val newStart = aDayInOtherMonth.copy(day = 1)
    val newEnd = aDayInOtherMonth.copy(day = aDayInOtherMonth.daysInThisMonth())
    return copy(endInclusive = newEnd, start = newStart)
}

fun DateRange.previousYear() = run {
    anotherYear { this - 1 }
}

fun DateRange.nextYear() = run {
    anotherYear { this + 1 }
}

private fun DateRange.anotherYear(yearCalculate: Int.() -> Int): DateRange {
    require(start.year == endInclusive.year)
    val newYear = yearCalculate(start.year)
    return Date(1, 1, newYear)..Date(day = 31, month = 12, year = newYear)
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
    val diffInDays = endInclusive.inDayOfYear() + startYearDays - start.inDayOfYear()
    return diffInDays.div(365 / 12)
}
