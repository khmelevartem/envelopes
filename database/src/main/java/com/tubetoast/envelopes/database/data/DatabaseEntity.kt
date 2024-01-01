package com.tubetoast.envelopes.database.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.ImmutableModel
import com.tubetoast.envelopes.common.domain.models.Spending

abstract class DatabaseEntity<T : ImmutableModel<T>> {
    abstract val primaryKey: Int
    abstract val foreignKey: Int
    abstract val model: T
}

@Entity
data class EnvelopeEntity(
    @PrimaryKey override val primaryKey: Int,
    override val foreignKey: Int = -1,
    override val model: Envelope
) : DatabaseEntity<Envelope>()


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
    override val model: Category
) : DatabaseEntity<Category>()


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
    override val model: Spending
) : DatabaseEntity<Spending>()
