package com.tubetoast.envelopes.common.domain.models

import java.util.Calendar

data class Date(
    val day: Int,
    val month: Int,
    val year: Int
) : ImmutableModel<Date>(), Comparable<Date> {
    init {
        if (month !in 1..12) throw IllegalArgumentException("month $month")
        when (month) {
            1, 3, 5, 7, 8, 10, 12 -> if (day !in 1..31) throw IllegalArgumentException("month $month day $day")
            4, 6, 9, 11 -> if (day > 30) throw IllegalArgumentException("month $month day $day")
            2 -> if (year.mod(4) == 0 && day > 29 || year.mod(4) != 0 && day > 28) throw IllegalArgumentException(
                "month $month day $day"
            )
        }
    }

    override fun compareTo(other: Date): Int =
        when {
            this.year == other.year && this.month == other.month && this.day == other.day -> 0
            this.year >= other.year && this.month >= other.month && this.day > other.day -> 1
            else -> -1
        }

    companion object {
        fun today(): Date =
            date { get(Calendar.DAY_OF_MONTH) }

        fun currentMonth(day: Int = 1) =
            date { day }

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
