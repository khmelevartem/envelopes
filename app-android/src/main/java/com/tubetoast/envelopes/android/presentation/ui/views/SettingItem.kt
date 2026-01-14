package com.tubetoast.envelopes.android.presentation.ui.views

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CheckboxSettingItem(
    text: String,
    default: Boolean,
    modifier: Modifier = Modifier,
    action: (Boolean) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, color = Color.DarkGray)
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = text)
        Checkbox(checked = default, onCheckedChange = action)
    }
}
