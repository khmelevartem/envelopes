package com.tubetoast.envelopes.database.data

import com.tubetoast.envelopes.common.domain.UpdatingCategoriesRepository
import com.tubetoast.envelopes.common.domain.UpdatingEnvelopesRepository
import com.tubetoast.envelopes.common.domain.UpdatingRepository
import com.tubetoast.envelopes.common.domain.UpdatingSpendingRepository
import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.Id
import com.tubetoast.envelopes.common.domain.models.ImmutableModel
import com.tubetoast.envelopes.common.domain.models.Root
import com.tubetoast.envelopes.common.domain.models.Spending

open class UpdatingRepositoryDatabaseImpl<M : ImmutableModel<M>, Key : ImmutableModel<Key>>(
    private val dataSource: DataSource<M, Key, *, *>
) : UpdatingRepository<M, Key>() {

    override fun get(valueId: Id<M>): M? {
        return dataSource.get(valueId)
    }

    override fun getCollection(keyId: Id<Key>): Set<M> {
        return dataSource.getCollection(keyId).toSet()
    }

    override fun moveImpl(value: M, newKyId: Id<Key>): Boolean {
        return dataSource.move(value, newKyId)
    }

    override fun getAll(): Set<M> {
        return dataSource.getAll().toSet()
    }

    override fun getAllByKeys(): Map<Id<Key>, Set<M>> {
        return dataSource.getAllByKeys()
    }

    override fun getKey(valueId: Id<M>): Id<Key>? {
        return dataSource.getKey(valueId)
    }

    override fun addImpl(value: M, keyId: Id<Key>): Boolean {
        return dataSource.write(value, keyId)
    }

    override fun deleteImpl(value: M): Boolean {
        return dataSource.delete(value.id)
    }

    override fun editImpl(oldValue: M, newValue: M): Boolean {
        return dataSource.update(oldValue.id, newValue)
    }

    override fun deleteCollectionImpl(keyId: Id<Key>): Set<Id<M>> {
        dataSource.deleteCollection(keyId)
        return emptySet() // deleting recursive with foreign key
    }

    override fun deleteAllImpl(): Set<Id<M>> {
        dataSource.deleteAll()
        return emptySet() // deleting recursive with foreign key
    }
}

/** [UpdatingEnvelopesRepository] */
class EnvelopesRepositoryDatabaseImpl(
    dataSource: EnvelopeDataSource
) : UpdatingRepositoryDatabaseImpl<Envelope, Root>(dataSource)

/** [UpdatingCategoriesRepository] */
class CategoriesRepositoryDatabaseImpl(
    dataSource: CategoryDataSource
) : UpdatingRepositoryDatabaseImpl<Category, Envelope>(dataSource)

/** [UpdatingSpendingRepository] */
class SpendingRepositoryDatabaseImpl(
    dataSource: SpendingDataSource
) : UpdatingRepositoryDatabaseImpl<Spending, Category>(dataSource)
