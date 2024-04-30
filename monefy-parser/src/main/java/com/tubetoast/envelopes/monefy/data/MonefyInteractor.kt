package com.tubetoast.envelopes.monefy.data

import com.tubetoast.envelopes.common.data.EnvelopesRepositoryWithUndefinedCategories.Companion.undefinedCategoriesEnvelope
import com.tubetoast.envelopes.common.domain.UpdatingCategoriesRepository
import com.tubetoast.envelopes.common.domain.UpdatingSpendingRepository
import com.tubetoast.envelopes.common.domain.models.Date
import com.tubetoast.envelopes.common.domain.models.Spending
import com.tubetoast.envelopes.common.domain.put
import java.io.InputStream

class MonefyInteractor(
    private val monefyDataParser: MonefyDataParser,
    private val categoriesRepository: UpdatingCategoriesRepository,
    private val spendingRepository: UpdatingSpendingRepository,
) {
    @JvmOverloads
    suspend fun import(inputStream: InputStream, startFrom: Date? = null): Date? {
        val snapshots = monefyDataParser.parse(inputStream.reader().readLines(), startFrom)
        snapshots.onEach { snapshot ->
            val category = snapshot.category
            categoriesRepository.put(category) {
                undefinedCategoriesEnvelope.id
            }
            snapshot.transactions
                .filterIsInstance<Spending>()
                .map { spending -> category.id to spending }
                .let { spendingRepository.add(*it.toTypedArray()) }
        }
        return snapshots.flatMap { it.transactions.map { transaction -> transaction.date } }
            .maxOrNull() ?: startFrom
    }
}