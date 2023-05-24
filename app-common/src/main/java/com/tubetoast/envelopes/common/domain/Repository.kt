package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.domain.models.*

interface Repository<M : ImmutableModel<M>, Key> {
    fun get(keyHash: Hash<Key>): Set<M>
    fun add(keyHash: Hash<Key>, value: M)
    fun delete(value: M)
    fun edit(oldValue: M, newValue: M)
}

abstract class UpdatingRepository<M : ImmutableModel<M>, Key> : Repository<M, Key> {
    var listener: (() -> Unit)? = null

    override fun add(keyHash: Hash<Key>, value: M) {
        if (addImpl(value, keyHash)) listener?.invoke()
    }

    override fun delete(value: M) {
        if (deleteImpl(value)) listener?.invoke()
    }

    override fun edit(oldValue: M, newValue: M) {
        if (editImpl(oldValue, newValue)) listener?.invoke()
    }

    protected abstract fun addImpl(value: M, keyHash: Hash<Key>): Boolean
    protected abstract fun deleteImpl(value: M): Boolean
    protected abstract fun editImpl(oldValue: M, newValue: M): Boolean
}

fun <M> Repository<M, *>.put(value: M) where M : ImmutableModel<M> {
    add(Hash.any, value)
}

typealias SpendingRepository = UpdatingRepository<Spending, Category>
typealias CategoryRepository = UpdatingRepository<Category, Envelope>
typealias EnvelopesRepository = UpdatingRepository<Envelope, Unit>
