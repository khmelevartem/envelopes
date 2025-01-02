package com.tubetoast.envelopes.common.data

import com.tubetoast.envelopes.common.domain.UpdatingCategoriesRepository
import com.tubetoast.envelopes.common.domain.UpdatingEnvelopesRepository
import com.tubetoast.envelopes.common.domain.UpdatingRepository
import com.tubetoast.envelopes.common.domain.UpdatingSpendingRepository
import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.Id
import com.tubetoast.envelopes.common.domain.models.ImmutableModel
import com.tubetoast.envelopes.common.domain.models.Root
import com.tubetoast.envelopes.common.domain.models.Spending

open class UpdatingRepositoryInMemoryImpl<M : ImmutableModel<M>, Key : ImmutableModel<Key>> :
    UpdatingRepository<M, Key>() {

    protected val sets = mutableMapOf<Id<Key>, MutableSet<M>>()
    protected val keys = mutableMapOf<Id<M>, Id<Key>>()

    override fun get(valueId: Id<M>): M? {
        val key = keys[valueId]
        return sets[key]?.find { it.id == valueId }
    }

    override fun getCollection(keyId: Id<Key>) =
        sets.getOrPut(keyId) { mutableSetOf() }

    override fun getAll(): Set<M> =
        sets.flatMapTo(mutableSetOf()) { it.value }

    override fun getAllByKeys(): Map<Id<Key>, Set<M>> {
        return sets
    }

    override fun getKey(valueId: Id<M>): Id<Key>? {
        return keys[valueId]
    }

    override fun addImpl(value: M, keyId: Id<Key>): Boolean {
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

    override fun moveImpl(value: M, newKyId: Id<Key>): Boolean {
        keys[value.id]?.let { oldKey ->
            return getCollection(oldKey).remove(value) && getCollection(newKyId).add(value)
        }
        return false
    }

    override fun deleteCollectionImpl(keyId: Id<Key>): Set<Id<M>> {
        return sets.remove(keyId)?.mapTo(mutableSetOf()) { it.id }.orEmpty()
    }

    override fun deleteAllImpl(): Set<Id<M>> {
        val deleted = sets.values.flatten().mapTo(mutableSetOf()) { it.id }
        sets.clear()
        return deleted
    }
}

/** [UpdatingEnvelopesRepository] */
open class EnvelopesRepositoryInMemoryBase : UpdatingRepositoryInMemoryImpl<Envelope, Root>()

/** [UpdatingCategoriesRepository] */
open class CategoriesRepositoryInMemoryBase : UpdatingRepositoryInMemoryImpl<Category, Envelope>()

/** [UpdatingSpendingRepository] */
open class SpendingRepositoryInMemoryBase : UpdatingRepositoryInMemoryImpl<Spending, Category>()
