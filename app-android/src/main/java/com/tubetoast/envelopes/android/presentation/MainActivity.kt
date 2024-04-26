package com.tubetoast.envelopes.android.presentation

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.tubetoast.envelopes.android.presentation.ui.EnvelopesApp
import com.tubetoast.envelopes.android.presentation.ui.screens.ChooseEnvelopeViewModel
import com.tubetoast.envelopes.android.presentation.ui.screens.EditCategoryViewModel
import com.tubetoast.envelopes.android.presentation.ui.screens.EditEnvelopeViewModel
import com.tubetoast.envelopes.android.presentation.ui.screens.EnvelopesListViewModel
import com.tubetoast.envelopes.android.presentation.ui.screens.SettingsViewModel
import com.tubetoast.envelopes.android.settings.Setting
import com.tubetoast.envelopes.android.settings.SettingsRepository
import com.tubetoast.envelopes.common.di.api
import com.tubetoast.envelopes.common.domain.models.Date
import com.tubetoast.envelopes.monefy.di.MonefyParserApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val envelopesListViewModel: EnvelopesListViewModel by viewModel()
    private val editEnvelopeViewModel: EditEnvelopeViewModel by viewModel()
    private val editCategoryViewModel: EditCategoryViewModel by viewModel()
    private val chooseEnvelopeViewModel: ChooseEnvelopeViewModel by viewModel()
    private val settingsViewModel: SettingsViewModel by viewModel()
    private val settingsRepository: SettingsRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        readIntent(intent)
        setContent {
            EnvelopesApp(
                envelopesListViewModel,
                editEnvelopeViewModel,
                editCategoryViewModel,
                chooseEnvelopeViewModel,
                settingsViewModel
            )
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        readIntent(intent)
    }

    private fun readIntent(intent: Intent?) = intent?.run {
        val uri: Uri = data ?: if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            extras?.getParcelable(Intent.EXTRA_STREAM, Uri::class.java)
        } else {
            @Suppress("DEPRECATION") extras?.get(Intent.EXTRA_STREAM) as Uri?
        } ?: return@run
                val onlyCurrentMonth = settingsRepository.getSetting(Setting.Key.CURRENT_MONTH).checked

        lifecycleScope.launch(Dispatchers.IO) {
            contentResolver.openInputStream(uri)?.use { inputStream ->
                val startFrom = if (onlyCurrentMonth) Date.currentMonth() else null
                api<MonefyParserApi>().monefyInteractor.import(inputStream, startFrom)
            }
        }
    }
}
