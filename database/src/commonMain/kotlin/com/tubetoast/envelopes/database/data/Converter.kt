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

    fun toDatabaseEntity(
        domainModel: M,
        foreignKey: Int,
        primaryKey: Int = 0
    ): DE
}

abstract class CheckingConverter<M : ImmutableModel<M>, DE : DatabaseEntity> : Converter<M, DE> {
    final override fun toDomainModel(databaseEntity: DE): M =
        databaseEntity.run {
            withCheckValueId {
                toDomainModelUnsafe()
            }
        }

    abstract fun DE.toDomainModelUnsafe(): M
}

class EnvelopeConverter : CheckingConverter<Envelope, EnvelopeEntity>() {
    override fun EnvelopeEntity.toDomainModelUnsafe(): Envelope = Envelope(name = name, limit = Amount(units = limit))

    override fun toDatabaseEntity(
        domainModel: Envelope,
        foreignKey: Int,
        primaryKey: Int
    ) = EnvelopeEntity(
        primaryKey = primaryKey,
        valueId = domainModel.id.code,
        foreignKey = foreignKey,
        name = domainModel.name,
        limit = domainModel.limit.units
    )
}

class CategoryConverter : Converter<Category, CategoryEntity> {
    override fun toDomainModel(databaseEntity: CategoryEntity): Category =
        databaseEntity.run {
            Category(name = name, limit = limit?.let { Amount(units = it) })
        }

    override fun toDatabaseEntity(
        domainModel: Category,
        foreignKey: Int,
        primaryKey: Int
    ) = CategoryEntity(
        primaryKey = primaryKey,
        valueId = domainModel.id.code,
        foreignKey = foreignKey,
        name = domainModel.name,
        limit = domainModel.limit?.units
    )
}

class SpendingConverter : CheckingConverter<Spending, SpendingEntity>() {
    override fun SpendingEntity.toDomainModelUnsafe(): Spending =
        Spending(
            amount = Amount(units = amount),
            date = date.toDate(),
            comment = comment
        )

    override fun toDatabaseEntity(
        domainModel: Spending,
        foreignKey: Int,
        primaryKey: Int
    ) = SpendingEntity(
        primaryKey = primaryKey,
        valueId = domainModel.id.code,
        foreignKey = foreignKey,
        amount = domainModel.amount.units,
        date = domainModel.date.fromDate(),
        comment = domainModel.comment
    )
}

class GoalConverter : CheckingConverter<Goal, GoalEntity>() {
    override fun GoalEntity.toDomainModelUnsafe(): Goal =
        Goal(
            name = name,
            target = Amount(units = target),
            start = startDate.toDateOrNull(),
            finish = finishDate.toDateOrNull()
        )

    override fun toDatabaseEntity(
        domainModel: Goal,
        foreignKey: Int,
        primaryKey: Int
    ) = GoalEntity(
        primaryKey = primaryKey,
        valueId = domainModel.id.code,
        foreignKey = foreignKey,
        name = domainModel.name,
        target = domainModel.target.units,
        startDate = domainModel.start?.fromDate().orEmpty(),
        finishDate = domainModel.finish?.fromDate().orEmpty()
    )
}

fun <T : ImmutableModel<T>> DatabaseEntity.withCheckValueId(model: () -> T): T =
    model().also {
        check(it.id.code == valueId) {
            "Inconsistent valueId: result is ${it.id.code} but is $valueId in db"
        }
    }
