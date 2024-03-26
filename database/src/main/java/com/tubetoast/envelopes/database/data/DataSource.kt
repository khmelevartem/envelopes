package com.tubetoast.envelopes.database.data

import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.ImmutableModel
import com.tubetoast.envelopes.common.domain.models.Spending

abstract class DataSource<M : ImmutableModel, Key, in DE : DatabaseEntity>(
    private val dao: StandardDao<DE>,
    private val converter: Converter<M, DE>
) {
    fun getAll(): List<M> = dao.getAll().map { converter.toDomainModel(it) }

    fun getCollection(parentKey: String): List<M> =
        dao.getCollection(parentKey).map { converter.toDomainModel(it) }

    fun get(valueId: String): M? = dao.get(valueId)?.let { converter.toDomainModel(it) }

    fun write(value: M, keyId: String) = dao.write(converter.toDatabaseEntity(value, keyId))

    fun delete(valueId: String) = dao.delete(valueId) != 0

    fun deleteCollection(keyId: String) = dao.deleteCollection(keyId) != 0

    fun update(oldValueId: String, value: M): Boolean {
        return dao.get(oldValueId)?.foreignKey?.let { parentId ->
            dao.delete(oldValueId)
            dao.write(converter.toDatabaseEntity(value, parentId))
            true
        } ?: false
//        return dao.update(oldValueId, value)
    }
}

class EnvelopeDataSource(dao: EnvelopeDao, converter: Converter<Envelope, EnvelopeEntity>) :
    DataSource<Envelope, String, EnvelopeEntity>(dao, converter)

class CategoryDataSource(dao: CategoryDao, converter: Converter<Category, CategoryEntity>) :
    DataSource<Category, Envelope, CategoryEntity>(dao, converter)

class SpendingDataSource(dao: SpendingDao, converter: Converter<Spending, SpendingEntity>) :
    DataSource<Spending, Category, SpendingEntity>(dao, converter)
