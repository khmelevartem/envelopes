package com.tubetoast.envelopes.monefy.data

import com.tubetoast.envelopes.common.domain.UpdatingCategoriesRepository
import com.tubetoast.envelopes.common.domain.UpdatingEnvelopesRepository
import com.tubetoast.envelopes.common.domain.UpdatingSpendingRepository
import com.tubetoast.envelopes.common.domain.models.Date
import com.tubetoast.envelopes.common.domain.models.Spending
import com.tubetoast.envelopes.common.domain.models.undefinedCategoriesEnvelope
import com.tubetoast.envelopes.common.domain.put
import java.io.InputStream

class MonefyInteractor(
    private val monefyDataParser: MonefyDataParser,
    private val envelopesRepository: UpdatingEnvelopesRepository,
    private val categoriesRepository: UpdatingCategoriesRepository,
    private val spendingRepository: UpdatingSpendingRepository,
) {
    @JvmOverloads
    suspend fun import(inputStream: InputStream, startFrom: Date? = null): Date? {
        val snapshots = monefyDataParser.parse(inputStream.reader().readLines(), startFrom)
        var prepare = true
        snapshots.onEach { snapshot ->
            val category = snapshot.category
            categoriesRepository.put(category) {
                if (prepare) {
                    envelopesRepository.put(undefinedCategoriesEnvelope)
                    prepare = false
                }
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