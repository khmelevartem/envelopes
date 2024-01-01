package com.tubetoast.envelopes.database.data

import com.tubetoast.envelopes.common.domain.CategoriesRepository
import com.tubetoast.envelopes.common.domain.EnvelopesRepository
import com.tubetoast.envelopes.common.domain.SpendingRepository
import com.tubetoast.envelopes.common.domain.UpdatingRepository
import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.Id
import com.tubetoast.envelopes.common.domain.models.ImmutableModel
import com.tubetoast.envelopes.common.domain.models.Spending

open class UpdatingRepositoryDatabaseImpl<M : ImmutableModel<M>, Key> :
    UpdatingRepository<M, Key>() {

    override fun get(valueId: Id<M>): M? {
        TODO()
    }

    override fun getCollection(keyId: Id<Key>): Set<M> {
        TODO()
    }

    override fun addImpl(value: M, keyId: Id<Key>): Boolean {
        TODO()
    }

    override fun deleteImpl(value: M): Boolean {
        TODO()
    }

    override fun editImpl(oldValue: M, newValue: M): Boolean {
        TODO()
    }

    override fun deleteCollectionImpl(keyId: Id<Key>): Set<Id<M>> {
        TODO()
    }
}

/** [EnvelopesRepository] */
open class EnvelopesRepositoryDatabaseBase : UpdatingRepositoryDatabaseImpl<Envelope, String>()

/** [CategoriesRepository] */
open class CategoriesRepositoryDatabaseBase : UpdatingRepositoryDatabaseImpl<Category, Envelope>()

/** [SpendingRepository] */
open class SpendingRepositoryDatabaseBase : UpdatingRepositoryDatabaseImpl<Spending, Category>()
