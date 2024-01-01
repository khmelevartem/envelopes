package com.tubetoast.envelopes.monefy.data

import com.tubetoast.envelopes.common.data.CategoriesRepositoryInMemoryBase
import com.tubetoast.envelopes.common.data.CategoryKeySource
import com.tubetoast.envelopes.common.data.CategoryKeySourceUndefinedImpl

class MonefyCategoryRepositoryImpl(
    monefySource: MonefySource,
    categoryKeySource: CategoryKeySource = CategoryKeySourceUndefinedImpl()
) : CategoriesRepositoryInMemoryBase() {
    init {
        if (anythingChanged(monefySource, categoryKeySource)) update?.invoke()

    }

    private fun anythingChanged(
        monefySource: MonefySource,
        categoryKeySource: CategoryKeySource
    ) = monefySource.categorySnapshots.map { snapshot ->
        val category = snapshot.category
        val envelopeId = categoryKeySource.getKeyId(category)
        addImpl(category, envelopeId)
    }.any()
}