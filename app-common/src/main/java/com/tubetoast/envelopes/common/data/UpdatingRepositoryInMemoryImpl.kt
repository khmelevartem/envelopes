package com.tubetoast.envelopes.common.data

import com.tubetoast.envelopes.common.domain.CategoriesRepository
import com.tubetoast.envelopes.common.domain.EnvelopesRepository
import com.tubetoast.envelopes.common.domain.SpendingRepository
import com.tubetoast.envelopes.common.domain.UpdatingRepository
import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.Id
import com.tubetoast.envelopes.common.domain.models.ImmutableModel
import com.tubetoast.envelopes.common.domain.models.Spending

open class UpdatingRepositoryInMemoryImpl<M : ImmutableModel<M>, Key> :
    UpdatingRepository<M, Key>() {

    protected val sets = mutableMapOf<Id<Key>, MutableSet<M>>()
    protected val keys = mutableMapOf<Id<M>, Id<Key>>()

    override fun get(valueId: Id<M>): M? {
        val key = keys[valueId]
        return sets[key]?.find { it.id == valueId }
    }

    override fun addImpl(value: M, keyId: Id<Key>): Boolean {
        if (keyId == Id.any) throw IllegalArgumentException("Can not add with uncertain key")
        keys[value.id] = keyId
        return getCollection(keyId).add(value)
    }

    override fun deleteImpl(value: M): Boolean {
        return keys[value.id]?.let {
            keys.remove(value.id)
            getCollection(it)
        }?.remove(value) ?: false
    }

    override fun editImpl(oldValue: M, newValue: M): Boolean =
        keys[oldValue.id]?.let { key ->
            keys.remove(oldValue.id)
            keys[newValue.id] = key
            getCollection(key).run {
                remove(oldValue) && add(newValue)
            }
        } ?: throw IllegalArgumentException("There was no old value $oldValue found")

    override fun getCollection(keyId: Id<Key>) = if (keyId == Id.any) {
        sets.flatMapTo(mutableSetOf()) { it.value }
        // No modification can be made!
    } else {
        sets.getOrPut(keyId) { mutableSetOf() }
    }

    override fun deleteCollectionImpl(keyId: Id<Key>): Set<Id<M>> {
        return sets.remove(keyId)?.mapTo(mutableSetOf()) { it.id }.orEmpty()
    }
}

/** [EnvelopesRepository] */
open class EnvelopesRepositoryImpl : UpdatingRepositoryInMemoryImpl<Envelope, String>()

/** [CategoriesRepository] */
open class CategoriesRepositoryImpl : UpdatingRepositoryInMemoryImpl<Category, Envelope>()

/** [SpendingRepository] */
open class SpendingRepositoryImpl : UpdatingRepositoryInMemoryImpl<Spending, Category>()
