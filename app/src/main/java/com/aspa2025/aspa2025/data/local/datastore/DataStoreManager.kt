package com.aspa2025.aspa2025.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.aspa2025.aspa2025.core.constants.enums.Provider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")

class DataStoreManager(private val context: Context) {
    companion object {
        private val ONBOARDING_KEY = booleanPreferencesKey("isOnboardingCompleted")
        private val PROVIDER_KEY = stringPreferencesKey("lastLoginProvider")
    }

    val isOnboardingCompleted: Flow<Boolean> = context.dataStore.data
        .map { prefs -> prefs[ONBOARDING_KEY] ?: false }

    val lastLoginProvider: Flow<Provider?> = context.dataStore.data
        .map { prefs ->
            prefs[PROVIDER_KEY]?.let { providerName ->
                runCatching { Provider.valueOf(providerName) }.getOrNull()
            }
        }


    suspend fun setIsOnboardingCompleted(completed: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[ONBOARDING_KEY] = completed
        }
    }

    suspend fun setLastLoginProvider(provider: Provider?) {
        context.dataStore.edit { prefs ->
            if (provider == null) {
                prefs.remove(PROVIDER_KEY)
            } else {
                prefs[PROVIDER_KEY] = provider.name
            }
        }
    }
}