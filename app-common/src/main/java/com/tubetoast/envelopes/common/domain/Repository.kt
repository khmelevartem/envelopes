package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.ImmutableModel
import com.tubetoast.envelopes.common.domain.models.Spending

interface Repository<M : ImmutableModel, Key> {
    fun get(valueId: String): M?
    fun getCollection(keyId: String): Set<M>
    fun getAll(): Set<M>
    fun add(keyId: String, value: M)
    fun delete(value: M)
    fun move(value: M, newKey: String)
    fun edit(oldValue: M, newValue: M)
}

abstract class UpdatingRepository<M : ImmutableModel, Key> : Repository<M, Key> {
    var update: (() -> Unit)? = null
    var deleteListener: ((String) -> Unit)? = null
    val deleteListenerImpl: ((String) -> Unit) = {
        deleteCollection(it)
    }

    final override fun add(keyId: String, value: M) {
        if (addImpl(value, keyId)) update?.invoke()
    }

    final override fun delete(value: M) {
        if (deleteImpl(value)) {
            deleteListener?.invoke(value.id)
            update?.invoke()
        }
    }

    final override fun move(value: M, newKey: String) {
        if (deleteImpl(value) && addImpl(value, newKey)) {
            update?.invoke()
        }
    }

    final override fun edit(oldValue: M, newValue: M) {
        if (editImpl(oldValue, newValue)) update?.invoke()
    }

    private fun deleteCollection(keyId: String) {
        deleteCollectionImpl(keyId).forEach {
            deleteListener?.invoke(it)
        }
    }

    abstract fun addImpl(value: M, keyId: String): Boolean
    abstract fun deleteImpl(value: M): Boolean
    abstract fun editImpl(oldValue: M, newValue: M): Boolean

    /** Returns deleted */
    abstract fun deleteCollectionImpl(keyId: String): Set<String>
}

fun <M, Key> Repository<M, Key>.put(value: M) where M : ImmutableModel {
    add(value.id, value)
}

typealias UpdatingSpendingRepository = UpdatingRepository<Spending, Category>
typealias UpdatingCategoriesRepository = UpdatingRepository<Category, Envelope>
typealias UpdatingEnvelopesRepository = UpdatingRepository<Envelope, String>
