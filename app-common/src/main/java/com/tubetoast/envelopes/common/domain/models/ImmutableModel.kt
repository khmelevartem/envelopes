package com.tubetoast.envelopes.common.domain.models

abstract class ImmutableModel<T: ImmutableModel<T>> {
    val hash: Hash<T> by lazy { Hash(hashCode()) }
}

@JvmInline
value class Hash<T>(val hasCode: Int) {
    companion object {
        fun <K> any() = Hash<K>(0)
    }
}