package com.tubetoast.envelopes.common.domain.models

interface ImmutableModel {
    val id: String
}

val Any.className get() = this::class.simpleName
