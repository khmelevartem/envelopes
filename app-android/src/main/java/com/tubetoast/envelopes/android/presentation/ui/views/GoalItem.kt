package com.tubetoast.envelopes.android.presentation.ui.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tubetoast.envelopes.android.presentation.ui.screens.ItemModel
import com.tubetoast.envelopes.android.presentation.utils.formatToReadableNumber
import com.tubetoast.envelopes.common.domain.models.Goal
import com.tubetoast.envelopes.common.domain.savePerMonth
import com.tubetoast.envelopes.common.domain.snapshots.GoalSnapshot
import com.tubetoast.envelopes.common.domain.snapshots.percentage

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
        modifier = Modifier.padding(8.dp)
    ) {
        Text(
            text = goal.name,
            style = MaterialTheme.typography.titleLarge,
            maxLines = 1
        )
        HorizontalProgressBar(percentage, darkColor)
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            goal.finish?.let {
                Text(
                    text = it.toString(),
                    modifier = Modifier.align(Alignment.BottomStart)
                )
            }

            snapshot.savePerMonth()?.let {
                Text(
                    text = it.units.formatToReadableNumber(),
                    modifier = Modifier.align(Alignment.BottomEnd)
                )
            }
        }
    }
}
