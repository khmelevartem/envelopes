package com.tubetoast.envelopes.common.data

import com.tubetoast.envelopes.common.domain.models.Amount
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.Id
import com.tubetoast.envelopes.common.domain.models.Root

class EnvelopesRepositoryWithUndefinedCategories : EnvelopesRepositoryInMemoryBase() {

    override fun get(valueId: Id<Envelope>): Envelope {
        return undefinedCategoriesEnvelope
    }

    override fun getAll(): Set<Envelope> {
        return setOf(undefinedCategoriesEnvelope)
    }

    override fun getCollection(keyId: Id<Root>): MutableSet<Envelope> {
        return mutableSetOf(undefinedCategoriesEnvelope)
    }

    override fun deleteImpl(value: Envelope): Boolean {
        return false
    }

    override fun deleteCollectionImpl(keyId: Id<Root>): Set<Id<Envelope>> {
        return emptySet()
    }

    override fun getAllByKeys(): Map<Id<Root>, Set<Envelope>> {
        return mapOf(Root.id to setOf(undefinedCategoriesEnvelope))
    }

    override fun addImpl(value: Envelope, keyId: Id<Root>): Boolean {
        return false
    }

    override fun editImpl(oldValue: Envelope, newValue: Envelope): Boolean {
        return false
    }

    companion object {
        val undefinedCategoriesEnvelope =
            Envelope(name = "Undefined", limit = Amount(Integer.MAX_VALUE))
    }
}
