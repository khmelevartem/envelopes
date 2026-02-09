package com.tubetoast.envelopes.ui.presentation.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tubetoast.envelopes.ui.presentation.ui.theme.darken

@Composable
fun HorizontalInfoBar(
    currentValue: String,
    maxValue: String,
    percentage: Float,
    color: Color,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary,
    cornerRadius: Dp = 16.dp,
    shape: Shape = RoundedCornerShape(cornerRadius),
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = color,
                shape = shape
            )
    ) {
        val darkerColor = remember { color.darken() }
        Box(
            modifier = Modifier
                .widthIn(min = cornerRadius * 2)
                .fillMaxWidth(fraction = percentage)
                .fillMaxHeight()
                .background(
                    color = darkerColor,
                    shape = shape
                ).align(Alignment.CenterStart)
        ) {
            Text(
                text = "",
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(vertical = 4.dp, horizontal = 16.dp)
            )
        }
        Text(
            text = currentValue,
            color = contentColor,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(vertical = 4.dp, horizontal = 16.dp),
            maxLines = 1
        )

        Text(
            text = maxValue,
            color = contentColor,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(vertical = 4.dp, horizontal = 16.dp)
        )
    }
}
