package com.tubetoast.envelopes.common.domain.models

import kotlin.math.min

fun Date.monthAsRange() =
    copy(day = 1)..copy(day = daysInThisMonth())

fun Date.yearAsRange() =
    copy(day = 1, month = 1)..copy(day = 31, month = 12)

infix operator fun Date.rangeTo(other: Date): DateRange =
    DateRange(other, this)

fun Date.daysInThisYear() =
    Calendar.daysOfYear(year)

fun Date.daysInThisMonth() =
    Calendar.daysByMonths(year)[month] ?: throw IllegalArgumentException("month $month")

fun Date.inDayOfYear(): Int {
    return Calendar.daysByMonths(year).entries.fold(0) { acc, (monthNumber, daysInIt) ->
        acc + when {
            (monthNumber < month) -> daysInIt
            monthNumber == month -> day
            else -> 0
        }
    }
}

fun Date.nextDay() = when (day) {
    daysInThisYear() -> copy(day = 1, month = 1, year = year + 1)
    daysInThisMonth() -> copy(day = 1, month = month + 1)
    else -> copy(day = day + 1)
}

fun Date.previousDay() = when {
    day == 1 && month == 1 -> copy(day = 31, month = 12, year = year - 1)
    day == 1 -> copy(day = Calendar.daysByMonths(year)[month - 1]!!, month = month - 1, year = year)
    else -> copy(day = day - 1)
}

fun Date.minusMonth() =
    when (month) {
        1 -> Date(
            day = min(day, 31),
            month = 12,
            year = year - 1
        )

        else -> Date(
            day = min(day, Calendar.daysByMonths(year)[month - 1]!!),
            month = month - 1,
            year = year
        )
    }

fun Date.plusMonth() =
    when (month) {
        12 -> Date(
            day = min(day, Calendar.daysByMonths(year + 1)[1]!!),
            month = 1,
            year = year + 1
        )

        else -> Date(
            day = min(day, Calendar.daysByMonths(year)[month + 1]!!),
            month = month + 1,
            year = year
        )
    }

object DateConverter {
    private const val DELIMITER = "/"
    fun String.toDate() = split(DELIMITER).map { it.toInt() }
        .let { (d, m, y) -> Date(day = d, month = m, year = y) }

    fun Date.fromDate() = "$day$DELIMITER$month$DELIMITER$year"
}

object Calendar {

    fun daysOfYear(year: Int) =
        if (year.isLeapYear()) 366 else 365

    fun daysByMonths(year: Int) =
        if (year.isLeapYear()) vYear else oYear

    private fun Int.isLeapYear() = mod(4) == 0

    private val vYear = createMap(isLeapYear = true)

    private val oYear = createMap()

    private fun createMap(isLeapYear: Boolean = false) = mutableMapOf<Int, Int>().apply {
        arrayOf(1, 3, 5, 7, 8, 10, 12).forEach { put(it, 31) }
        arrayOf(4, 6, 9, 11).forEach { put(it, 30) }
        put(2, if (isLeapYear) 29 else 28)
    }
}
