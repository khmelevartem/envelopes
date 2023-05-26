package com.tubetoast.envelopes.android.presentation.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.tubetoast.envelopes.android.presentation.ui.screens.EnvelopesListViewModel
import com.tubetoast.envelopes.android.presentation.ui.theme.EnvelopesTheme
import com.tubetoast.envelopes.android.presentation.ui.views.EnvelopeView
import com.tubetoast.envelopes.android.presentation.ui.views.MainListView
import com.tubetoast.envelopes.android.presentation.ui.views.PlusView
import com.tubetoast.envelopes.common.data.CategoriesRepositoryImpl
import com.tubetoast.envelopes.common.data.EnvelopesRepositoryImpl
import com.tubetoast.envelopes.common.data.SpendingRepositoryImpl
import com.tubetoast.envelopes.common.domain.SnapshotsInteractorImpl
import com.tubetoast.envelopes.common.domain.stub.StubEditInteractorImpl

@Composable
fun EnvelopesApp(envelopesListViewModel: EnvelopesListViewModel) {
    val snapshots = envelopesListViewModel.itemModels.collectAsState(initial = emptyList())
    EnvelopesTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            LazyColumn {
                items(snapshots.value) { snapshot ->
                    MainListView {
                        EnvelopeView(snapshot)
                    }
                }
                item {
                    MainListView {
                        PlusView {
                            envelopesListViewModel.addEnvelope()
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val repository = EnvelopesRepositoryImpl()
    EnvelopesApp(
        EnvelopesListViewModel(
            SnapshotsInteractorImpl(
                SpendingRepositoryImpl(),
                CategoriesRepositoryImpl(),
                repository
            ),
            StubEditInteractorImpl(
                repository
            )
        )

    )
}
