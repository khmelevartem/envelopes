package com.tubetoast.envelopes.android.presentation.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun HorizontalProgressBar(percentage: Float, darkColor: Color) {
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
