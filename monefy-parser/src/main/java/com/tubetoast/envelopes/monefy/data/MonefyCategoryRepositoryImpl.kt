package com.tubetoast.envelopes.monefy.data

import com.tubetoast.envelopes.common.data.CategoriesRepositoryImpl
import com.tubetoast.envelopes.common.domain.models.Amount
import com.tubetoast.envelopes.common.domain.models.Envelope

class MonefyCategoryRepositoryImpl(monefySource: MonefySource) : CategoriesRepositoryImpl() {
    init {
        val envelopeHash = Envelope("all", Amount.ZERO).hash
        val categories = monefySource.categorySnapshots.mapTo(mutableSetOf()) { it.category }
        sets[envelopeHash] = categories
        categories.forEach { keys[it.hash] = envelopeHash }
    }
}