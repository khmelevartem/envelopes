package com.tubetoast.envelopes.common.domain.models

infix operator fun Date.rangeTo(other: Date): DateRange =
    DateRange(other, this)

fun Date.daysInThisMonth() =
    Calendar.daysByMonths(year)[month] ?: throw IllegalArgumentException("month $month")

fun Date.inDaysOfYear(): Int {
    return Calendar.daysByMonths(year).entries.fold(0) { acc, (monthNumber, daysInIt) ->
        acc + when {
            (monthNumber < month) -> daysInIt
            monthNumber == month -> day
            else -> 0
        }
    }
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

object DateConverter {
    private const val DELIMITER = "/"
    fun String.toDate() = split(DELIMITER).map { it.toInt() }
        .let { (d, m, y) -> Date(day = d, month = m, year = y) }

    fun Date.fromDate() = "$day$DELIMITER$month$DELIMITER$year"
}

object Calendar {

    fun daysOfYear(year: Int) =
        if (year.mod(4) == 0) 366 else 365

    fun daysByMonths(year: Int) =
        if (year.mod(4) == 0) vYear else oYear

    private val vYear = createMap(isVisokosniy = true)

    private val oYear = createMap()

    private fun createMap(isVisokosniy: Boolean = false) = mutableMapOf<Int, Int>().apply {
        arrayOf(1, 3, 5, 7, 8, 10, 12).forEach { put(it, 31) }
        arrayOf(4, 6, 9, 11).forEach { put(it, 30) }
        put(2, if (isVisokosniy) 29 else 28)
    }
}
