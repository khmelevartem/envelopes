package com.tubetoast.envelopes.ui.presentation.ui.views

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CardItem(
    color: Color,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Surface(
        color = color,
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .shadow(4.dp, shape = RoundedCornerShape(8.dp)),
        shape = RoundedCornerShape(8.dp),
        content = content
    )
}
