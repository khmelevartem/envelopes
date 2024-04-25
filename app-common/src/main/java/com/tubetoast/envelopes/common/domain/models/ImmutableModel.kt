package com.tubetoast.envelopes.common.domain.models

abstract class ImmutableModel<T : ImmutableModel<T>> {
    open val id: Id<T> by lazy { Id(hashCode()) }
}

@JvmInline
value class Id<out T>(val code: Int) {
    companion object {
        val any = Id<Nothing>(-1)
        fun <K> any(): Id<K> = any
    }
}

fun <T> Any.id() = Id<T>(hashCode())
fun <T> Int.id() = Id<T>(this)

object Root: ImmutableModel<Root>() {
    override val id: Id<Root>
        get() = Id.any
}