package com.tubetoast.envelopes.common.domain.models

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.minus
import kotlinx.datetime.number
import kotlinx.datetime.plus
import kotlin.math.min

fun Date.monthAsRange() = copy(day = 1)..copy(day = daysInThisMonth())

fun Date.yearAsRange() = copy(day = 1, month = 1)..copy(day = 31, month = 12)

infix fun Date.dateRangeTo(other: Date): DateRange = DateRange(start = this, endInclusive = other)

fun Date.daysInThisYear(): Int = if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) 366 else 365

fun Date.daysInThisMonth(): Int =
    when (month) {
        2 -> if (daysInThisYear() == 366) 29 else 28
        4, 6, 9, 11 -> 30
        else -> 31
    }

fun Date.inDayOfYear(): Int = toLocalDate().dayOfYear

fun Date.nextDay(): Date {
    val next = toLocalDate().plus(1, DateTimeUnit.DAY)
    return Date(next.day, next.month.number, next.year)
}

fun Date.previousDay(): Date {
    val prev = toLocalDate().minus(1, DateTimeUnit.DAY)
    return Date(prev.day, prev.month.number, prev.year)
}

fun Date.plusMonth(): Date {
    val next = toLocalDate().plus(1, DateTimeUnit.MONTH)
    return Date(next.day, next.month.number, next.year)
}

fun Date.minusMonth(): Date {
    val prev = toLocalDate().minus(1, DateTimeUnit.MONTH)
    return Date(prev.day, prev.month.number, prev.year)
}

fun Date.plusYear() = changeYear(year + 1)

fun Date.minusYear() = changeYear(year - 1)

fun Date.changeYear(newYear: Int) =
    when {
        month == 2 && day == 29 -> copy(
            day = min(day, Calendar.daysByMonths(newYear)[month]!!),
            year = newYear
        )

        else -> copy(year = newYear)
    }

object DateConverter {
    private const val DELIMITER = "/"

    fun String.toDate() =
        split(DELIMITER)
            .map { it.toInt() }
            .let { (d, m, y) -> Date(day = d, month = m, year = y) }

    fun String.toDateOrNull() =
        split(DELIMITER)
            .mapNotNull { it.toIntOrNull() }
            .run { if (size != 3) null else this }
            ?.let { (d, m, y) -> Date(day = d, month = m, year = y) }

    fun Date.fromDate() = "$day$DELIMITER$month$DELIMITER$year"
}

object Calendar {
    fun daysOfYear(year: Int) = if (year.isLeapYear()) 366 else 365

    fun daysByMonths(year: Int) = if (year.isLeapYear()) vYear else oYear

    private fun Int.isLeapYear() = mod(4) == 0

    private val vYear = createMap(isLeapYear = true)

    private val oYear = createMap()

    private fun createMap(isLeapYear: Boolean = false) =
        mutableMapOf<Int, Int>().apply {
            arrayOf(1, 3, 5, 7, 8, 10, 12).forEach { put(it, 31) }
            arrayOf(4, 6, 9, 11).forEach { put(it, 30) }
            put(2, if (isLeapYear) 29 else 28)
        }
}
