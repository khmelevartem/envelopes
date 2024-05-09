package com.tubetoast.envelopes.android.presentation.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.unit.dp
import com.tubetoast.envelopes.android.presentation.ui.screens.ItemModel
import com.tubetoast.envelopes.android.presentation.ui.theme.darken
import com.tubetoast.envelopes.common.domain.models.Envelope

@Composable
fun EnvelopeLabelView(
    itemModel: ItemModel<Envelope>,
    isChosen: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) = Row(
    modifier = modifier
        .clickable(onClick = onClick)
        .background(color = itemModel.color)
        .fillMaxWidth()
        .height(48.dp)
) {
    if (isChosen) {
        Box(
            modifier = modifier
                .background(itemModel.color.darken())
                .width(4.dp)
                .fillMaxHeight()
        )
    }

    Spacer(modifier = Modifier.weight(1f))
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxHeight()) {
        Text(
            text = itemModel.data.name,
            modifier = Modifier.padding(end = 18.dp),
            style = if (isChosen) TextStyle(fontWeight = SemiBold) else TextStyle.Default
        )
    }
}
