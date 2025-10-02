package com.aspa2025.aspa2025.features.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aspa2025.aspa2025.data.local.datastore.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager,
): ViewModel() {
    val notificationEnabled: Flow<Boolean> = dataStoreManager.notificationEnabled

    private val _toastEvent = Channel<String>()
    val toastEvent = _toastEvent.receiveAsFlow()

    fun changeNotificationEnabled(value: Boolean) {
        viewModelScope.launch {
            dataStoreManager.setNotificationEnabled(value)
        }
    }

    fun showToast(value: Boolean) {
        val boolText = if(value) "퀴즈 알림 푸시 수신에 동의하셨습니다. "
            else "퀴즈 알림 푸시 수신을 거부하셨습니다. "

        val date = System.currentTimeMillis()
        val sdf = SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA)
        val dateText = sdf.format(date)
        _toastEvent.trySend(boolText + dateText)
    }

}