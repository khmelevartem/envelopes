package com.tubetoast.envelopes.monefy.data

import com.tubetoast.envelopes.common.domain.models.Amount
import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.Id
import com.tubetoast.envelopes.common.domain.models.ImmutableModel

interface KeySource<M: ImmutableModel<M>, K> {
    fun getKeyId(value: M): Id<K>
}

interface CategoryKeySource: KeySource<Category, Envelope>

class CategoryKeySourceImpl : CategoryKeySource {
    override fun getKeyId(value: Category): Id<Envelope> {
        return when (value.name) {
            "Такси" -> Envelope("1", Amount.ZERO)
            else -> Envelope("2", Amount.ZERO)
        }.id
    }
}