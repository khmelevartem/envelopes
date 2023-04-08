package com.tubetoast.envelopes.common.data

import com.tubetoast.envelopes.common.domain.models.*

sealed interface Source<M : ImmutableModel<M>, Key> {
    fun getAll(): Set<M>
    fun getByKey(key: Hash<Key>): M
    fun put(key: Hash<Key>, newValue: M)
    fun delete(key: Hash<Key>, value: M)
    fun deleteAll(key: Hash<Key>)
    fun clear()
}

interface EnvelopesSource : Source<Envelope, Any>
interface SpendingSource : Source<Spending, Category>
interface CategoriesSource : Source<Category, Envelope>