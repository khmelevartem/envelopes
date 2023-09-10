package com.tubetoast.envelopes.common.domain.models

abstract class ImmutableModel<T : ImmutableModel<T>> {
    val hash: Hash<T> by lazy { Hash(hashCode()) }
}

@JvmInline
value class Hash<out T> constructor(val hashCode: Int) {
    companion object {
        val any = Hash<Nothing>(-1)
        fun <K> any(): Hash<K> = any
    }
}

fun <T> Any.hash() = Hash<T>(hashCode())
