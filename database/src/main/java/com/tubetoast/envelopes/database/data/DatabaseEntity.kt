package com.tubetoast.envelopes.database.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

abstract class DatabaseEntity {
    abstract val primaryKey: String
    abstract val foreignKey: String
}

@Entity
data class EnvelopeEntity(
    @PrimaryKey override val primaryKey: String,
    @ColumnInfo(index = true) override val foreignKey: String = "",
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
    @PrimaryKey override val primaryKey: String,
    @ColumnInfo(index = true) override val foreignKey: String,
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
    @PrimaryKey override val primaryKey: String,
    @ColumnInfo(index = true) override val foreignKey: String,
    val amount: Int,
    val date: String,
    val comment: String?
) : DatabaseEntity()
