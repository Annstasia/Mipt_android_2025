package ru.mipt.annstase.hw2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.mipt.annstase.hw2.data.repository.SettingsRepository
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: SettingsRepository
) : ViewModel() {

    val filtersEnabled: StateFlow<Boolean> = repository.filtersEnabled
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val adsEnabled: StateFlow<Boolean> = repository.adsEnabled
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val sortByUrgency: StateFlow<Boolean> = repository.sortByUrgency
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun setFiltersEnabled(enabled: Boolean) {
        viewModelScope.launch {
            repository.setFiltersEnabled(enabled)
        }
    }

    fun setAdsEnabled(enabled: Boolean) {
        viewModelScope.launch {
            repository.setAdsEnabled(enabled)
        }
    }

    fun setSortByUrgency(enabled: Boolean) {
        viewModelScope.launch {
            repository.setSortByUrgency(enabled)
        }
    }
}
