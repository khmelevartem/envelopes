package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.Hash

interface CategoryInteractor {
    fun getCategoryByName(name: String): Category?
    fun getCategory(hash: Hash<Category>): Category?
    fun addCategory(category: Category, envelopeHash: Hash<Envelope>)
    fun editCategory(old: Category, new: Category)
    fun moveCategory(category: Category, newEnvelopeHash: Hash<Envelope>)
    fun deleteCategory(category: Category)
}
