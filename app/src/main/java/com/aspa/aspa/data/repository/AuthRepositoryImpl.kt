package com.aspa.aspa.data.repository

import android.util.Log
import com.aspa.aspa.data.dto.UserProfileDto
import com.aspa.aspa.data.remote.GoogleRemoteDataSource
import com.aspa.aspa.data.remote.UserRemoteDataSource
import com.aspa.aspa.model.Provider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.functions.FirebaseFunctions
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class AuthRepositoryImpl @Inject constructor(
    // 힐트 생성자
    private val googleDs: GoogleRemoteDataSource,
    private val userDs: UserRemoteDataSource,
    private val auth: FirebaseAuth,
    private val functions: FirebaseFunctions, // functions("asia-northeast3")로 제공되도록 Hilt 모듈에서 주입
) : AuthRepository { //1. 현재 inject 주입을 통해서 생성
    override suspend fun signInWithGoogle(idToken: String): Result<UserProfileDto> = runCatching {

        // 1) Firebase 로그인
        val firebaseUser = googleDs.signInWithGoogle(idToken)
            ?: error("로그인 실패")

        // 2) 이메일 확인 현재 providers 안에서 추출이 됨.
        val email = firebaseUser.email
            ?: firebaseUser.providerData.firstOrNull { it.providerId == GoogleAuthProvider.PROVIDER_ID }?.email
            ?: error("구글 이메일을 가져오는데 문제가 발생했습니다.")

        // 4) DTO 만들고 저장
        val dto = UserProfileDto(
            uid = firebaseUser.uid,
            email = email,
            name = firebaseUser.displayName,
            provider = "google"
        )

        val saved = userDs.upsertProfile(dto) // await 포함
        saved
    }.onFailure {
        Log.e("AuthRepo", "signInWithGoogle FAILED: ${it.javaClass.simpleName} ${it.message}", it)
    }


    override suspend fun signInWithNaver(accessToken: String?) = runCatching {
        require(!accessToken.isNullOrEmpty()) { "❌ Access Token이 없습니다." }

        val data = hashMapOf(
            "accessToken" to accessToken
        )

        val customToken = functions
            .getHttpsCallable("loginWithNaver")
            .call(data)
            .await()
            .getData() as String

        auth.signInWithCustomToken(customToken).await()
        Log.d("NAVER_LOGIN", "✅ 파이어베이스 세션 로그인 성공")
        Log.d("NAVER_LOGIN", "uid : ${auth.uid}")

    }.onFailure {
        Log.e("AuthRepo", "signInWithNaver FAILED: ${it.javaClass.simpleName} ${it.message}", it)
    }

    override suspend fun fetchProvider(): Result<Provider> = runCatching {
        val provider = userDs.fecthProvider()
        Log.d("LOGOUT", "provider: $provider")
        when (provider) {
            "google" -> Provider.GOOGLE
            "kakao" -> Provider.KAKAO
            "naver" -> Provider.NAVER
            else -> throw IllegalStateException("❌ Unknown provider")
        }
    }.onFailure { e ->
        Log.e("LOGOUT", "❌ fetchProvider FAILED: ${e.javaClass.simpleName} ${e.message}", e)
    }
}
