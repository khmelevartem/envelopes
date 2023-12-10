package com.tubetoast.envelopes.common.data

import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Hash
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

    override fun getByKey(key: Hash<Category>): Spending {
        TODO("Not yet implemented")
    }

    override fun put(key: Hash<Category>, newValue: Spending) {
        TODO("Not yet implemented")
    }

    override fun delete(key: Hash<Category>, value: Spending) {
        TODO("Not yet implemented")
    }

    override fun deleteAll(key: Hash<Category>) {
        TODO("Not yet implemented")
    }

    override fun clear() {
        TODO("Not yet implemented")
    }
}
