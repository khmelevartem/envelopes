package com.tubetoast.envelopes.android.presentation

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tubetoast.envelopes.android.settings.Setting
import com.tubetoast.envelopes.android.settings.SettingsRepository
import com.tubetoast.envelopes.common.domain.models.DateConverter.fromDate
import com.tubetoast.envelopes.common.domain.models.DateConverter.toDate
import com.tubetoast.envelopes.monefy.data.MonefyInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.InputStream

class MainViewModel(
    private val settingsRepository: SettingsRepository,
    private val monefyInteractor: MonefyInteractor,
    private val sharedPrefs: SharedPreferences,
) : ViewModel() {

    constructor(
        settingsRepository: SettingsRepository,
        monefyInteractor: MonefyInteractor,
        context: Context
    ) : this(
        settingsRepository,
        monefyInteractor,
        context.getSharedPreferences("EnvelopesMain", Context.MODE_PRIVATE)
    )

    fun import(input: InputStream) {
        val fromLastImport = settingsRepository.getSetting(Setting.Key.FROM_LAST_IMPORT).checked
        viewModelScope.launch(Dispatchers.IO) {
            input.use { input ->
                val startFrom = if (fromLastImport) {
                    sharedPrefs.getString(LAST_IMPORT, null)?.toDate()
                } else {
                    null
                }
                monefyInteractor.import(input, startFrom)?.let { lastDate ->
                    sharedPrefs.edit().putString(LAST_IMPORT, lastDate.fromDate()).apply()
                }
            }
        }
    }
}

private const val LAST_IMPORT = "LAST_IMPORT"