package com.tubetoast.envelopes.common.data

import com.tubetoast.envelopes.common.domain.UpdatingRepository
import com.tubetoast.envelopes.common.domain.models.*

open class UpdatingRepositoryInMemoryImpl<M : ImmutableModel<M>, Key> :
    UpdatingRepository<M, Key>() {

    private val sets = mutableMapOf<Hash<Key>, MutableSet<M>>()
    private val keys = mutableMapOf<Hash<M>, Hash<Key>>()

    override fun get(keyHash: Hash<Key>) = sets.getOrPut(keyHash) { mutableSetOf() }

    override fun addImpl(value: M, keyHash: Hash<Key>): Boolean {
        keys[value.hash] = keyHash
        return get(keyHash).add(value)
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

class EnvelopesRepositoryImpl : UpdatingRepositoryInMemoryImpl<Envelope, Unit>()
class CategoriesRepositoryImpl : UpdatingRepositoryInMemoryImpl<Category, Envelope>()
class SpendingRepositoryImpl : UpdatingRepositoryInMemoryImpl<Spending, Category>()
