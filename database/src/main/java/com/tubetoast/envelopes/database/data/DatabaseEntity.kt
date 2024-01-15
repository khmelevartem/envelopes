package com.tubetoast.envelopes.database.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

abstract class DatabaseEntity {
    abstract val primaryKey: Int
    abstract val foreignKey: Int
}

@Entity
data class EnvelopeEntity(
    @PrimaryKey override val primaryKey: Int,
    override val foreignKey: Int = -1,
    val name: String,
    val limit: Int
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
    @PrimaryKey override val primaryKey: Int,
    override val foreignKey: Int,
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
    @PrimaryKey override val primaryKey: Int,
    override val foreignKey: Int,
    val amount: Int,
    val date: String,
    val comment: String?
) : DatabaseEntity()
