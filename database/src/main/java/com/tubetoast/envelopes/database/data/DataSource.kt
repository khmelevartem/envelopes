package com.tubetoast.envelopes.database.data

import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.Id
import com.tubetoast.envelopes.common.domain.models.ImmutableModel
import com.tubetoast.envelopes.common.domain.models.Spending

abstract class DataSource<M : ImmutableModel<M>, Key, in DE : DatabaseEntity>(
    private val dao: StandardDao<DE>,
    private val converter: Converter<M, DE>
) {
    fun getAll(): List<M> = dao.getAll().map { converter.toDomainModel(it) }

    fun getCollection(parentKey: Id<Key>): List<M> =
        dao.getCollection(parentKey.code).map { converter.toDomainModel(it) }

    fun get(valueId: Id<M>): M? = dao.get(valueId.code)?.let { converter.toDomainModel(it) }

    fun write(value: M, keyId: Id<Key>) = dao.write(converter.toDatabaseEntity(value, keyId.code))

    fun delete(valueId: Id<M>) = dao.delete(valueId.code) != 0

    fun deleteCollection(keyId: Id<Key>) = dao.deleteCollection(keyId.code) != 0

    fun update(oldValueId: Id<M>, value: M): Boolean {
         return dao.get(oldValueId.code)?.foreignKey?.let { parentId ->
            dao.delete(oldValueId.code)
            dao.write(converter.toDatabaseEntity(value, parentId))
             true
        } ?: false
//        return dao.update(oldValueId.code, value)
    }
}

class EnvelopeDataSource(dao: EnvelopeDao, converter: Converter<Envelope, EnvelopeEntity>) :
    DataSource<Envelope, String, EnvelopeEntity>(dao, converter)

class CategoryDataSource(dao: CategoryDao, converter: Converter<Category, CategoryEntity>) :
    DataSource<Category, Envelope, CategoryEntity>(dao, converter)


class SpendingDataSource(dao: SpendingDao, converter: Converter<Spending, SpendingEntity>) :
    DataSource<Spending, Category, SpendingEntity>(dao, converter)

