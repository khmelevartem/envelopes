package com.tubetoast.envelopes.android.presentation.ui.screens

import com.tubetoast.envelopes.common.domain.CategoryInteractor
import com.tubetoast.envelopes.common.domain.models.Amount
import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.Id

class CategoryInteractorStub(
    val categories: MutableList<Category> = mutableListOf(
        Category("test", Amount(10))
    )
) : CategoryInteractor {

    override suspend fun getCategoryByName(name: String): Category? {
        return categories.find { it.name == name }
    }

    override suspend fun getCategory(id: Id<Category>): Category? {
        return categories.find { it.id == id }
    }

    override suspend fun addCategory(category: Category, envelopeId: Id<Envelope>) {
        categories.add(category)
    }

    override suspend fun editCategory(old: Category, new: Category) {
        categories.run {
            remove(old)
            add(new)
        }
    }

    override suspend fun moveCategory(category: Category, newEnvelopeId: Id<Envelope>) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCategory(category: Category) {
        categories.remove(category)
    }
}
