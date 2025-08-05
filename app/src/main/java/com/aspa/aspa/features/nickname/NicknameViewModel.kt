package com.aspa.aspa.features.nickname

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class NicknameViewModel : ViewModel() {
    
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    
    private val _nicknameState = MutableStateFlow<NicknameState>(NicknameState.Idle)
    val nicknameState: StateFlow<NicknameState> = _nicknameState.asStateFlow()
    
    private val _nickname = MutableStateFlow("")
    val nickname: StateFlow<String> = _nickname.asStateFlow()
    
    fun updateNickname(newNickname: String) {
        // 최대 10자까지만 입력 가능
        if (newNickname.length <= 10) {
            _nickname.value = newNickname
        }
    }
    
    fun saveNickname() {
        viewModelScope.launch {
            try {
                _nicknameState.value = NicknameState.Loading
                
                val currentUser = auth.currentUser
                if (currentUser == null) {
                    _nicknameState.value = NicknameState.Error("사용자 정보를 찾을 수 없습니다.")
                    return@launch
                }
                
                val trimmedNickname = _nickname.value.trim()
                
                // 유효성 검사
                when {
                    trimmedNickname.isEmpty() -> {
                        _nicknameState.value = NicknameState.Error("닉네임을 입력해주세요.")
                        return@launch
                    }
                    trimmedNickname.length < 2 -> {
                        _nicknameState.value = NicknameState.Error("닉네임은 2자 이상 입력해주세요.")
                        return@launch
                    }
                    trimmedNickname.length > 10 -> {
                        _nicknameState.value = NicknameState.Error("닉네임은 10자 이하로 입력해주세요.")
                        return@launch
                    }
                    !isValidNickname(trimmedNickname) -> {
                        _nicknameState.value = NicknameState.Error("닉네임에는 특수문자를 사용할 수 없습니다.")
                        return@launch
                    }
                }
                
                // 닉네임 중복 확인
                val isDuplicate = checkNicknameDuplicate(trimmedNickname)
                if (isDuplicate) {
                    _nicknameState.value = NicknameState.Error("이미 사용 중인 닉네임입니다.")
                    return@launch
                }
                
                // Firestore에 닉네임 업데이트
                firestore.collection("users")
                    .document(currentUser.uid)
                    .update("nickname", trimmedNickname)
                    .await()
                
                _nicknameState.value = NicknameState.Success
                
            } catch (e: Exception) {
                _nicknameState.value = NicknameState.Error("닉네임 저장에 실패했습니다. 다시 시도해주세요.")
            }
        }
    }
    
    private fun isValidNickname(nickname: String): Boolean {
        // 한글, 영문, 숫자만 허용
        val pattern = "^[가-힣a-zA-Z0-9]+$".toRegex()
        return pattern.matches(nickname)
    }
    
    private suspend fun checkNicknameDuplicate(nickname: String): Boolean {
        return try {
            val snapshot = firestore.collection("users")
                .whereEqualTo("nickname", nickname)
                .get()
                .await()
            
            !snapshot.isEmpty
        } catch (e: Exception) {
            false // 에러 발생 시 중복이 아닌 것으로 처리
        }
    }
    
    fun resetState() {
        _nicknameState.value = NicknameState.Idle
    }
}

sealed class NicknameState {
    object Idle : NicknameState()
    object Loading : NicknameState()
    object Success : NicknameState()
    data class Error(val message: String) : NicknameState()
} 