package com.aspa.aspa.features.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LoginViewModel : ViewModel() {
    
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()
    
    fun signInWithGoogle(task: Task<GoogleSignInAccount>) {
        viewModelScope.launch {
            try {
                _loginState.value = LoginState.Loading
                
                val account = task.getResult(ApiException::class.java)
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                
                val result = auth.signInWithCredential(credential).await()
                
                if (result.user != null) {
                    // 사용자 정보를 Firestore에 저장
                    saveUserToFirestore(result.user!!)
                    
                    // 닉네임이 이미 설정되어 있는지 확인
                    val hasNickname = checkUserNickname(result.user!!.uid)
                    if (hasNickname) {
                        _loginState.value = LoginState.Success(result.user!!)
                    } else {
                        _loginState.value = LoginState.NeedNickname(result.user!!)
                    }
                } else {
                    _loginState.value = LoginState.Error("로그인에 실패했습니다.")
                }
                
            } catch (e: ApiException) {
                _loginState.value = LoginState.Error("Google 로그인에 실패했습니다: ${e.message}")
            } catch (e: Exception) {
                _loginState.value = LoginState.Error("로그인 중 오류가 발생했습니다: ${e.message}")
            }
        }
    }
    
    private suspend fun saveUserToFirestore(user: com.google.firebase.auth.FirebaseUser) {
        try {
            val userData = hashMapOf(
                "uid" to user.uid,
                "email" to user.email,
                "displayName" to user.displayName,
                "photoUrl" to user.photoUrl?.toString(),
                "provider" to "google",
                "createdAt" to com.google.firebase.Timestamp.now()
            )
            
            firestore.collection("users")
                .document(user.uid)
                .set(userData, com.google.firebase.firestore.SetOptions.merge())
                .await()
                
        } catch (e: Exception) {
            // Firestore 저장 실패는 로그인 성공을 방해하지 않도록 로그만 남김
            println("Firestore 저장 실패: ${e.message}")
        }
    }
    
    private suspend fun checkUserNickname(uid: String): Boolean {
        return try {
            val document = firestore.collection("users")
                .document(uid)
                .get()
                .await()
            
            val nickname = document.getString("nickname")
            !nickname.isNullOrEmpty()
        } catch (e: Exception) {
            false
        }
    }
    
    fun resetState() {
        _loginState.value = LoginState.Idle
    }
}

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val user: com.google.firebase.auth.FirebaseUser) : LoginState()
    data class NeedNickname(val user: com.google.firebase.auth.FirebaseUser) : LoginState()
    data class Error(val message: String) : LoginState()
} 