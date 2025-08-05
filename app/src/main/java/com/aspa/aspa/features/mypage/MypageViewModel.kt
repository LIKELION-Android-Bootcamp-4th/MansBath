package com.aspa.aspa.features.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MypageViewModel : ViewModel() {
    private val auth = try {
        FirebaseAuth.getInstance()
    } catch (e: Exception) {
        null
    }
    
    private val _currentUser = MutableStateFlow<FirebaseUser?>(null)
    val currentUser: StateFlow<FirebaseUser?> = _currentUser.asStateFlow()
    
    init {
        try {
            // 현재 로그인된 사용자 정보 가져오기
            _currentUser.value = auth?.currentUser
            
            // 인증 상태 변경 감지
            auth?.addAuthStateListener { firebaseAuth ->
                _currentUser.value = firebaseAuth.currentUser
            }
        } catch (e: Exception) {
            println("Firebase 초기화 실패: ${e.message}")
            // Preview 환경에서는 Firebase가 없을 수 있으므로 무시
        }
    }
    
    fun logout() {
        viewModelScope.launch {
            try {
                auth?.signOut()
                // 로그아웃 후 상태 초기화
                _currentUser.value = null
            } catch (e: Exception) {
                println("로그아웃 실패: ${e.message}")
            }
        }
    }
} 