package com.tubetoast.envelopes.android.presentation.ui.views

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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
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
import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.snapshots.EnvelopeSnapshot

@Composable
fun EnvelopeView(
    itemModel: ItemModel<EnvelopeSnapshot>,
    byYear: Boolean,
    onEditClick: (Envelope) -> Unit,
    onDeleteClick: (Envelope) -> Unit,
    onAddClick: (Envelope) -> Unit,
    onCategoryClick: (Category, Envelope) -> Unit,
    modifier: Modifier = Modifier
) = Surface(
    color = itemModel.color,
    modifier = modifier.clickable { onEditClick(itemModel.data.envelope) }) {
    val percentage = itemModel.data.run { if (byYear) yearPercentage else percentage }
    val darkColor = MaterialTheme.colors.onSurface.copy(alpha = 0.3f)
    ProgressBar(percentage, darkColor)
    Percentage(percentage, darkColor)
    Column(
        modifier = Modifier.padding(top = 6.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
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
            )
            .border(
                width = 1.dp,
                color = itemModel.color.darken(),
                shape = RoundedCornerShape(4.dp)
            )
        items(itemModel.data.categories.toList()) {
            CategoryView(
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
                onClick = { onAddClick(itemModel.data.envelope) }) {
                Icon(Icons.Rounded.Add, contentDescription = "add category")
            }
        }
    }
}

@Composable
private fun Buttons(
    itemModel: ItemModel<EnvelopeSnapshot>,
    onDeleteClick: (Envelope) -> Unit
) {
    Row(horizontalArrangement = Arrangement.End) {
        IconButton(onClick = { onDeleteClick(itemModel.data.envelope) }) {
            Icon(Icons.Rounded.Delete, contentDescription = "delete envelope")
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
            val limit = envelope.run { if (byYear) yearLimit else limit }.units.formatNumber()
            Text(
                text = "${sum.units.formatNumber()} / $limit",
                fontSize = TextUnit(value = 20f, type = Sp),
                color = darkColor,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
private fun Percentage(percentage: Float, darkColor: Color) {
    Box(
        contentAlignment = Alignment.BottomEnd,
        modifier = Modifier.offset(x = 20.dp, y = 44.dp)
    ) {
        Text(
            text = "${(percentage * 100).toInt()}%",
            fontWeight = FontWeight.Bold,
            fontSize = TextUnit(value = 96f, type = Sp),
            color = darkColor,
            maxLines = 1,
        )
    }
}

@Composable
private fun ProgressBar(percentage: Float, darkColor: Color) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth(fraction = percentage)
                .background(
                    color = darkColor,
                    shape = RoundedCornerShape(bottomEnd = 4.dp, topEnd = 4.dp)
                )
                .height(6.dp)
        )
    }
}

private fun Int.formatNumber(): String {
    val rev = toString().reversed()
    val strBuilder = StringBuilder()
    for (i in rev.indices) {
        if (i != 0 && i.mod(3) == 0 && rev.length >= i + 1) {
            strBuilder.append(" ")
        }
        strBuilder.append(rev[i])
    }
    return strBuilder.toString().reversed()
}
