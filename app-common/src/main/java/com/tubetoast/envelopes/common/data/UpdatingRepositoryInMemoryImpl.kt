package com.tubetoast.envelopes.common.data

import com.tubetoast.envelopes.common.domain.UpdatingCategoriesRepository
import com.tubetoast.envelopes.common.domain.UpdatingEnvelopesRepository
import com.tubetoast.envelopes.common.domain.UpdatingRepository
import com.tubetoast.envelopes.common.domain.UpdatingSpendingRepository
import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Envelope

import com.tubetoast.envelopes.common.domain.models.ImmutableModel
import com.tubetoast.envelopes.common.domain.models.Spending

open class UpdatingRepositoryInMemoryImpl<M : ImmutableModel, Key> :
    UpdatingRepository<M, Key>() {

    protected val sets = mutableMapOf<String, MutableSet<M>>()
    protected val keys = mutableMapOf<String, String>()

    override fun get(valueId: String): M? {
        val key = keys[valueId]
        return sets[key]?.find { it.id == valueId }
    }

    override fun getCollection(keyId: String) = if (keyId.isEmpty()) {
        throw UnsupportedOperationException("need to delete this call")
    } else {
        sets.getOrPut(keyId) { mutableSetOf() }
    }

    override fun getAll(): Set<M> =
        sets.flatMapTo(mutableSetOf()) { it.value }

    override fun addImpl(value: M, keyId: String): Boolean {
        if (keyId.isEmpty()) throw IllegalArgumentException("Can not add with uncertain key")
        keys[value.id] = keyId
        return getCollection(keyId).add(value)
    }

    override fun deleteImpl(value: M): Boolean {
        return keys[value.id]?.let {
            keys.remove(value.id)
            getCollection(it).remove(value)
        } ?: false
    }

    override fun editImpl(oldValue: M, newValue: M): Boolean =
        keys[oldValue.id]?.let { key ->
            keys.remove(oldValue.id)
            keys[newValue.id] = key
            getCollection(key).run {
                remove(oldValue) && add(newValue)
            }
        } ?: false

    override fun deleteCollectionImpl(keyId: String): Set<String> {
        return sets.remove(keyId)?.mapTo(mutableSetOf()) { it.id }.orEmpty()
    }
}

/** [UpdatingEnvelopesRepository] */
open class EnvelopesRepositoryInMemoryBase : UpdatingRepositoryInMemoryImpl<Envelope, String>()

/** [UpdatingCategoriesRepository] */
open class CategoriesRepositoryInMemoryBase : UpdatingRepositoryInMemoryImpl<Category, Envelope>()

/** [UpdatingSpendingRepository] */
open class SpendingRepositoryInMemoryBase : UpdatingRepositoryInMemoryImpl<Spending, Category>()
