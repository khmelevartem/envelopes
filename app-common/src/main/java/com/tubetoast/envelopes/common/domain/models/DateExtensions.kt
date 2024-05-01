package com.tubetoast.envelopes.common.domain.models

infix operator fun Date.rangeTo(other: Date): DateRange =
    DateRange(other, this)

fun Date.daysInThisMonth() =
    Calendar.daysByMonths(year)[month] ?: throw IllegalArgumentException("month $month")

object DateConverter {
    private const val DELIMITER = "/"
    fun String.toDate() = split(DELIMITER).map { it.toInt() }
        .let { (d, m, y) -> Date(day = d, month = m, year = y) }

    fun Date.fromDate() = "$day$DELIMITER$month$DELIMITER$year"
}

object Calendar {
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
