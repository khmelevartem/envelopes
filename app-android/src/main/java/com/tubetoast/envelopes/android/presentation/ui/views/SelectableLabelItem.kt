package com.tubetoast.envelopes.android.presentation.ui.views

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.unit.dp
import com.tubetoast.envelopes.android.presentation.ui.theme.darken

@Composable
fun SelectableLabelItem(
    label: String,
    color: Color,
    modifier: Modifier = Modifier,
    isChosen: Boolean = false,
    onClick: () -> Unit
) = Row(
    modifier = modifier
        .clickable(onClick = onClick)
        .fillMaxWidth()
        .height(48.dp)
) {
    AnimatedVisibility(visible = isChosen) {
        Box(
            modifier = modifier
                .background(color.darken())
                .width(8.dp)
                .fillMaxHeight()
        )
    }
    Spacer(modifier = Modifier.weight(1f))
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxHeight()) {
        Text(
            text = label,
            modifier = Modifier.padding(end = 18.dp),
            style = if (isChosen) TextStyle(fontWeight = SemiBold) else TextStyle.Default
        )
    }
}
