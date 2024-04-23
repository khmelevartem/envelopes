package com.tubetoast.envelopes.android.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.tubetoast.envelopes.android.presentation.ui.EnvelopesApp
import com.tubetoast.envelopes.android.presentation.ui.screens.ChooseEnvelopeViewModel
import com.tubetoast.envelopes.android.presentation.ui.screens.EditCategoryViewModel
import com.tubetoast.envelopes.android.presentation.ui.screens.EditEnvelopeViewModel
import com.tubetoast.envelopes.android.presentation.ui.screens.EnvelopesListViewModel
import com.tubetoast.envelopes.common.di.api
import com.tubetoast.envelopes.monefy.di.MonefyParserApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val envelopesListViewModel: EnvelopesListViewModel by viewModel()
    private val editEnvelopeViewModel: EditEnvelopeViewModel by viewModel()
    private val editCategoryViewModel: EditCategoryViewModel by viewModel()
    private val chooseEnvelopeViewModel: ChooseEnvelopeViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        importFile(intent.data)

        setContent {
            EnvelopesApp(
                envelopesListViewModel,
                editEnvelopeViewModel,
                editCategoryViewModel,
                chooseEnvelopeViewModel
            )
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        importFile(intent?.data)
    }

    private fun importFile(uri: Uri?) = uri?.let {
        lifecycleScope.launch(Dispatchers.IO) {
            contentResolver.openInputStream(it)?.use { inputStream ->
                api<MonefyParserApi>().monefyInteractor.import(inputStream)
            }
        }
    }
}
