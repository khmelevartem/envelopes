package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.domain.models.*

interface Repository<M : ImmutableModel<M>, Key> {
    fun getCollection(keyHash: Hash<Key>): Set<M>

    fun get(modelHash: Hash<M>): M?
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

fun <M, Key> Repository<M, Key>.put(value: M) where M : ImmutableModel<M> {
    add(value.hash(), value)
}

fun <M, Key> Repository<M, Key>.getAll(): Collection<M> where M : ImmutableModel<M> =
    getCollection(Hash.any)

typealias SpendingRepository = UpdatingRepository<Spending, Category>
typealias CategoriesRepository = UpdatingRepository<Category, Envelope>
typealias EnvelopesRepository = UpdatingRepository<Envelope, String>
