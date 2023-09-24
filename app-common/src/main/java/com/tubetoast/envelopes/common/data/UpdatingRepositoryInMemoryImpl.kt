package com.tubetoast.envelopes.common.data

import com.tubetoast.envelopes.common.domain.CategoriesRepository
import com.tubetoast.envelopes.common.domain.EnvelopesRepository
import com.tubetoast.envelopes.common.domain.SpendingRepository
import com.tubetoast.envelopes.common.domain.UpdatingRepository
import com.tubetoast.envelopes.common.domain.models.*

open class UpdatingRepositoryInMemoryImpl<M : ImmutableModel<M>, Key> :
    UpdatingRepository<M, Key>() {

    private val sets = mutableMapOf<Hash<Key>, MutableSet<M>>()
    private val keys = mutableMapOf<Hash<M>, Hash<Key>>()

    override fun get(valueHash: Hash<M>): M? {
        val key = keys[valueHash]
        return sets[key]?.find { it.hash == valueHash }
    }

    override fun addImpl(value: M, keyHash: Hash<Key>): Boolean {
        if (keyHash == Hash.any) throw IllegalArgumentException("Can not add with uncertain key")
        keys[value.hash] = keyHash
        return getCollection(keyHash).add(value)
    }

    override fun deleteImpl(value: M): Boolean {
        return keys[value.hash]?.let {
            keys.remove(value.hash)
            getCollection(it)
        }?.remove(value) ?: false
    }

    override fun editImpl(oldValue: M, newValue: M): Boolean =
        keys[oldValue.hash]
            ?.let { key ->
                keys.remove(oldValue.hash)
                keys[newValue.hash] = key
                getCollection(key)
            }?.run {
                remove(oldValue) && add(newValue)
            } ?: throw IllegalArgumentException("There was no old value $oldValue found")

    override fun getCollection(keyHash: Hash<Key>) = if (keyHash == Hash.any) {
        sets.flatMapTo(mutableSetOf()) { it.value }
        // No modification can be made!
    } else {
        sets.getOrPut(keyHash) { mutableSetOf() }
    }

    override fun deleteCollectionImpl(keyHash: Hash<Key>): Set<Hash<M>> {
        return sets.remove(keyHash)?.mapTo(mutableSetOf()) { it.hash }.orEmpty()
    }
}

/** [EnvelopesRepository] */
class EnvelopesRepositoryImpl : UpdatingRepositoryInMemoryImpl<Envelope, String>()

/** [CategoriesRepository] */
class CategoriesRepositoryImpl : UpdatingRepositoryInMemoryImpl<Category, Envelope>()

/** [SpendingRepository] */
class SpendingRepositoryImpl : UpdatingRepositoryInMemoryImpl<Spending, Category>()
