package com.tubetoast.envelopes.database.data

import com.tubetoast.envelopes.common.domain.CategoriesRepository
import com.tubetoast.envelopes.common.domain.EnvelopesRepository
import com.tubetoast.envelopes.common.domain.GoalsRepository
import com.tubetoast.envelopes.common.domain.Repository
import com.tubetoast.envelopes.common.domain.SpendingRepository
import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.Goal
import com.tubetoast.envelopes.common.domain.models.Id
import com.tubetoast.envelopes.common.domain.models.ImmutableModel
import com.tubetoast.envelopes.common.domain.models.Root
import com.tubetoast.envelopes.common.domain.models.Spending
import com.tubetoast.envelopes.common.settings.MutableSettingsRepository
import com.tubetoast.envelopes.common.settings.Setting
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

open class DatabaseRepository<M : ImmutableModel<M>, Key : ImmutableModel<Key>>(
    private val dataSource: DataSource<M, Key, *, *>
) : Repository<M, Key> {

    override fun get(valueId: Id<M>): M? {
        return dataSource.get(valueId)
    }

    override fun getCollection(keyId: Id<Key>): Set<M> {
        return dataSource.getCollection(keyId).toSet()
    }

    override fun move(value: M, newKey: Id<Key>) {
        dataSource.move(value, newKey)
    }

    override fun getAll(): Set<M> {
        return dataSource.getAll().toSet()
    }

    override fun getKey(valueId: Id<M>): Id<Key>? {
        return dataSource.getKey(valueId)
    }

    override fun add(keyId: Id<Key>, value: M) {
        dataSource.write(value, keyId)
    }

    override fun add(vararg values: Pair<Id<Key>, M>) {
        values.forEach {
            add(it.first, it.second)
        }
    }

    override fun delete(value: M) {
        dataSource.delete(value.id)
    }

    override fun edit(oldValue: M, newValue: M) {
        dataSource.update(oldValue.id, newValue)
    }

    override fun deleteCollection(keyId: Id<Key>) {
        dataSource.deleteCollection(keyId)
    }

    override fun deleteAll() {
        dataSource.deleteAll()
    }
}

/** [GoalsRepository] */
class GoalsDatabaseRepository(
    dataSource: GoalsDataSource,
    settingsRepository: MutableSettingsRepository
) : CleanableRepository<Goal, Root>(dataSource, settingsRepository, Setting.Key.DELETE_GOALS)

/** [EnvelopesRepository] */
class EnvelopesDatabaseRepository(
    dataSource: EnvelopeDataSource
) : DatabaseRepository<Envelope, Root>(dataSource)

/** [CategoriesRepository] */
class CategoriesDatabaseRepository(
    dataSource: CategoryDataSource
) : DatabaseRepository<Category, Envelope>(dataSource)

/** [SpendingRepository] */
class SpendingDatabaseRepository(
    dataSource: SpendingDataSource,
    settingsRepository: MutableSettingsRepository
) : CleanableRepository<Spending, Category>(dataSource, settingsRepository, Setting.Key.DELETE_SPENDING)

abstract class CleanableRepository<M : ImmutableModel<M>, Key : ImmutableModel<Key>>(
    dataSource: DataSource<M, Key, *, *>,
    private val settingsRepository: MutableSettingsRepository,
    private val settingKey: Setting.Key,
    scope: CoroutineScope = CoroutineScope(Job())
) : DatabaseRepository<M, Key>(dataSource) {

    init {
        scope.launch {
            settingsRepository.getSettingFlow(settingKey).collect {
                if (it.checked) deleteAll()
            }
        }
    }

    override fun add(keyId: Id<Key>, value: M) {
        val added = super.add(keyId, value)
        settingsRepository.run {
            val setting = getSetting(settingKey)
            if (setting.checked) {
                saveChanges(
                    setting.copy(checked = false)
                )
            }
        }
        return added
    }
}
