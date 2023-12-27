package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.Id

class CategoryInteractorImpl(
    private val repository: CategoriesRepository,
) : CategoryInteractor {
    override fun getCategoryByName(name: String): Category? {
        return repository
            .getAll()
            .find {
                it.name == name
            }
    }

    override fun getCategory(id: Id<Category>): Category? {
        return repository.get(id)
    }

    override fun addCategory(category: Category, envelopeId: Id<Envelope>) {
        repository.add(envelopeId, category)
    }

    override fun editCategory(old: Category, new: Category) {
        repository.edit(old, new)
    }

    override fun moveCategory(category: Category, newEnvelopeId: Id<Envelope>) {
        repository.delete(category)
        repository.add(newEnvelopeId, category)
    }

    override fun deleteCategory(category: Category) {
        repository.delete(category)
    }
}
