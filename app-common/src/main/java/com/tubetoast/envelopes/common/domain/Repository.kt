package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.Goal
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
    fun deleteAll()
    fun move(value: M, newKey: Id<Key>)
    fun edit(oldValue: M, newValue: M)
}

abstract class UpdatingRepository<M : ImmutableModel<M>, Key : ImmutableModel<Key>> :
    Repository<M, Key> {
    protected val updateListeners: MutableList<(() -> Unit)> = mutableListOf()
    protected var deleteListeners: MutableList<((Id<M>) -> Unit)> = mutableListOf()

    fun onKeyDelete(key: Id<Key>) {
        deleteCollection(key)
    }

    fun addUpdateListener(listener: () -> Unit) {
        updateListeners.add(listener)
    }

    fun addDeleteListener(listener: (Id<M>) -> Unit) {
        deleteListeners.add(listener)
    }

    final override fun add(keyId: Id<Key>, value: M) {
        if (addImpl(value, keyId)) notifyUpdate()
    }

    override fun add(vararg values: Pair<Id<Key>, M>) {
        if (values.map { (key, value) -> addImpl(value, key) }.any()) {
            notifyUpdate()
        }
    }

    final override fun delete(value: M) {
        if (deleteImpl(value)) {
            notifyDelete(value.id)
            notifyUpdate()
        }
    }

    final override fun move(value: M, newKey: Id<Key>) {
        if (moveImpl(value, newKey)) {
            notifyUpdate()
        }
    }

    final override fun edit(oldValue: M, newValue: M) {
        if (editImpl(oldValue, newValue)) notifyUpdate()
    }

    final override fun deleteCollection(keyId: Id<Key>) {
        deleteCollectionImpl(keyId).onEach { deleted ->
            notifyDelete(deleted)
        }
        notifyUpdate()
    }

    final override fun deleteAll() {
        deleteAllImpl().onEach { deleted ->
            notifyDelete(deleted)
        }
        notifyUpdate()
    }

    abstract fun addImpl(value: M, keyId: Id<Key>): Boolean
    abstract fun deleteImpl(value: M): Boolean

    /** Returns deleted */
    abstract fun deleteCollectionImpl(keyId: Id<Key>): Set<Id<M>>
    abstract fun deleteAllImpl(): Set<Id<M>>
    abstract fun editImpl(oldValue: M, newValue: M): Boolean
    abstract fun moveImpl(value: M, newKyId: Id<Key>): Boolean

    private fun notifyUpdate() {
        updateListeners.forEach { it.invoke() }
    }

    private fun notifyDelete(value: Id<M>) {
        deleteListeners.forEach { it.invoke(value) }
    }
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
typealias UpdatingGoalsRepository = UpdatingRepository<Goal, Root>
