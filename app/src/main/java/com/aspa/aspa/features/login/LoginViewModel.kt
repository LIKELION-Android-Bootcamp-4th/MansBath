package com.aspa.aspa.features.login

import android.app.Activity
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aspa.aspa.data.dto.UserProfileDto
import com.aspa.aspa.data.repository.AuthRepository
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface LoginState {
    object Idle : LoginState
    object Loading : LoginState
    data class Success(val user: UserProfileDto?) : LoginState
    data class Error(val message: String) : LoginState
}

@HiltViewModel //viewModel 등록 및 생성
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val credentialManager: CredentialManager,
    private val getCredentialRequest: GetCredentialRequest
) : ViewModel() {

    companion object { private const val TAG = "LoginVM" }

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState = _loginState.asStateFlow()

    fun signInWithGoogleCredential(activity: Activity, onSuccess: () -> Unit) {

        viewModelScope.launch {
            _loginState.value = LoginState.Loading

            try {
                // 1) CredentialManager에서 자격 증명 받기
                val response = credentialManager.getCredential(activity, getCredentialRequest)
                val cred = response.credential

                // 2) Google ID 토큰 추출
                val idToken = when (cred) {
                    is CustomCredential -> {

                        if (cred.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {

                            val token = GoogleIdTokenCredential.createFrom(cred.data).idToken
                            token
                        } else {
                            null
                        }
                    }

                    else -> {
                        Log.w(TAG, "문제가 발생 하였습니다 ")
                        null
                    }
                }

                if (idToken.isNullOrBlank()) {
                    Log.e(TAG, "토큰을 가져오지 못함.")
                    return@launch
                }

                // 3) Repository 로그인 + 프로필 저장
                var repoResult: Result<UserProfileDto> = authRepository.signInWithGoogle(idToken)

                repoResult
                    .onSuccess { dto ->
                        Log.d(TAG, "성공하였습니다.")
                        _loginState.value = LoginState.Success(dto)
                        onSuccess()
                    }
                    .onFailure { e ->
                        Log.e(TAG, "repo failure: ", e)
                    }

            }catch (error : Exception){
                Log.e(TAG,"문제가 발생했습니다. ${error.message}")

            }
        }
    }

    fun signInWithNaver(accessToken: String?) = viewModelScope.launch {
        _loginState.value = LoginState.Loading
        authRepository.signInWithNaver(accessToken)
            .onSuccess { _loginState.value = LoginState.Success(null) }
            .onFailure { e ->
                _loginState.value = LoginState.Error(e.message ?: "❌ 네이버 로그인 실패")
            }
    }
}
