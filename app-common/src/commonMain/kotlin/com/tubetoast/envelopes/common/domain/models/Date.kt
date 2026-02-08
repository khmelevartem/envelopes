package com.tubetoast.envelopes.common.domain.models

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Instant

data class Date(
    val day: Int,
    val month: Int,
    val year: Int
) : ImmutableModel<Date>(),
    Comparable<Date> {
    init {
        require(month in 1..12) { "month $month" }
        require(day <= daysInThisMonth()) { "year $year month $month day $day" }
    }

    override fun compareTo(other: Date): Int = compareValuesBy(this, other, { it.year }, { it.month }, { it.day })

    override fun toString(): String = "$day.$month.$year"

    infix operator fun rangeTo(other: Date): DateRange = dateRangeTo(other)

    fun toLocalDate(): LocalDate = LocalDate(year, month, day)

    companion object {
        fun LocalDate.toDate() = Date(day, month.number, year)

        fun fromMillis(millis: Long): Date {
            val instant = Instant.fromEpochMilliseconds(millis)
            val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
            return Date(dateTime.day, dateTime.month.number, dateTime.year)
        }

        fun today(): Date {
            val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            return Date(now.day, now.month.number, now.year)
        }

        fun currentMonth() = today().monthAsRange()

        fun currentYear() = today().yearAsRange()
    }
}
