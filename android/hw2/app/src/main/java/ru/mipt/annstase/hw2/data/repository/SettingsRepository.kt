package ru.mipt.annstase.hw2.data.repository

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(name = "settings")

object PreferenceKeys {
    val FILTERS_ENABLED = booleanPreferencesKey("filters_enabled")
    val ADS_ENABLED     = booleanPreferencesKey("ads_enabled")
    val SORT_BY_URGENCY = booleanPreferencesKey("sort_by_urgency")
}

class SettingsRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    val filtersEnabled: Flow<Boolean> = context.dataStore.data
        .map { it[PreferenceKeys.FILTERS_ENABLED] ?: true }
        .distinctUntilChanged()

    val adsEnabled: Flow<Boolean> = context.dataStore.data
        .map { it[PreferenceKeys.ADS_ENABLED] ?: true }
        .distinctUntilChanged()

    val sortByUrgency: Flow<Boolean> = context.dataStore.data
        .map { it[PreferenceKeys.SORT_BY_URGENCY] ?: false }
        .distinctUntilChanged()

    suspend fun setFiltersEnabled(value: Boolean) {
        context.dataStore.edit { it[PreferenceKeys.FILTERS_ENABLED] = value }
    }

    suspend fun setAdsEnabled(value: Boolean) {
        context.dataStore.edit { it[PreferenceKeys.ADS_ENABLED] = value }
    }

    suspend fun setSortByUrgency(value: Boolean) {
        context.dataStore.edit { it[PreferenceKeys.SORT_BY_URGENCY] = value }
    }
}
