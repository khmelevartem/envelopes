package com.tubetoast.envelopes.common.data

import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Id
import com.tubetoast.envelopes.common.domain.models.Spending
import java.io.File

class MonefyDataSource(private val file: File) : SpendingSource {

    init {
        parseFile()
    }

    private fun parseFile() {

    }

    override fun getAll(): Set<Spending> {
        TODO("Not yet implemented")
    }

    override fun getByKey(key: Id<Category>): Spending {
        TODO("Not yet implemented")
    }

    override fun put(key: Id<Category>, newValue: Spending) {
        TODO("Not yet implemented")
    }

    override fun delete(key: Id<Category>, value: Spending) {
        TODO("Not yet implemented")
    }

    override fun deleteAll(key: Id<Category>) {
        TODO("Not yet implemented")
    }

    override fun clear() {
        TODO("Not yet implemented")
    }
}
