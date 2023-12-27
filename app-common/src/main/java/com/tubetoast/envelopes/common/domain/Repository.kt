package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.Id
import com.tubetoast.envelopes.common.domain.models.ImmutableModel
import com.tubetoast.envelopes.common.domain.models.Spending
import com.tubetoast.envelopes.common.domain.models.id

interface Repository<M : ImmutableModel<M>, Key> {
    fun get(valueId: Id<M>): M?
    fun add(keyId: Id<Key>, value: M)
    fun delete(value: M)
    fun edit(oldValue: M, newValue: M)
    fun getCollection(keyId: Id<Key>): Set<M>
}

abstract class UpdatingRepository<M : ImmutableModel<M>, Key> : Repository<M, Key> {
    var update: (() -> Unit)? = null
    var deleteListener: ((Id<M>) -> Unit)? = null
    val deleteListenerImpl: ((Id<Key>) -> Unit) = {
        deleteCollection(it)
    }

    override fun add(keyId: Id<Key>, value: M) {
        if (addImpl(value, keyId)) update?.invoke()
    }

    override fun delete(value: M) {
        if (deleteImpl(value)) {
            deleteListener?.invoke(value.id)
            update?.invoke()
        }
    }

    override fun edit(oldValue: M, newValue: M) {
        if (editImpl(oldValue, newValue)) update?.invoke()
    }

    private fun deleteCollection(keyId: Id<Key>) {
        deleteCollectionImpl(keyId).forEach {
            deleteListener?.invoke(it)
        }
    }

    protected abstract fun addImpl(value: M, keyId: Id<Key>): Boolean
    protected abstract fun deleteImpl(value: M): Boolean
    protected abstract fun editImpl(oldValue: M, newValue: M): Boolean

    /** Returns deleted */
    protected abstract fun deleteCollectionImpl(keyId: Id<Key>): Set<Id<M>>
}

fun <M, Key> Repository<M, Key>.put(value: M) where M : ImmutableModel<M> {
    add(value.id(), value)
}

fun <M, Key> Repository<M, Key>.getAll(): Collection<M> where M : ImmutableModel<M> =
    getCollection(Id.any)

typealias SpendingRepository = UpdatingRepository<Spending, Category>
typealias CategoriesRepository = UpdatingRepository<Category, Envelope>
typealias EnvelopesRepository = UpdatingRepository<Envelope, String>
