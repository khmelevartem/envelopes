package com.tubetoast.envelopes.database.data

import com.tubetoast.envelopes.common.domain.UpdatingCategoriesRepository
import com.tubetoast.envelopes.common.domain.UpdatingEnvelopesRepository
import com.tubetoast.envelopes.common.domain.UpdatingRepository
import com.tubetoast.envelopes.common.domain.UpdatingSpendingRepository
import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.ImmutableModel
import com.tubetoast.envelopes.common.domain.models.Spending

open class UpdatingRepositoryDatabaseImpl<M : ImmutableModel, Key>(
    private val dataSource: DataSource<M, Key, *>
) : UpdatingRepository<M, Key>() {

    override fun get(valueId: String): M? {
        return dataSource.get(valueId)
    }

    override fun getCollection(keyId: String): Set<M> {
        return dataSource.getCollection(keyId).toSet()
    }

    override fun getAll(): Set<M> {
        return dataSource.getAll().toSet()
    }

    override fun addImpl(value: M, keyId: String): Boolean {
        dataSource.write(value, keyId)
        return true // fix it with custom insert
    }

    override fun deleteImpl(value: M): Boolean {
        return dataSource.delete(value.id)
    }

    override fun editImpl(oldValue: M, newValue: M): Boolean {
        return dataSource.update(oldValue.id, newValue)
    }

    override fun deleteCollectionImpl(keyId: String): Set<String> {
        dataSource.deleteCollection(keyId)
        return emptySet() // deleting recursive with foreign key
    }
}

/** [UpdatingEnvelopesRepository] */
open class EnvelopesRepositoryDatabaseBase(
    dataSource: EnvelopeDataSource
) : UpdatingRepositoryDatabaseImpl<Envelope, String>(dataSource)

/** [UpdatingCategoriesRepository] */
open class CategoriesRepositoryDatabaseBase(
    dataSource: CategoryDataSource
) : UpdatingRepositoryDatabaseImpl<Category, Envelope>(dataSource)

/** [UpdatingSpendingRepository] */
open class SpendingRepositoryDatabaseBase(
    dataSource: SpendingDataSource
) : UpdatingRepositoryDatabaseImpl<Spending, Category>(dataSource)
