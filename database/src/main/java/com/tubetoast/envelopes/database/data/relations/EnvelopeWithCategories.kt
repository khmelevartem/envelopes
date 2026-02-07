package com.tubetoast.envelopes.database.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.tubetoast.envelopes.database.data.CategoryEntity
import com.tubetoast.envelopes.database.data.EnvelopeEntity
import com.tubetoast.envelopes.database.data.SpendingEntity

data class EnvelopeWithCategories(
    @Embedded val envelope: EnvelopeEntity,
    @Relation(
        entity = CategoryEntity::class,
        parentColumn = "primaryKey",
        entityColumn = "foreignKey"
    )
    val categories: List<CategoryWithSpending>
)

data class CategoryWithSpending(
    @Embedded val category: CategoryEntity,
    @Relation(
        entity = SpendingEntity::class,
        parentColumn = "primaryKey",
        entityColumn = "foreignKey"
    )
    val spending: List<SpendingEntity>
)
