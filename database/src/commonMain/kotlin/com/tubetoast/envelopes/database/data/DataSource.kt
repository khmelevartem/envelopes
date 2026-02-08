package com.tubetoast.envelopes.database.data

import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.Goal
import com.tubetoast.envelopes.common.domain.models.Id
import com.tubetoast.envelopes.common.domain.models.ImmutableModel
import com.tubetoast.envelopes.common.domain.models.Root
import com.tubetoast.envelopes.common.domain.models.Spending
import com.tubetoast.envelopes.common.domain.models.id
import com.tubetoast.envelopes.database.data.dao.CategoryDao
import com.tubetoast.envelopes.database.data.dao.EnvelopeDao
import com.tubetoast.envelopes.database.data.dao.GoalDao
import com.tubetoast.envelopes.database.data.dao.SpendingDao
import com.tubetoast.envelopes.database.data.dao.StandardDao

// Model, Key, ModelDatabaseEntity, KeyDatabaseEntity
abstract class DataSource<M, K, MDE, KDE>(
    private val dao: StandardDao<MDE>,
    private val converter: Converter<M, MDE>,
    private val parentDao: StandardDao<KDE>?,
    private val parentConverter: Converter<K, KDE>?
) where M : ImmutableModel<M>, K : ImmutableModel<K>, MDE : DatabaseEntity, KDE : DatabaseEntity {
    suspend fun get(valueId: Id<M>): M? = dao.get(valueId.code)?.let { converter.toDomainModel(it) }

    suspend fun getCollection(parentId: Id<K>): List<M> =
        parentDao
            ?.get(parentId.code)
            ?.primaryKey
            ?.let { foreignKey ->
                dao.getCollection(foreignKey).map { converter.toDomainModel(it) }
            }.orEmpty()

    suspend fun getAll(): List<M> = dao.getAll().map { converter.toDomainModel(it) }

    suspend fun getAllByKeys(): Map<Id<K>, Set<M>> {
        val data = dao.getAll()
        val map = mutableMapOf<Int, MutableSet<M>>()
        data.forEach {
            map
                .getOrPut(it.foreignKey) { mutableSetOf() }
                .add(converter.toDomainModel(it))
        }

        return map.mapKeys {
            parentDao
                ?.getByKey(it.key)
                ?.let { parent -> parentConverter?.toDomainModel(parent)?.id }
                ?: Root.id.id()
        }
    }

    suspend fun getKey(valueId: Id<M>): Id<K>? =
        dao.get(valueId.code)?.foreignKey?.let { parentPrimaryKey ->
            parentDao?.getByKey(parentPrimaryKey)?.let {
                parentConverter?.toDomainModel(it)?.id
            }
        }

    suspend fun write(
        value: M,
        parentId: Id<K>
    ) = try {
        val foreignKey = if (parentDao == null) {
            Root.id.code
        } else {
            parentDao.get(parentId.code)?.primaryKey
                ?: throw IllegalArgumentException("Must have parent")
        }
        dao.write(converter.toDatabaseEntity(value, foreignKey))
        true // fix it with custom insert
    } catch (e: IllegalArgumentException) {
        false
    }

    suspend fun delete(valueId: Id<M>) = dao.delete(valueId.code) != 0

    suspend fun deleteCollection(parentID: Id<K>) =
        parentDao?.get(parentID.code)?.primaryKey?.let { foreignKey ->
            dao.deleteCollection(foreignKey) != 0
        }

    suspend fun deleteAll() = dao.deleteAll() != 0

    suspend fun update(
        oldValueId: Id<M>,
        value: M
    ): Boolean {
        val oldMDE = dao.get(oldValueId.code) ?: return false
        val newMDE = converter.toDatabaseEntity(value, oldMDE.foreignKey, oldMDE.primaryKey)
        return dao.update(newMDE) != 0
    }

    suspend fun move(
        value: M,
        newParentId: Id<K>
    ): Boolean {
        val oldMDE = dao.get(value.id.code) ?: return false
        val newForeignKey = parentDao?.get(newParentId.code)?.primaryKey ?: return false
        val newMDE = converter.toDatabaseEntity(value, newForeignKey, oldMDE.primaryKey)
        return dao.update(newMDE) != 0
    }
}

class GoalsDataSource(
    dao: GoalDao,
    goalConverter: Converter<Goal, GoalEntity>
) : DataSource<Goal, Root, GoalEntity, GoalEntity>(dao, goalConverter, null, null)

class EnvelopeDataSource(
    dao: EnvelopeDao,
    envelopeConverter: Converter<Envelope, EnvelopeEntity>
) : DataSource<Envelope, Root, EnvelopeEntity, EnvelopeEntity>(dao, envelopeConverter, null, null)

class CategoryDataSource(
    dao: CategoryDao,
    categoryConverter: Converter<Category, CategoryEntity>,
    parentDao: StandardDao<EnvelopeEntity>,
    envelopeConverter: Converter<Envelope, EnvelopeEntity>
) : DataSource<Category, Envelope, CategoryEntity, EnvelopeEntity>(
        dao,
        categoryConverter,
        parentDao,
        envelopeConverter
    )

class SpendingDataSource(
    dao: SpendingDao,
    converter: Converter<Spending, SpendingEntity>,
    parentDao: StandardDao<CategoryEntity>,
    parentConverter: Converter<Category, CategoryEntity>
) : DataSource<Spending, Category, SpendingEntity, CategoryEntity>(
        dao,
        converter,
        parentDao,
        parentConverter
    )
