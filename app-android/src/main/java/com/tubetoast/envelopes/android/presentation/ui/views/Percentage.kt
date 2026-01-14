package com.tubetoast.envelopes.android.presentation.ui.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType.Companion.Sp
import androidx.compose.ui.unit.dp

@Composable
fun Percentage(percentage: Float, darkColor: Color) {
    Box(
        contentAlignment = Alignment.BottomEnd,
        modifier = Modifier.offset(x = 20.dp, y = 44.dp)
    ) {
        Text(
            text = "${(percentage * 100).toInt()}%",
            fontWeight = FontWeight.Bold,
            fontSize = TextUnit(value = 96f, type = Sp),
            color = darkColor,
            maxLines = 1
        )
    }
}
