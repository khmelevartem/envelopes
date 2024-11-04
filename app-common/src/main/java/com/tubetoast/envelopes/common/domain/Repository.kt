package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.Id
import com.tubetoast.envelopes.common.domain.models.ImmutableModel
import com.tubetoast.envelopes.common.domain.models.Root
import com.tubetoast.envelopes.common.domain.models.Spending

interface Repository<M : ImmutableModel<M>, Key : ImmutableModel<Key>> {
    fun get(valueId: Id<M>): M?
    fun getCollection(keyId: Id<Key>): Set<M>
    fun getAll(): Set<M>
    fun getAllByKeys(): Map<Id<Key>, Set<M>>
    fun getKey(valueId: Id<M>): Id<Key>?
    fun add(keyId: Id<Key>, value: M)
    fun add(vararg values: Pair<Id<Key>, M>)
    fun delete(value: M)
    fun deleteCollection(keyId: Id<Key>)
    fun move(value: M, newKey: Id<Key>)
    fun edit(oldValue: M, newValue: M)
}

abstract class UpdatingRepository<M : ImmutableModel<M>, Key : ImmutableModel<Key>> :
    Repository<M, Key> {
    var update: (() -> Unit)? = null
    var deleteListener: ((Id<M>) -> Unit)? = null
    val deleteListenerImpl: ((Id<Key>) -> Unit) = {
        deleteCollection(it)
    }

    final override fun add(keyId: Id<Key>, value: M) {
        if (addImpl(value, keyId)) update?.invoke()
    }

    override fun add(vararg values: Pair<Id<Key>, M>) {
        if (values.map { (key, value) -> addImpl(value, key) }.any()) {
            update?.invoke()
        }
    }

    final override fun delete(value: M) {
        if (deleteImpl(value)) {
            deleteListener?.invoke(value.id)
            update?.invoke()
        }
    }

    final override fun move(value: M, newKey: Id<Key>) {
        if (moveImpl(value, newKey)) {
            update?.invoke()
        }
    }

    final override fun edit(oldValue: M, newValue: M) {
        if (editImpl(oldValue, newValue)) update?.invoke()
    }

    final override fun deleteCollection(keyId: Id<Key>) {
        deleteCollectionImpl(keyId).forEach {
            deleteListener?.invoke(it)
        }
    }

    abstract fun addImpl(value: M, keyId: Id<Key>): Boolean
    abstract fun deleteImpl(value: M): Boolean

    /** Returns deleted */
    abstract fun deleteCollectionImpl(keyId: Id<Key>): Set<Id<M>>
    abstract fun editImpl(oldValue: M, newValue: M): Boolean
    abstract fun moveImpl(value: M, newKyId: Id<Key>): Boolean
}

fun <M : ImmutableModel<M>, Key : ImmutableModel<Key>> Repository<M, Key>.put(
    value: M,
    keyFallback: ((Id<M>) -> Id<Key>) = { Id.any }
) {
    add(getKey(value.id) ?: keyFallback.invoke(value.id), value)
}

typealias UpdatingSpendingRepository = UpdatingRepository<Spending, Category>
typealias UpdatingCategoriesRepository = UpdatingRepository<Category, Envelope>
typealias UpdatingEnvelopesRepository = UpdatingRepository<Envelope, Root>
