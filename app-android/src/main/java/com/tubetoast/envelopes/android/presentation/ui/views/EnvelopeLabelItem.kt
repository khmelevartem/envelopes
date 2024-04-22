package com.tubetoast.envelopes.android.presentation.ui.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.unit.dp
import com.tubetoast.envelopes.android.presentation.ui.screens.ItemModel
import com.tubetoast.envelopes.android.presentation.ui.theme.EColor
import com.tubetoast.envelopes.common.domain.models.Envelope

@Composable
fun EnvelopeLabelView(
    itemModel: ItemModel<Envelope>,
    chosen: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) = Surface(
    color = itemModel.color,
    modifier = modifier
        .clickable(onClick = onClick)
        .fillMaxWidth(),
    border = BorderStroke(if (chosen) 4.dp else 0.dp, EColor.Gray)
) {
    Text(
        text = itemModel.data.name,
        modifier = Modifier.padding(8.dp),
        style = if (chosen) TextStyle(fontWeight = SemiBold) else TextStyle.Default
    )
}
