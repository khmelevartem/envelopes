package com.tubetoast.envelopes.ui.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tubetoast.envelopes.common.domain.models.DateConverter.fromDate
import com.tubetoast.envelopes.common.domain.models.DateConverter.toDate
import com.tubetoast.envelopes.common.settings.Setting
import com.tubetoast.envelopes.common.settings.SettingsRepository
import com.tubetoast.envelopes.monefy.data.MonefyInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(
    private val settingsRepository: SettingsRepository,
    private val monefyInteractor: MonefyInteractor,
    private val kvStore: LocalKVStore
) : ViewModel() {
    fun import(fileLines: List<String>) {
        val fromLastImport = settingsRepository.getSetting(Setting.Key.FROM_LAST_IMPORT).checked

        viewModelScope.launch(Dispatchers.Default) {
            val startFrom = if (fromLastImport) {
                kvStore.getString(LAST_IMPORT)?.toDate()
            } else {
                null
            }

            monefyInteractor.import(fileLines, startFrom)?.let { lastDate ->
                kvStore.putString(LAST_IMPORT, lastDate.fromDate())
            }
        }

        viewModelScope.launch(Dispatchers.Default) {
            settingsRepository.getSettingFlow(Setting.Key.DELETE_SPENDING).collect {
                if (it.checked) kvStore.putString(LAST_IMPORT, null)
            }
        }
    }

    companion object {
        private const val LAST_IMPORT = "com.tubetoast.envelopes.android.LAST_IMPORT"
    }
}
