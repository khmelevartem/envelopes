package com.tubetoast.envelopes.database.data

import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.Id
import com.tubetoast.envelopes.common.domain.models.ImmutableModel
import com.tubetoast.envelopes.common.domain.models.Spending

abstract class DataSource<M : ImmutableModel<M>, in Key>(
    private val dao: StandardDao<M, DatabaseEntity<M>>
) {
    fun getAll(): List<M> = dao.getAll().map { it.toDomainModel() }

    fun getCollection(parentKey: Id<Key>): List<M> =
        dao.getCollection(parentKey.code).map { it.toDomainModel() }

    fun get(valueId: Id<M>): M? = dao.get(valueId.code)?.toDomainModel()

    fun write(value: M, keyId: Id<Key>) = dao.write(getDatabaseEntity(value, keyId))

    fun delete(valueId: Id<M>) = dao.delete(valueId.code) != 0

    fun deleteCollection(keyId: Id<Key>) = dao.deleteCollection(keyId.code) != 0

//    fun update(oldValueId: Id<M>, value: M) = dao.update(oldValueId.code, value)

    protected abstract fun getDatabaseEntity(value: M, keyId: Id<Key>): DatabaseEntity<M>
}

class EnvelopeDataSource(envelopeDao: EnvelopeDao) : DataSource<Envelope, String>(envelopeDao) {
    override fun getDatabaseEntity(value: Envelope, keyId: Id<String>): DatabaseEntity<Envelope> {
        return EnvelopeEntity(value)
    }
}

class CategoryDataSource(envelopeDao: CategoryDao) : DataSource<Category, Envelope>(envelopeDao) {
    override fun getDatabaseEntity(value: Category, keyId: Id<Envelope>): DatabaseEntity<Category> {
        return CategoryEntity(value, keyId)
    }
}

class SpendingDataSource(envelopeDao: SpendingDao) : DataSource<Spending, Category>(envelopeDao) {
    override fun getDatabaseEntity(value: Spending, keyId: Id<Category>): DatabaseEntity<Spending> {
        return SpendingEntity(value, keyId)
    }
}

