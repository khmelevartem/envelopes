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
    fun getKey(valueId: Id<M>): Id<Key>?
    fun add(keyId: Id<Key>, value: M)
    fun add(vararg values: Pair<Id<Key>, M>)
    fun delete(value: M)
    fun deleteCollection(keyId: Id<Key>)
    fun deleteAll()
    fun move(value: M, newKey: Id<Key>)
    fun edit(oldValue: M, newValue: M)
}

fun <M : ImmutableModel<M>, Key : ImmutableModel<Key>> Repository<M, Key>.put(
    value: M,
    keyFallback: ((Id<M>) -> Id<Key>) = { Id.any }
) {
    add(getKey(value.id) ?: keyFallback.invoke(value.id), value)
}

typealias SpendingRepository = Repository<Spending, Category>
typealias CategoriesRepository = Repository<Category, Envelope>
typealias EnvelopesRepository = Repository<Envelope, Root>
typealias GoalsRepository = Repository<Goal, Root>
