package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.Id
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CategoryInteractorImpl(
    private val repository: UpdatingCategoriesRepository
) : CategoryInteractor {
    private val dispatcher = Dispatchers.IO
    override suspend fun getCategoryByName(name: String): Category? {
        return withContext(dispatcher) {
            repository
                .getAll()
                .find {
                    it.name == name
                }
        }
    }

    override suspend fun getCategory(id: Id<Category>): Category? {
        return withContext(dispatcher) { repository.get(id) }
    }

    override suspend fun addCategory(category: Category, envelopeId: Id<Envelope>) {
        withContext(dispatcher) { repository.add(envelopeId, category) }
    }

    override suspend fun editCategory(old: Category, new: Category) {
        withContext(dispatcher) {
            repository.edit(old, new)
        }
    }

    override suspend fun moveCategory(category: Category, newEnvelopeId: Id<Envelope>) {
        withContext(dispatcher) {
            repository.move(category, newEnvelopeId)
        }
    }

    override suspend fun deleteCategory(category: Category) {
        withContext(dispatcher) {
            repository.delete(category)
        }
    }
}
