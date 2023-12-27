package com.tubetoast.envelopes.monefy.data

import com.tubetoast.envelopes.common.data.CategoriesRepositoryImpl

class MonefyCategoryRepositoryImpl(
    monefySource: MonefySource,
    categoryKeySource: CategoryKeySource = CategoryKeySourceImpl()
) : CategoriesRepositoryImpl() {
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