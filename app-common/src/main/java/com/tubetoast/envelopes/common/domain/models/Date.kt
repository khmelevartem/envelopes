package com.tubetoast.envelopes.common.domain.models

import kotlin.random.Random

data class Date(
    val minute: Int,
    val hour: Int,
    val day: Int,
    val month: Int,
    val year: Int
) : ImmutableModel<Date>()

fun randomDate() = Random(System.currentTimeMillis()).run {
    Date(minute = nextInt(), hour = nextInt(), day = nextInt(), month = nextInt(), year = nextInt())
}
