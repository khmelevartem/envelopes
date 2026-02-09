package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.Goal
import com.tubetoast.envelopes.common.domain.models.Id
import com.tubetoast.envelopes.common.domain.models.ImmutableModel
import com.tubetoast.envelopes.common.domain.models.Root
import com.tubetoast.envelopes.common.domain.models.Spending

interface Repository<M : ImmutableModel<M>, Key : ImmutableModel<Key>> {
    suspend fun get(valueId: Id<M>): M?

    suspend fun getCollection(keyId: Id<Key>): Set<M>

    suspend fun getAll(): Set<M>

    suspend fun getKey(valueId: Id<M>): Id<Key>?

    suspend fun add(
        keyId: Id<Key>,
        value: M
    )

    suspend fun add(vararg values: Pair<Id<Key>, M>)

    suspend fun delete(value: M)

    suspend fun deleteCollection(keyId: Id<Key>)

    suspend fun deleteAll()

    suspend fun move(
        value: M,
        newKey: Id<Key>
    )

    suspend fun edit(
        oldValue: M,
        newValue: M
    )
}

suspend fun <M : ImmutableModel<M>, Key : ImmutableModel<Key>> Repository<M, Key>.put(
    value: M,
    keyFallback: suspend ((Id<M>) -> Id<Key>) = { Id.any }
) {
    add(getKey(value.id) ?: keyFallback.invoke(value.id), value)
}

typealias SpendingRepository = Repository<Spending, Category>
typealias CategoriesRepository = Repository<Category, Envelope>
typealias EnvelopesRepository = Repository<Envelope, Root>
typealias GoalsRepository = Repository<Goal, Root>
