package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.domain.models.Category

interface CategoryInteractor {
    suspend fun getCategoryByName(name: String): Category?
    suspend fun getCategory(id: String): Category?
    suspend fun addCategory(category: Category, envelopeId: String)
    suspend fun editCategory(old: Category, new: Category)
    suspend fun moveCategory(category: Category, newEnvelopeId: String)
    suspend fun deleteCategory(category: Category)
}
