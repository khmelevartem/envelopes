package com.tubetoast.envelopes.android.presentation

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.tubetoast.envelopes.android.presentation.ui.EnvelopesApp
import com.tubetoast.envelopes.android.presentation.ui.theme.EnvelopesTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModel()

    init {
        addOnNewIntentListener(::readIntent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        readIntent(intent)
        setContent {
            EnvelopesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    EnvelopesApp()
                }
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun readIntent(intent: Intent?) = intent?.run {
        val uri: Uri? = data ?: if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            extras?.getParcelable(Intent.EXTRA_STREAM, Uri::class.java)
        } else {
            extras?.get(Intent.EXTRA_STREAM) as Uri?
        }

        uri?.let { contentResolver.openInputStream(it) }
            ?.let { mainViewModel.import(it) }
    }
}
