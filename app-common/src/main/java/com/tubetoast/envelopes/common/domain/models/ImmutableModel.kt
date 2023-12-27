package com.tubetoast.envelopes.common.domain.models

abstract class ImmutableModel<T : ImmutableModel<T>> {
    val id: Id<T> by lazy { Id(hashCode()) }
}

@JvmInline
value class Id<out T> constructor(val idCode: Int) {
    companion object {
        val any = Id<Nothing>(-1)
        fun <K> any(): Id<K> = any
    }
}

fun <T> Any.id() = Id<T>(hashCode())
fun <T> Int.id() = Id<T>(this)
