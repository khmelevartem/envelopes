package com.tubetoast.envelopes.android.presentation.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType.Companion.Sp
import androidx.compose.ui.unit.dp
import com.tubetoast.envelopes.android.presentation.ui.screens.ItemModel
import com.tubetoast.envelopes.android.presentation.ui.theme.darken
import com.tubetoast.envelopes.common.domain.models.Goal
import com.tubetoast.envelopes.common.domain.models.sum
import com.tubetoast.envelopes.common.domain.snapshots.GoalSnapshot
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
    val percentage = itemModel.data.categories.map { it.sum() }.sum() / itemModel.data.goal.target
    val darkColor = MaterialTheme.colors.onSurface.copy(alpha = 0.3f)
    HorizontalProgressBar(percentage, darkColor)
    Percentage(percentage, darkColor)
    Column(
        modifier = Modifier.padding(top = 6.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Info(itemModel, darkColor)
            IconButton(onClick = { onDeleteClick(itemModel.data.goal) }) {
                Icon(Icons.Rounded.Delete, contentDescription = "delete goal")
            }
        }
        Categories(modifier, itemModel)
    }
}

@Composable
private fun Categories(
    modifier: Modifier,
    itemModel: ItemModel<GoalSnapshot>
) {
    LazyRow(
        modifier = modifier.padding(4.dp),
        contentPadding = PaddingValues(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        val categoriesModifier = Modifier
            .height(44.dp)
            .background(
                color = itemModel.color.copy(alpha = 0.8f),
                shape = RoundedCornerShape(4.dp)
            )
            .border(
                width = 1.dp,
                color = itemModel.color.darken(),
                shape = RoundedCornerShape(4.dp)
            )
        items(itemModel.data.categories.map { it.category }) {
            CategoryItem(
                name = it.name,
                modifier = categoriesModifier
            ) {}
        }
    }
}

@Composable
private fun Info(
    itemModel: ItemModel<GoalSnapshot>,
    darkColor: Color
) {
    itemModel.data.run {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = goal.name,
                fontSize = TextUnit(value = 24f, type = Sp),
                fontWeight = FontWeight.SemiBold,
                maxLines = 1
            )
            Text(
                text = goal.finish?.let { "Until $it" } ?: "",
                fontSize = TextUnit(value = 20f, type = Sp),
                color = darkColor,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
