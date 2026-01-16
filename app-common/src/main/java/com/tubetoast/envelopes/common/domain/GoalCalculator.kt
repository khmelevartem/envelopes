package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.domain.models.Amount
import com.tubetoast.envelopes.common.domain.models.Date
import com.tubetoast.envelopes.common.domain.models.inMonths
import com.tubetoast.envelopes.common.domain.snapshots.GoalSnapshot
import com.tubetoast.envelopes.common.domain.snapshots.sum

fun GoalSnapshot.savePerMonth(today: Date = Date.today()): Amount? {
    val finish = goal.finish ?: return null
    if (finish < today) return null

    val months = (today..finish).inMonths()
    if (months == 0) return null

    val target = goal.target
    val sum = sum()
    if (target < sum) return null

    return (target - sum) / months
}
