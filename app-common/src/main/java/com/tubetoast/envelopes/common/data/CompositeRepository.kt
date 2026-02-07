package com.tubetoast.envelopes.common.data

import com.tubetoast.envelopes.common.domain.CategoriesRepository
import com.tubetoast.envelopes.common.domain.EnvelopesRepository
import com.tubetoast.envelopes.common.domain.Repository
import com.tubetoast.envelopes.common.domain.SpendingRepository
import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.Id
import com.tubetoast.envelopes.common.domain.models.ImmutableModel
import com.tubetoast.envelopes.common.domain.models.Root
import com.tubetoast.envelopes.common.domain.models.Spending
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/**
 * Order of [repositories] matters
 */
open class CompositeRepository<M : ImmutableModel<M>, Key : ImmutableModel<Key>>(
    private vararg val repositories: Repository<M, Key>
) : Repository<M, Key> {

    private val lock = ReentrantLock()

    override fun get(valueId: Id<M>): M? {
        lock.withLock {
            repositories.forEach { repo ->
                repo.get(valueId)?.let { result -> return result }
            }
        }
        return null
    }

    override fun getCollection(keyId: Id<Key>): Set<M> = lock.withLock {
        repositories.fold(mutableSetOf()) { set, repo ->
            set.apply { addAll(repo.getCollection(keyId)) }
        }
    }

    override fun getAll(): Set<M> = lock.withLock {
        repositories.fold(mutableSetOf()) { set, repo ->
            set.apply { addAll(repo.getAll()) }
        }
    }

    override fun getKey(valueId: Id<M>): Id<Key>? {
        lock.withLock {
            repositories.forEach { repo ->
                repo.getKey(valueId)?.let { return it }
            }
        }
        return null
    }

    override fun add(keyId: Id<Key>, value: M) = lock.withLock {
        repositories.forEach { repo ->
            repo.add(keyId, value)
        }
    }

    override fun add(vararg values: Pair<Id<Key>, M>) = lock.withLock {
        repositories.forEach {
            it.add(*values)
        }
    }

    override fun delete(value: M) = lock.withLock {
        repositories.forEach { repo ->
            repo.delete(value)
        }
    }

    override fun deleteCollection(keyId: Id<Key>) = lock.withLock {
        repositories.forEach { repo ->
            repo.deleteCollection(keyId)
        }
    }

    override fun deleteAll() = lock.withLock {
        repositories.forEach {
            it.deleteAll()
        }
    }

    override fun move(value: M, newKey: Id<Key>) = lock.withLock {
        repositories.forEach { repo ->
            repo.move(value, newKey)
        }
    }

    override fun edit(oldValue: M, newValue: M) = lock.withLock {
        repositories.forEach { repo ->
            repo.edit(oldValue, newValue)
        }
    }
}

/** [EnvelopesRepository] */
open class CompositeEnvelopesRepositoryBase(
    vararg repositories: Repository<Envelope, Root>
) : CompositeRepository<Envelope, Root>(*repositories)

/** [CategoriesRepository] */
open class CompositeCategoriesRepositoryBase(
    vararg repositories: Repository<Category, Envelope>
) : CompositeRepository<Category, Envelope>(*repositories)

/** [SpendingRepository] */
open class CompositeSpendingRepositoryBase(
    vararg repositories: Repository<Spending, Category>
) : CompositeRepository<Spending, Category>(*repositories)
