package com.tubetoast.envelopes.android.presentation.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.tubetoast.envelopes.android.presentation.ui.theme.EnvelopesTheme
import com.tubetoast.envelopes.android.presentation.ui.views.EnvelopeView
import com.tubetoast.envelopes.common.domain.EnvelopesInteractor
import com.tubetoast.envelopes.common.domain.SampleEnvelopesInteractor

@Composable
fun EnvelopesApp(interactor: EnvelopesInteractor) {
    EnvelopesTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            LazyRow {
                items(interactor.envelopeSnapshot.toList()) { snapshot ->
                    EnvelopeView(snapshot)
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    EnvelopesApp(SampleEnvelopesInteractor())
}