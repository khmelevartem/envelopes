package com.tubetoast.envelopes.database.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.tubetoast.envelopes.common.domain.models.Root

abstract class DatabaseEntity {
    abstract val primaryKey: Int
    abstract val foreignKey: Int
    abstract val valueId: Int
}

@Entity(
    indices = [
        Index("valueId", orders = emptyArray(), unique = true)
    ]
)
data class GoalEntity(
    @PrimaryKey(autoGenerate = true) override var primaryKey: Int = 0,
    override val foreignKey: Int = Root.id.code,
    override val valueId: Int,
    val name: String,
    val target: Long,
    val startDate: String,
    val finishDate: String
) : DatabaseEntity()

@Entity(
    indices = [
        Index("valueId", orders = emptyArray(), unique = true)
    ]
)
data class EnvelopeEntity(
    @PrimaryKey(autoGenerate = true) override var primaryKey: Int = 0,
    override val foreignKey: Int = Root.id.code,
    override val valueId: Int,
    val name: String,
    val limit: Long
) : DatabaseEntity()

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = EnvelopeEntity::class,
            parentColumns = arrayOf("primaryKey"),
            childColumns = arrayOf("foreignKey"),
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("valueId", orders = emptyArray(), unique = true)
    ]
)
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) override var primaryKey: Int = 0,
    override val foreignKey: Int,
    override val valueId: Int,
    val name: String,
    val limit: Long?
) : DatabaseEntity()

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = arrayOf("primaryKey"),
            childColumns = arrayOf("foreignKey"),
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("valueId", orders = emptyArray(), unique = true)
    ]
)
data class SpendingEntity(
    @PrimaryKey(autoGenerate = true) override var primaryKey: Int = 0,
    override val foreignKey: Int,
    override val valueId: Int,
    val amount: Long,
    val date: String,
    val comment: String?
) : DatabaseEntity()
