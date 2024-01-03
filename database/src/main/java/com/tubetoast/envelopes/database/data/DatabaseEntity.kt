package com.tubetoast.envelopes.database.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.tubetoast.envelopes.common.domain.models.Amount
import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Date
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.Id
import com.tubetoast.envelopes.common.domain.models.ImmutableModel
import com.tubetoast.envelopes.common.domain.models.Spending

abstract class DatabaseEntity<out T : ImmutableModel<out T>>() {
    abstract val primaryKey: Int
    abstract val foreignKey: Int
    abstract fun toDomainModel(): T
}

@Entity
data class EnvelopeEntity(
    @PrimaryKey override val primaryKey: Int,
    override val foreignKey: Int = -1,
    val name: String,
    val limit: Int
) : DatabaseEntity<Envelope>() {
    constructor(envelope: Envelope) : this(
        primaryKey = envelope.id.code,
        name = envelope.name,
        limit = envelope.limit.units
    )

    override fun toDomainModel() = Envelope(name = name, limit = Amount(units = limit))
}


@Entity(
    foreignKeys = [ForeignKey(
        entity = EnvelopeEntity::class,
        parentColumns = arrayOf("primaryKey"),
        childColumns = arrayOf("foreignKey"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class CategoryEntity(
    @PrimaryKey override val primaryKey: Int,
    override val foreignKey: Int,
    val name: String,
    val limit: Int?
) : DatabaseEntity<Category>() {

    constructor(category: Category, envelopeKey: Id<Envelope>) : this(
        primaryKey = category.id.code,
        foreignKey = envelopeKey.code,
        name = category.name,
        limit = category.limit?.units
    )

    override fun toDomainModel(): Category =
        Category(name = name, limit = limit?.let { Amount(units = it) })
}


@Entity(
    foreignKeys = [ForeignKey(
        entity = CategoryEntity::class,
        parentColumns = arrayOf("primaryKey"),
        childColumns = arrayOf("foreignKey"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class SpendingEntity(
    @PrimaryKey override val primaryKey: Int,
    override val foreignKey: Int,
    val amount: Int,
    val date: String,
    val comment: String?
) : DatabaseEntity<Spending>() {

    constructor(spending: Spending, categoryKey: Id<Category>) : this(
        primaryKey = spending.id.code,
        foreignKey = categoryKey.code,
        amount = spending.amount.units,
        date = spending.date.fromDate(),
        comment = spending.comment
    )

    override fun toDomainModel(): Spending = Spending(
        amount = Amount(units = amount), date = date.toDate(), comment = comment
    )

    companion object DateConverter {
        private const val DELIMITER = "/"
        fun String.toDate() = split(DELIMITER).map { it.toInt() }
            .let { (d, m, y) -> Date(day = d, month = m, year = y) }

        fun Date.fromDate() = "$day$DELIMITER$month$DELIMITER$year"
    }
}
