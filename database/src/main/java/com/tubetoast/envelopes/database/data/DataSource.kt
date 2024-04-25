package com.tubetoast.envelopes.database.data

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.Id
import com.tubetoast.envelopes.common.domain.models.ImmutableModel
import com.tubetoast.envelopes.common.domain.models.Root
import com.tubetoast.envelopes.common.domain.models.Spending

// Model, Key, ModelDatabaseEntity, KeyDatabaseEntity
abstract class DataSource<M, K, MDE, KDE>(
    private val dao: StandardDao<MDE>,
    private val converter: Converter<M, MDE>,
    private val parentDao: StandardDao<KDE>?,
    private val parentConverter: Converter<K, KDE>?
) where M : ImmutableModel<M>, K : ImmutableModel<K>, MDE : DatabaseEntity, KDE : DatabaseEntity {

    fun getAll(): List<M> = dao.getAll().map { converter.toDomainModel(it) }

    fun getCollection(keyId: Id<K>): List<M> =
        parentDao?.get(keyId.code)?.primaryKey?.let { foreignKey ->
            dao.getCollection(foreignKey).map { converter.toDomainModel(it) }
        }.orEmpty()

    fun get(valueId: Id<M>): M? = dao.get(valueId.code)?.let { converter.toDomainModel(it) }

    fun getKey(valueId: Id<M>): Id<K>? =
        dao.get(valueId.code)?.foreignKey?.let { parentPrimaryKey ->
            parentDao?.getByKey(parentPrimaryKey)?.let {
                parentConverter?.toDomainModel(it)?.id
            }

        }

    fun write(value: M, keyId: Id<K>) = try {
        val foreignKey = parentDao?.get(keyId.code)?.primaryKey
        dao.write(converter.toDatabaseEntity(value, foreignKey))
        true // fix it with custom insert
    } catch (e: SQLiteConstraintException) {
        Log.w("Envelopes", "insert failed", e)
        false
    } catch (e: IllegalArgumentException) {
        Log.w("Envelopes", "insert failed", e)
        false
    }

    fun delete(valueId: Id<M>) = dao.delete(valueId.code) != 0

    fun deleteCollection(keyId: Id<K>) =
        parentDao?.get(keyId.code)?.primaryKey?.let { foreignKey ->
            dao.deleteCollection(foreignKey) != 0
        }

    fun update(oldValueId: Id<M>, value: M): Boolean {
        val oldMDE = dao.get(oldValueId.code) ?: return false
        val newMDE = converter.toDatabaseEntity(value, oldMDE.foreignKey, oldMDE.primaryKey)
        return dao.update(newMDE) != 0
    }
}

class EnvelopeDataSource(dao: EnvelopeDao, converter: Converter<Envelope, EnvelopeEntity>) :
    DataSource<Envelope, Root, EnvelopeEntity, EnvelopeEntity>(dao, converter, null, null)

class CategoryDataSource(
    dao: CategoryDao,
    converter: Converter<Category, CategoryEntity>,
    parentDao: StandardDao<EnvelopeEntity>,
    parentConverter: Converter<Envelope, EnvelopeEntity>
) : DataSource<Category, Envelope, CategoryEntity, EnvelopeEntity>(
    dao,
    converter,
    parentDao,
    parentConverter
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