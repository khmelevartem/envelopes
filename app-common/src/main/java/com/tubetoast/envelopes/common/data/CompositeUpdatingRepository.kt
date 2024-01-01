package com.tubetoast.envelopes.common.data

import com.tubetoast.envelopes.common.domain.UpdatingRepository
import com.tubetoast.envelopes.common.domain.models.Id
import com.tubetoast.envelopes.common.domain.models.ImmutableModel

/**
 * Order of [repositories] matters
 */
internal class CompositeUpdatingRepository<M : ImmutableModel<M>, Key>(
    private val repositories: List<UpdatingRepository<M, Key>>
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
        repositories.forEach { repo ->
            if(repo.addImpl(value, keyId)) return true
        }
        return false
    }

    override fun deleteImpl(value: M): Boolean {
        repositories.forEach { repo ->
            if(repo.deleteImpl(value)) return true
        }
        return false
    }

    override fun editImpl(oldValue: M, newValue: M): Boolean {
        repositories.forEach { repo ->
            if(repo.editImpl(oldValue, newValue)) return true
        }
        return false
    }

    override fun deleteCollectionImpl(keyId: Id<Key>): Set<Id<M>> {
        repositories.forEach { repo ->
            repo.deleteCollectionImpl(keyId).takeIf { it.isNotEmpty() }?.let { return it }
        }
        return emptySet()
    }
}