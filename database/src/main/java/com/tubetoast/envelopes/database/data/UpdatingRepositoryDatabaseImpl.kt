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

abstract class UpdatingRepositoryDatabaseImpl<M : ImmutableModel<M>, Key>(
    private val dataSource: DataSource<M, Key>
) : UpdatingRepository<M, Key>() {

    override fun get(valueId: Id<M>): M? {
        return dataSource.get(valueId)
    }

    override fun getCollection(keyId: Id<Key>): Set<M> {
        return dataSource.getCollection(keyId).toSet()
    }

    override fun addImpl(value: M, keyId: Id<Key>): Boolean {
        dataSource.write(value, keyId)
        return true // fix it with custom insert
    }

    override fun deleteImpl(value: M): Boolean {
        return dataSource.delete(value.id)
    }

    override fun editImpl(oldValue: M, newValue: M): Boolean {
        TODO("need to be implemented")
//        return dataSource.update(oldValue.id, newValue)
    }

    override fun deleteCollectionImpl(keyId: Id<Key>): Set<Id<M>> {
        dataSource.deleteCollection(keyId)
        return emptySet() // deleting recursive with foreign key
    }
}

/** [EnvelopesRepository] */
open class EnvelopesRepositoryDatabaseBase(
    dataSource: EnvelopeDataSource
) : UpdatingRepositoryDatabaseImpl<Envelope, String>(dataSource)

/** [CategoriesRepository] */
open class CategoriesRepositoryDatabaseBase(
    dataSource: CategoryDataSource
) : UpdatingRepositoryDatabaseImpl<Category, Envelope>(dataSource)

/** [SpendingRepository] */
open class SpendingRepositoryDatabaseBase(
    dataSource: SpendingDataSource
) : UpdatingRepositoryDatabaseImpl<Spending, Category>(dataSource)
