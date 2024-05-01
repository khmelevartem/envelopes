package com.tubetoast.envelopes.common.domain.models

import java.util.Calendar

data class Date(
    val day: Int,
    val month: Int,
    val year: Int
) : ImmutableModel<Date>(), Comparable<Date> {
    init {
        if (month !in 1..12)
            throw IllegalArgumentException("month $month")
        if (daysByMonths(year)[month]!! < day)
            throw IllegalArgumentException("year $year month $month day $day")
    }

    override fun compareTo(other: Date): Int =
        when {
            this.year == other.year && this.month == other.month && this.day == other.day -> 0
            this.year >= other.year && this.month >= other.month && this.day > other.day -> 1
            else -> -1
        }

    companion object {

        private val vYear = createMap(isVisokosniy = true)

        private val oYear = createMap()

        private fun createMap(isVisokosniy: Boolean = false) = mutableMapOf<Int, Int>().apply {
            arrayOf(1, 3, 5, 7, 8, 10, 12).forEach { put(it, 31) }
            arrayOf(4, 6, 9, 11).forEach { put(it, 30) }
            put(2, if (isVisokosniy) 29 else 28)
        }

        private fun daysByMonths(year: Int) =
            if (year.mod(4) == 0) vYear else oYear

        fun today(): Date =
            date { get(Calendar.DAY_OF_MONTH) }

        fun currentMonth() =
            date { 1 }..date {
                daysByMonths(get(Calendar.YEAR))[get(Calendar.MONTH) + 1]!!
            }

        private fun date(day: Calendar.() -> Int) =
            Calendar.getInstance().run {
                Date(
                    day = day(),
                    month = get(Calendar.MONTH) + 1,
                    year = get(Calendar.YEAR),
                )
            }
    }
}

data class DateRange(
    override val endInclusive: Date,
    override val start: Date
) : ClosedRange<Date>

infix operator fun Date.rangeTo(other: Date): DateRange =
    DateRange(other, this)

object DateConverter {
    private const val DELIMITER = "/"
    fun String.toDate() = split(DELIMITER).map { it.toInt() }
        .let { (d, m, y) -> Date(day = d, month = m, year = y) }

    fun Date.fromDate() = "$day$DELIMITER$month$DELIMITER$year"
}
