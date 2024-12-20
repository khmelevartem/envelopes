package com.tubetoast.envelopes.common.domain.models

import java.util.Calendar

data class Date(
    val day: Int,
    val month: Int,
    val year: Int
) : ImmutableModel<Date>(), Comparable<Date> {
    init {
        if (month !in 1..12) {
            throw IllegalArgumentException("month $month")
        }
        if (daysInThisMonth() < day) {
            throw IllegalArgumentException("year $year month $month day $day")
        }
    }

    override fun compareTo(other: Date): Int =
        when {
            this.year == other.year && this.month == other.month && this.day == other.day -> 0
            this.year > other.year -> 1
            this.year == other.year && this.month > other.month -> 1
            this.year == other.year && this.month == other.month && this.day > other.day -> 1
            else -> -1
        }

    companion object {
        fun today(): Date =
            Calendar.getInstance().run {
                Date(
                    day = get(Calendar.DAY_OF_MONTH),
                    month = get(Calendar.MONTH) + 1,
                    year = get(Calendar.YEAR)
                )
            }

        fun currentMonth() = today().monthAsRange()

        fun currentYear() = today().yearAsRange()
    }
}

fun Date.monthAsRange() =
    copy(day = 1)..copy(day = daysInThisMonth())

fun Date.yearAsRange() =
    copy(day = 1, month = 1)..copy(day = 31, month = 12)
