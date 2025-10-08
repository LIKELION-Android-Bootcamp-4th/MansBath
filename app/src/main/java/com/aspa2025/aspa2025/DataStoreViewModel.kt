package com.aspa2025.aspa2025

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aspa2025.aspa2025.core.constants.enums.Provider
import com.aspa2025.aspa2025.data.local.datastore.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DataStoreViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    val isOnboardingCompleted: Flow<Boolean> = dataStoreManager.isOnboardingCompleted
    val lastLoginProvider: Flow<Provider?> = dataStoreManager.lastLoginProvider

    fun setIsOnboardingCompleted(completed: Boolean) {
        viewModelScope.launch {
            dataStoreManager.setIsOnboardingCompleted(completed)
        }
    }

    fun setLastLoginProvider(provider: Provider?) {
        viewModelScope.launch {
            dataStoreManager.setLastLoginProvider(provider)
        }
    }
}