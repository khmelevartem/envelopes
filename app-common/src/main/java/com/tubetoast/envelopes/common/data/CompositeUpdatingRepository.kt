package com.tubetoast.envelopes.common.data

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
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/**
 * Order of [repositories] matters
 */
open class CompositeUpdatingRepository<M : ImmutableModel<M>, Key : ImmutableModel<Key>>(
    private vararg val repositories: UpdatingRepository<M, Key>
) : UpdatingRepository<M, Key>() {

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

    override fun addImpl(value: M, keyId: Id<Key>): Boolean = lock.withLock {
        repositories.asSequence().filter { repo ->
            repo.addImpl(value, keyId)
        }.any()
    }

    override fun deleteImpl(value: M): Boolean =
        lock.withLock {
            repositories.map { repo ->
                repo.deleteImpl(value)
            }.any()
        }

    override fun editImpl(oldValue: M, newValue: M): Boolean =
        lock.withLock {
            repositories.asSequence().filter { repo ->
                repo.editImpl(oldValue, newValue)
            }.any()
        }

    override fun moveImpl(value: M, newKyId: Id<Key>): Boolean =
        lock.withLock {
            repositories.asSequence().filter { repo ->
                repo.moveImpl(value, newKyId)
            }.any()
        }

    override fun deleteCollectionImpl(keyId: Id<Key>): Set<Id<M>> =
        lock.withLock {
            repositories.fold(mutableSetOf()) { set, repo ->
                set.apply { addAll(repo.deleteCollectionImpl(keyId)) }
            }
        }
}

/** [UpdatingEnvelopesRepository] */
open class CompositeEnvelopesRepositoryBase(
    vararg repositories: UpdatingRepository<Envelope, Root>
) : CompositeUpdatingRepository<Envelope, Root>(*repositories)

/** [UpdatingCategoriesRepository] */
open class CompositeCategoriesRepositoryBase(
    vararg repositories: UpdatingRepository<Category, Envelope>
) : CompositeUpdatingRepository<Category, Envelope>(*repositories)

/** [UpdatingSpendingRepository] */
open class CompositeSpendingRepositoryBase(
    vararg repositories: UpdatingRepository<Spending, Category>
) : CompositeUpdatingRepository<Spending, Category>(*repositories)
