package com.tubetoast.envelopes.common.data

import com.tubetoast.envelopes.common.domain.UpdatingCategoriesRepository
import com.tubetoast.envelopes.common.domain.UpdatingEnvelopesRepository
import com.tubetoast.envelopes.common.domain.UpdatingRepository
import com.tubetoast.envelopes.common.domain.UpdatingSpendingRepository
import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.Id
import com.tubetoast.envelopes.common.domain.models.ImmutableModel
import com.tubetoast.envelopes.common.domain.models.Spending

/**
 * Order of [repositories] matters
 */
open class CompositeUpdatingRepository<M : ImmutableModel<M>, Key>(
    private vararg val repositories: UpdatingRepository<M, Key>
) : UpdatingRepository<M, Key>() {
    override fun get(valueId: Id<M>): M? {
        repositories.forEach { repo ->
            repo.get(valueId)?.let { result -> return result }
        }
        return null
    }

    override fun getCollection(keyId: Id<Key>): Set<M> {
        repositories.forEach { repo ->
            repo.getCollection(keyId).takeIf { it.isNotEmpty() }?.let { return it }
        }
        return emptySet()
    }

    override fun addImpl(value: M, keyId: Id<Key>): Boolean {
        return repositories.map { repo ->
            repo.addImpl(value, keyId)
        }.any()
    }

    override fun deleteImpl(value: M): Boolean {
        return repositories.map { repo ->
            repo.deleteImpl(value)
        }.any()
    }

    override fun editImpl(oldValue: M, newValue: M): Boolean {
        return repositories.map { repo ->
            repo.editImpl(oldValue, newValue)
        }.any()
    }

    override fun deleteCollectionImpl(keyId: Id<Key>): Set<Id<M>> {
        return repositories.fold(mutableSetOf()) { set, repo ->
            set.apply { addAll(repo.deleteCollectionImpl(keyId)) }
        }
    }
}


/** [UpdatingEnvelopesRepository] */
open class CompositeEnvelopesRepositoryBase(
    vararg repositories: UpdatingRepository<Envelope, String>
) : CompositeUpdatingRepository<Envelope, String>(*repositories)

/** [UpdatingCategoriesRepository] */
open class CompositeCategoriesRepositoryBase(
    vararg repositories: UpdatingRepository<Category, Envelope>
) : CompositeUpdatingRepository<Category, Envelope>(*repositories)

/** [UpdatingSpendingRepository] */
open class CompositeSpendingRepositoryBase(
    vararg repositories: UpdatingRepository<Spending, Category>
) : CompositeUpdatingRepository<Spending, Category>(*repositories)
