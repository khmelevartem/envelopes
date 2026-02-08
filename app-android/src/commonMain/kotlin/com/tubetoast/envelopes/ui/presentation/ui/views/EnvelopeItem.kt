package com.tubetoast.envelopes.ui.presentation.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType.Companion.Sp
import androidx.compose.ui.unit.dp
import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.snapshots.EnvelopeSnapshot
import com.tubetoast.envelopes.common.utils.formatToReadableNumber
import com.tubetoast.envelopes.ui.presentation.ui.screens.ItemModel
import com.tubetoast.envelopes.ui.presentation.ui.theme.darken

@Composable
fun EnvelopeView(
    itemModel: ItemModel<EnvelopeSnapshot>,
    byYear: Boolean,
    onEditClick: (Envelope) -> Unit,
    onDeleteClick: (Envelope) -> Unit,
    onAddClick: (Envelope) -> Unit,
    onCategoryClick: (Category, Envelope) -> Unit,
    modifier: Modifier = Modifier
) = CardItem(
    color = itemModel.color,
    modifier = modifier.clickable { onEditClick(itemModel.data.envelope) }
) {
    val percentage = itemModel.data.run { if (byYear) yearPercentage else percentage }
    val darkColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
    HorizontalProgressBar(percentage, darkColor)
    Percentage(percentage, darkColor)
    Column(
        modifier = Modifier.padding(top = 6.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Info(itemModel, byYear, darkColor)
            IconButton(onClick = { onDeleteClick(itemModel.data.envelope) }) {
                Icon(Icons.Rounded.Delete, contentDescription = "delete envelope")
            }
        }
        Categories(modifier, itemModel, onCategoryClick, onAddClick)
    }
}

@Composable
private fun Categories(
    modifier: Modifier,
    itemModel: ItemModel<EnvelopeSnapshot>,
    onCategoryClick: (Category, Envelope) -> Unit,
    onAddClick: (Envelope) -> Unit
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
            ).border(
                width = 1.dp,
                color = itemModel.color.darken(),
                shape = RoundedCornerShape(4.dp)
            )
        items(itemModel.data.categories.toList()) {
            CategoryItem(
                snapshot = it,
                modifier = categoriesModifier
            ) {
                onCategoryClick(
                    it.category,
                    itemModel.data.envelope
                )
            }
        }
        item {
            IconButton(
                modifier = categoriesModifier.size(44.dp),
                onClick = { onAddClick(itemModel.data.envelope) }
            ) {
                Icon(Icons.Rounded.Add, contentDescription = "add category")
            }
        }
    }
}

@Composable
private fun Info(
    itemModel: ItemModel<EnvelopeSnapshot>,
    byYear: Boolean,
    darkColor: Color
) {
    itemModel.data.run {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = envelope.name,
                fontSize = TextUnit(value = 24f, type = Sp),
                fontWeight = FontWeight.SemiBold,
                maxLines = 1
            )
            val limit = envelope.run { if (byYear) yearLimit else limit }.units.formatToReadableNumber()
            Text(
                text = "${sum.units.formatToReadableNumber()} / $limit",
                fontSize = TextUnit(value = 20f, type = Sp),
                color = darkColor,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun HorizontalProgressBar(
    percentage: Float,
    darkColor: Color
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth(fraction = percentage)
                .background(
                    color = darkColor,
                    shape = RoundedCornerShape(bottomEnd = 4.dp, topEnd = 4.dp)
                ).height(6.dp)
        )
    }
}
