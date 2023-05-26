package com.tubetoast.envelopes.android.presentation.ui.views

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import com.tubetoast.envelopes.android.presentation.ui.theme.EColor

@Composable
fun PlusView(onClick: () -> Unit) {
    Surface(color = EColor.GrayLight) {
        IconButton(onClick = onClick) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "add new envelope")
        }
    }
}
