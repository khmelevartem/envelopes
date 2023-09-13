package com.tubetoast.envelopes.android.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.tubetoast.envelopes.android.presentation.ui.EnvelopesApp
import com.tubetoast.envelopes.android.presentation.ui.screens.EditCategoryViewModel
import com.tubetoast.envelopes.android.presentation.ui.screens.EditEnvelopeViewModel
import com.tubetoast.envelopes.android.presentation.ui.screens.EnvelopesListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val envelopesListViewModel: EnvelopesListViewModel by viewModel()
    private val editEnvelopeViewModel: EditEnvelopeViewModel by viewModel()
    private val editCategoryViewModel: EditCategoryViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EnvelopesApp(envelopesListViewModel, editEnvelopeViewModel, editCategoryViewModel)
        }
    }
}
