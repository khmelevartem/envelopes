package com.tubetoast.envelopes.android.presentation.ui.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType.Companion.Sp
import androidx.compose.ui.unit.dp
import com.tubetoast.envelopes.android.presentation.ui.screens.ItemModel
import com.tubetoast.envelopes.android.presentation.utils.formatToReadableNumber
import com.tubetoast.envelopes.common.domain.models.Date
import com.tubetoast.envelopes.common.domain.models.DateRange
import com.tubetoast.envelopes.common.domain.models.Goal
import com.tubetoast.envelopes.common.domain.models.inMonths
import com.tubetoast.envelopes.common.domain.savePerMonth
import com.tubetoast.envelopes.common.domain.snapshots.GoalSnapshot
import com.tubetoast.envelopes.common.domain.snapshots.percentage
import com.tubetoast.envelopes.common.domain.snapshots.sum

@Composable
fun GoalItem(
    itemModel: ItemModel<GoalSnapshot>,
    onEditClick: (Goal) -> Unit,
    onDeleteClick: (Goal) -> Unit,
    modifier: Modifier = Modifier
) = CardItem(
    color = itemModel.color,
    modifier = modifier.clickable { onEditClick(itemModel.data.goal) }
) {
    val snapshot = itemModel.data
    val goal = snapshot.goal
    val percentage = snapshot.percentage()
    val darkColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
    Column(
        modifier = Modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp, start = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = goal.name,
                fontWeight = FontWeight.SemiBold,
                fontSize = TextUnit(value = 24f, type = Sp),
                maxLines = 1
            )
            snapshot.savePerMonth()?.let {
                Text(
                    text = "${it.formatToReadableNumber()} / month",
                    fontSize = TextUnit(value = 20f, type = Sp),
                    fontWeight = FontWeight.Bold,
                    color = darkColor
                )
            }
        }
        HorizontalInfoBar(
            currentValue = snapshot.sum().formatToReadableNumber(),
            maxValue = goal.target.formatToReadableNumber(),
            percentage = percentage,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
            contentColor = MaterialTheme.colorScheme.onPrimary
        )

        val start = goal.start
        val finish = goal.finish
        if (start != null && finish != null) {
            val today = Date.today()
            val spent = if (today > start) start.rangeTo(today) else DateRange.EMPTY
            val spentMonths = spent.inMonths()
            val wholePeriod = (start..finish).inMonths()
            HorizontalInfoBar(
                currentValue = "$spentMonths months",
                maxValue = "until $finish",
                percentage = spentMonths / wholePeriod.toFloat(),
                color = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            )
        }
    }
}
