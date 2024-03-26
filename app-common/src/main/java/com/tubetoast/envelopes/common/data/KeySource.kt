package com.tubetoast.envelopes.common.data

import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.ImmutableModel

interface KeySource<M : ImmutableModel, K> {
    fun getKeyId(value: M): String
}

interface CategoryKeySource : KeySource<Category, Envelope>

class CategoryKeySourceUndefinedImpl : CategoryKeySource {
    override fun getKeyId(value: Category): String {
        return EnvelopesRepositoryWithUndefinedCategories.undefinedCategoriesEnvelope.id
    }
}
