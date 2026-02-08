package com.tubetoast.envelopes.common.data

import com.tubetoast.envelopes.common.domain.CategoriesRepository
import com.tubetoast.envelopes.common.domain.EnvelopesRepository
import com.tubetoast.envelopes.common.domain.Repository
import com.tubetoast.envelopes.common.domain.SpendingRepository
import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.Id
import com.tubetoast.envelopes.common.domain.models.ImmutableModel
import com.tubetoast.envelopes.common.domain.models.Root
import com.tubetoast.envelopes.common.domain.models.Spending

open class InMemoryRepository<M : ImmutableModel<M>, Key : ImmutableModel<Key>> : Repository<M, Key> {
    private val sets = mutableMapOf<Id<Key>, MutableSet<M>>()
    private val keys = mutableMapOf<Id<M>, Id<Key>>()

    override suspend fun get(valueId: Id<M>): M? {
        val key = keys[valueId]
        return sets[key]?.find { it.id == valueId }
    }

    override suspend fun getCollection(keyId: Id<Key>) = sets.getOrPut(keyId) { mutableSetOf() }

    override suspend fun getAll(): Set<M> = sets.flatMapTo(mutableSetOf()) { it.value }

    override suspend fun getKey(valueId: Id<M>): Id<Key>? = keys[valueId]

    override suspend fun add(
        keyId: Id<Key>,
        value: M
    ) {
        keys[value.id] = keyId
        getCollection(keyId).add(value)
    }

    override suspend fun add(vararg values: Pair<Id<Key>, M>) {
        values.forEach {
            add(it.first, it.second)
        }
    }

    override suspend fun delete(value: M) {
        keys[value.id]?.let {
            keys.remove(value.id)
            getCollection(it).remove(value)
        } ?: false
    }

    override suspend fun edit(
        oldValue: M,
        newValue: M
    ) {
        keys[oldValue.id]?.let { key ->
            keys.remove(oldValue.id)
            keys[newValue.id] = key
            getCollection(key).run {
                remove(oldValue) && add(newValue)
            }
        }
    }

    override suspend fun move(
        value: M,
        newKey: Id<Key>
    ) {
        keys[value.id]?.let { oldKey ->
            getCollection(oldKey).remove(value) && getCollection(newKey).add(value)
        }
    }

    override suspend fun deleteCollection(keyId: Id<Key>) {
        sets.remove(keyId)?.mapTo(mutableSetOf()) { it.id }.orEmpty()
    }

    override suspend fun deleteAll() {
        sets.values.flatten().mapTo(mutableSetOf()) { it.id }
        sets.clear()
    }
}

/** [EnvelopesRepository] */
class EnvelopesInMemoryRepository : InMemoryRepository<Envelope, Root>()

/** [CategoriesRepository] */
class CategoriesInMemoryRepository : InMemoryRepository<Category, Envelope>()

/** [SpendingRepository] */
class SpendingInMemoryRepository : InMemoryRepository<Spending, Category>()
