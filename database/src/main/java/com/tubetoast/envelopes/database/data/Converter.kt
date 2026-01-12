package com.tubetoast.envelopes.database.data

import com.tubetoast.envelopes.common.domain.models.Amount
import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.DateConverter.fromDate
import com.tubetoast.envelopes.common.domain.models.DateConverter.toDate
import com.tubetoast.envelopes.common.domain.models.DateConverter.toDateOrNull
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.Goal
import com.tubetoast.envelopes.common.domain.models.ImmutableModel
import com.tubetoast.envelopes.common.domain.models.Spending

interface Converter<M : ImmutableModel<M>, DE : DatabaseEntity> {
    fun toDomainModel(databaseEntity: DE): M
    fun toDatabaseEntity(domainModel: M, foreignKey: Int, primaryKey: Int = 0): DE
}

class EnvelopeConverter : Converter<Envelope, EnvelopeEntity> {
    override fun toDomainModel(databaseEntity: EnvelopeEntity): Envelope = databaseEntity.run {
        Envelope(name = name, limit = Amount(units = limit))
    }

    override fun toDatabaseEntity(domainModel: Envelope, foreignKey: Int, primaryKey: Int) =
        EnvelopeEntity(
            primaryKey = primaryKey,
            valueId = domainModel.id.code,
            foreignKey = foreignKey,
            name = domainModel.name,
            limit = domainModel.limit.units
        )
}

class CategoryConverter : Converter<Category, CategoryEntity> {
    override fun toDomainModel(databaseEntity: CategoryEntity): Category = databaseEntity.run {
        Category(name = name, limit = limit?.let { Amount(units = it) })
    }

    override fun toDatabaseEntity(domainModel: Category, foreignKey: Int, primaryKey: Int) =
        CategoryEntity(
            primaryKey = primaryKey,
            valueId = domainModel.id.code,
            foreignKey = foreignKey,
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

    override fun toDatabaseEntity(domainModel: Spending, foreignKey: Int, primaryKey: Int) =
        SpendingEntity(
            primaryKey = primaryKey,
            valueId = domainModel.id.code,
            foreignKey = foreignKey,
            amount = domainModel.amount.units,
            date = domainModel.date.fromDate(),
            comment = domainModel.comment
        )
}

class GoalConverter : Converter<Goal, GoalEntity> {
    override fun toDomainModel(databaseEntity: GoalEntity): Goal = databaseEntity.run {
        Goal(
            name = name,
            target = Amount(units = target),
            start = startDate.toDateOrNull(),
            finish = finishDate.toDateOrNull()
        )
    }

    override fun toDatabaseEntity(domainModel: Goal, foreignKey: Int, primaryKey: Int) =
        GoalEntity(
            primaryKey = primaryKey,
            valueId = domainModel.id.code,
            foreignKey = foreignKey,
            name = domainModel.name,
            target = domainModel.target.units,
            startDate = domainModel.start?.fromDate().orEmpty(),
            finishDate = domainModel.finish?.fromDate().orEmpty()
        )
}
