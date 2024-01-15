package com.tubetoast.envelopes.database.data

import com.tubetoast.envelopes.common.domain.models.Amount
import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Date
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.ImmutableModel
import com.tubetoast.envelopes.common.domain.models.Spending

interface Converter<M : ImmutableModel<M>, DE : DatabaseEntity> {
    fun toDomainModel(databaseEntity: DE): M
    fun toDatabaseEntity(domainModel: M, parentId: Int): DE
}

class EnvelopeConverter : Converter<Envelope, EnvelopeEntity> {
    override fun toDomainModel(databaseEntity: EnvelopeEntity): Envelope = databaseEntity.run {
        Envelope(name = name, limit = Amount(units = limit))
    }

    override fun toDatabaseEntity(domainModel: Envelope, parentId: Int) =
        EnvelopeEntity(
            primaryKey = domainModel.id.code,
            foreignKey = parentId,
            name = domainModel.name,
            limit = domainModel.limit.units
        )
}

class CategoryConverter : Converter<Category, CategoryEntity> {
    override fun toDomainModel(databaseEntity: CategoryEntity): Category = databaseEntity.run {
        Category(name = name, limit = limit?.let { Amount(units = it) })
    }

    override fun toDatabaseEntity(domainModel: Category, parentId: Int): CategoryEntity =
        CategoryEntity(
            primaryKey = domainModel.id.code,
            foreignKey = parentId,
            name = domainModel.name,
            limit = domainModel.limit?.units
        )
}

class SpendingConverter : Converter<Spending, SpendingEntity> {
    override fun toDomainModel(databaseEntity: SpendingEntity): Spending = databaseEntity.run {
        Spending(
            amount = Amount(units = amount),
            date = date.toDate(),
            comment = comment
        )
    }

    override fun toDatabaseEntity(domainModel: Spending, parentId: Int): SpendingEntity =
        SpendingEntity(
            primaryKey = domainModel.id.code,
            foreignKey = parentId,
            amount = domainModel.amount.units,
            date = domainModel.date.fromDate(),
            comment = domainModel.comment
        )

    companion object DateConverter {
        private const val DELIMITER = "/"
        fun String.toDate() = split(DELIMITER).map { it.toInt() }
            .let { (d, m, y) -> Date(day = d, month = m, year = y) }

        fun Date.fromDate() = "$day$DELIMITER$month$DELIMITER$year"
    }
}
