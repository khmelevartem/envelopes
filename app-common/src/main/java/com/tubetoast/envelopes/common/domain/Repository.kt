package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.domain.models.*

interface Repository<M : ImmutableModel<M>, Key> {
    fun get(keyHash: Hash<Key> = Hash.any()): Set<M>
    fun add(value: M, keyHash: Hash<Key>)
    fun delete(valueHash: Hash<M>)
    fun edit(valueHash: Hash<M>, newM: M)
}

abstract class UpdatingRepository<M : ImmutableModel<M>, Key> : Repository<M, Key> {
    var listener: (() -> Unit)? = null

    override fun add(value: M, keyHash: Hash<Key>) {
        if (addImpl(value, keyHash)) listener?.invoke()
    }

    override fun delete(valueHash: Hash<M>) {
        if (deleteImpl(valueHash)) listener?.invoke()
    }

    override fun edit(valueHash: Hash<M>, newM: M) {
        if (editImpl(valueHash, newM)) listener?.invoke()
    }

    protected abstract fun addImpl(value: M, keyHash: Hash<Key>): Boolean
    protected abstract fun deleteImpl(valueHash: Hash<M>): Boolean
    protected abstract fun editImpl(valueHash: Hash<M>, newM: M): Boolean
}

abstract class SpendingRepository : UpdatingRepository<Spending, Category>()
abstract class CategoryRepository : UpdatingRepository<Category, Envelope>()
abstract class EnvelopesRepository : UpdatingRepository<Envelope, Any>()

