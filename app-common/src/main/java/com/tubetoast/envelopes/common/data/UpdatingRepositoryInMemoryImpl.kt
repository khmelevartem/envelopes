package com.tubetoast.envelopes.common.data

import com.tubetoast.envelopes.common.domain.*
import com.tubetoast.envelopes.common.domain.models.*

open class UpdatingRepositoryInMemoryImpl<M : ImmutableModel<M>, Key> :
    UpdatingRepository<M, Key>() {

    private val sets = mutableMapOf<Hash<Key>, MutableSet<M>>()
    private val keys = mutableMapOf<Hash<M>, Hash<Key>>()

    // if ANY, then no modification can be made
    override fun get(modelHash: Hash<Key>) = if (modelHash == Hash.any) {
        sets.flatMapTo(mutableSetOf()) { it.value } // toSet()
    } else {
        sets.getOrPut(modelHash) { mutableSetOf() }
    }

    override fun addImpl(value: M, keyHash: Hash<Key>): Boolean {
        keys[value.hash] = keyHash
        return get(keyHash).add(value)
    }

    override fun get(modelHash: Hash<M>): M? {
        return getAll().find { it.hash == modelHash }
    }

    override fun deleteImpl(value: M): Boolean =
        keys[value.hash]?.let {
            keys.remove(value.hash)
            get(it)
        }?.remove(value) ?: false

    override fun editImpl(oldValue: M, newValue: M): Boolean =
        keys[oldValue.hash]
            ?.let { key ->
                keys.remove(oldValue.hash)
                keys[newValue.hash] = key
                get(key)
            }?.run {
                remove(oldValue) && add(newValue)
            } ?: throw IllegalArgumentException("There was no old value $oldValue found")
}

/** [EnvelopesRepository] */
typealias EnvelopesRepositoryImpl = UpdatingRepositoryInMemoryImpl<Envelope, String>

/** [CategoryRepository] */
typealias CategoriesRepositoryImpl = UpdatingRepositoryInMemoryImpl<Category, Envelope>

/** [SpendingRepository] */
typealias SpendingRepositoryImpl = UpdatingRepositoryInMemoryImpl<Spending, Category>
