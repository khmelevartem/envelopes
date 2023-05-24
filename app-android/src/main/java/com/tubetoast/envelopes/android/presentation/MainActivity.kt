package com.tubetoast.envelopes.android.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.tubetoast.envelopes.android.presentation.ui.EnvelopesApp
import com.tubetoast.envelopes.common.di.DomainApi
import com.tubetoast.envelopes.common.di.api
import com.tubetoast.envelopes.common.domain.EnvelopesInteractor

class MainActivity : ComponentActivity() {

    private val domainApi: DomainApi by lazy { api() }
    private val interactor: EnvelopesInteractor by lazy { domainApi.envelopesInteractor }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EnvelopesApp(interactor)
        }
    }
}
