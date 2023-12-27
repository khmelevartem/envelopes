package com.tubetoast.envelopes.common.data

import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.Id
import com.tubetoast.envelopes.common.domain.models.ImmutableModel
import com.tubetoast.envelopes.common.domain.models.Spending

sealed interface Source<M : ImmutableModel<M>, Key> {
    fun getAll(): Set<M>
    fun getByKey(key: Id<Key>): M
    fun put(key: Id<Key>, newValue: M)
    fun delete(key: Id<Key>, value: M)
    fun deleteAll(key: Id<Key>)
    fun clear()
}

interface EnvelopesSource : Source<Envelope, Any>
interface SpendingSource : Source<Spending, Category>
interface CategoriesSource : Source<Category, Envelope>