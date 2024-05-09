package com.tubetoast.envelopes.android.presentation.ui.views

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun PlusView(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Surface(color = MaterialTheme.colors.secondary, modifier = modifier) {
        IconButton(onClick = onClick) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "add new envelope")
        }
    }
}
