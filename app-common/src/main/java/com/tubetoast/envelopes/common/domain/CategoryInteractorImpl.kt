package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.Hash

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

    override fun getCategory(hash: Hash<Category>): Category? {
        return repository.get(hash)
    }

    override fun addCategory(category: Category, envelopeHash: Hash<Envelope>) {
        repository.add(envelopeHash, category)
    }

    override fun editCategory(old: Category, new: Category) {
        repository.edit(old, new)
    }

    override fun moveCategory(category: Category, newEnvelopeHash: Hash<Envelope>) {
        repository.delete(category)
        repository.add(newEnvelopeHash, category)
    }

    override fun deleteCategory(category: Category) {
        repository.delete(category)
    }
}
