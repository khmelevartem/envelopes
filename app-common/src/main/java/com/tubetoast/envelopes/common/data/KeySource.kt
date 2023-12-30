package com.tubetoast.envelopes.common.data

import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.Id
import com.tubetoast.envelopes.common.domain.models.ImmutableModel

interface KeySource<M: ImmutableModel<M>, K> {
    fun getKeyId(value: M): Id<K>
}

interface CategoryKeySource: KeySource<Category, Envelope>

class CategoryKeySourceUndefinedImpl : CategoryKeySource {
    override fun getKeyId(value: Category): Id<Envelope> {
        return EnvelopesRepositoryWithUndefinedCategories.undefinedCategoriesEnvelope.id
    }
}