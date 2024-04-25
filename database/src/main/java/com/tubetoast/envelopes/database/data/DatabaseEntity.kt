package com.tubetoast.envelopes.database.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

abstract class DatabaseEntity {
    abstract val primaryKey: Int
    abstract val foreignKey: Int?
    abstract val valueId: Int
}

@Entity
data class EnvelopeEntity(
    @PrimaryKey(autoGenerate = true) override var primaryKey: Int = 0,
    override val foreignKey: Int? = null,
    override val valueId: Int,
    val name: String,
    val limit: Int,
) : DatabaseEntity()

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = EnvelopeEntity::class,
            parentColumns = arrayOf("primaryKey"),
            childColumns = arrayOf("foreignKey"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) override var primaryKey: Int = 0,
    override val foreignKey: Int,
    override val valueId: Int,
    val name: String,
    val limit: Int?
) : DatabaseEntity()

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = arrayOf("primaryKey"),
            childColumns = arrayOf("foreignKey"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class SpendingEntity(
    @PrimaryKey(autoGenerate = true) override var primaryKey: Int = 0,
    override val foreignKey: Int,
    override val valueId: Int,
    val amount: Int,
    val date: String,
    val comment: String?
) : DatabaseEntity()
