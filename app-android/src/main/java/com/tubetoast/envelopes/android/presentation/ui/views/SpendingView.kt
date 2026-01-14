package com.tubetoast.envelopes.android.presentation.ui.views

import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tubetoast.envelopes.common.domain.models.Spending

@Composable
fun SpendingView(spending: Spending) {
    Surface(color = Color.White, modifier = Modifier.shadow(5.dp)) {
        Text(text = spending.amount.toString())
    }
}
