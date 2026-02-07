package com.tubetoast.envelopes.database.data.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.tubetoast.envelopes.database.data.CategoryEntity
import com.tubetoast.envelopes.database.data.CategoryToGoalLinkEntity
import com.tubetoast.envelopes.database.data.GoalEntity

data class GoalWithCategories(
    @Embedded val goal: GoalEntity,
    @Relation(
        entity = CategoryEntity::class,
        parentColumn = "primaryKey",
        entityColumn = "primaryKey",
        associateBy = Junction(
            value = CategoryToGoalLinkEntity::class,
            parentColumn = "goalKey",
            entityColumn = "categoryKey"
        )
    )
    val categories: List<CategoryWithSpending>
)
