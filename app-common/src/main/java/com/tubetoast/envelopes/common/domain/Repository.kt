package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.domain.models.*

interface Repository<M : ImmutableModel<M>, Key> {
    fun get(valueHash: Hash<M>): M?
    fun add(keyHash: Hash<Key>, value: M)
    fun delete(value: M)
    fun edit(oldValue: M, newValue: M)
    fun getCollection(keyHash: Hash<Key>): Set<M>
}

abstract class UpdatingRepository<M : ImmutableModel<M>, Key> : Repository<M, Key> {
    var update: (() -> Unit)? = null
    var deleteListener: ((Hash<M>) -> Unit)? = null
    val deleteListenerImpl: ((Hash<Key>) -> Unit) = {
        deleteCollection(it)
    }

    override fun add(keyHash: Hash<Key>, value: M) {
        if (addImpl(value, keyHash)) update?.invoke()
    }

    override fun delete(value: M) {
        if (deleteImpl(value)) {
            deleteListener?.invoke(value.hash)
            update?.invoke()
        }
    }

    override fun edit(oldValue: M, newValue: M) {
        if (editImpl(oldValue, newValue)) update?.invoke()
    }

    private fun deleteCollection(keyHash: Hash<Key>) {
        deleteCollectionImpl(keyHash).forEach {
            deleteListener?.invoke(it)
        }
    }

    protected abstract fun addImpl(value: M, keyHash: Hash<Key>): Boolean
    protected abstract fun deleteImpl(value: M): Boolean
    protected abstract fun editImpl(oldValue: M, newValue: M): Boolean

    /** Returns deleted */
    protected abstract fun deleteCollectionImpl(keyHash: Hash<Key>): Set<Hash<M>>
}

fun <M, Key> Repository<M, Key>.put(value: M) where M : ImmutableModel<M> {
    add(value.hash(), value)
}

fun <M, Key> Repository<M, Key>.getAll(): Collection<M> where M : ImmutableModel<M> =
    getCollection(Hash.any)

typealias SpendingRepository = UpdatingRepository<Spending, Category>
typealias CategoriesRepository = UpdatingRepository<Category, Envelope>
typealias EnvelopesRepository = UpdatingRepository<Envelope, String>
