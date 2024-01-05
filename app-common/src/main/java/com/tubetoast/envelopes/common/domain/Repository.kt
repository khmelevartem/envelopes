package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.Id
import com.tubetoast.envelopes.common.domain.models.ImmutableModel
import com.tubetoast.envelopes.common.domain.models.Spending
import com.tubetoast.envelopes.common.domain.models.id

interface Repository<M : ImmutableModel<M>, Key> {
    fun get(valueId: Id<M>): M?
    fun getCollection(keyId: Id<Key>): Set<M>
    fun getAll(): Set<M>
    fun add(keyId: Id<Key>, value: M)
    fun delete(value: M)
    fun move(value: M, newKey: Id<Key>)
    fun edit(oldValue: M, newValue: M)
}

abstract class UpdatingRepository<M : ImmutableModel<M>, Key> : Repository<M, Key> {
    var update: (() -> Unit)? = null
    var deleteListener: ((Id<M>) -> Unit)? = null
    val deleteListenerImpl: ((Id<Key>) -> Unit) = {
        deleteCollection(it)
    }

    final override fun add(keyId: Id<Key>, value: M) {
        if (addImpl(value, keyId)) update?.invoke()
    }

    final override fun delete(value: M) {
        if (deleteImpl(value)) {
            deleteListener?.invoke(value.id)
            update?.invoke()
        }
    }

    final override fun move(value: M, newKey: Id<Key>) {
        if (deleteImpl(value) && addImpl(value, newKey)) {
            update?.invoke()
        }
    }

    final override fun edit(oldValue: M, newValue: M) {
        if (editImpl(oldValue, newValue)) update?.invoke()
    }

    private fun deleteCollection(keyId: Id<Key>) {
        deleteCollectionImpl(keyId).forEach {
            deleteListener?.invoke(it)
        }
    }

    abstract fun addImpl(value: M, keyId: Id<Key>): Boolean
    abstract fun deleteImpl(value: M): Boolean
    abstract fun editImpl(oldValue: M, newValue: M): Boolean

    /** Returns deleted */
    abstract fun deleteCollectionImpl(keyId: Id<Key>): Set<Id<M>>
}

fun <M, Key> Repository<M, Key>.put(value: M) where M : ImmutableModel<M> {
    add(value.id(), value)
}

typealias UpdatingSpendingRepository = UpdatingRepository<Spending, Category>
typealias UpdatingCategoriesRepository = UpdatingRepository<Category, Envelope>
typealias UpdatingEnvelopesRepository = UpdatingRepository<Envelope, String>