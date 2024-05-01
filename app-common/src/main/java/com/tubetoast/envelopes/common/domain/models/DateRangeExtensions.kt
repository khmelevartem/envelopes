package com.tubetoast.envelopes.common.domain.models

fun DateRange.previousMonth() = run {
    require(start.month == endInclusive.month && start.year == endInclusive.year)
    val newStart = start.minusMonth { _, _ -> 1 }
    val newEnd = endInclusive.minusMonth { m, y -> Calendar.daysByMonths(y)[m]!! }
    copy(endInclusive = newEnd, start = newStart)
}


fun DateRange.nextMonth() = run {
    require(start.month == endInclusive.month && start.year == endInclusive.year)
    val newStart = start.plusMonth { _, _ -> 1 }
    val newEnd = endInclusive.plusMonth { m, y -> Calendar.daysByMonths(y)[m]!! }
    copy(endInclusive = newEnd, start = newStart)
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
