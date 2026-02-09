package com.tubetoast.envelopes.ui.presentation.navigation

import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.Goal

object AppNavigation {
    val start = envelopesList()

    fun envelopesList() = EnvelopesList.Args

    fun addEnvelope() = EnvelopeDetails.Args()

    fun editEnvelope(envelope: Envelope) = EnvelopeDetails.Args(envelope.id.code)

    fun addCategory(envelope: Envelope) = CategoryDetails.Args(envelopeId = envelope.id.code)

    fun editCategory(
        category: Category,
        envelope: Envelope
    ) = CategoryDetails.Args(envelopeId = envelope.id.code, categoryId = category.id.code)

    fun selectEnvelope(category: Category) = SelectEnvelope.Args(category.id.code)

    fun settings() = Settings.Args

    fun goalsList() = GoalsList.Args

    fun addGoal() = GoalDetails.Args()

    fun editGoal(goal: Goal) = GoalDetails.Args(goal.id.code)
}
