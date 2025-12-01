package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.Id

interface CategoryInteractor {
    suspend fun getAll(): Set<Category>
    suspend fun getCategoryByName(name: String): Category?
    suspend fun getCategory(id: Id<Category>): Category?
    suspend fun addCategory(category: Category, envelopeId: Id<Envelope>)
    suspend fun editCategory(old: Category, new: Category)
    suspend fun moveCategory(category: Category, newEnvelopeId: Id<Envelope>)
    suspend fun deleteCategory(category: Category)
}
