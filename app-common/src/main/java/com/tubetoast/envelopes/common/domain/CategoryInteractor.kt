package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.Id

interface CategoryInteractor {
    fun getCategoryByName(name: String): Category?
    fun getCategory(id: Id<Category>): Category?
    fun addCategory(category: Category, envelopeId: Id<Envelope>)
    fun editCategory(old: Category, new: Category)
    fun moveCategory(category: Category, newEnvelopeId: Id<Envelope>)
    fun deleteCategory(category: Category)
}
