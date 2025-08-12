package com.aspa.aspa.data.repository

import android.util.Log
import android.util.Base64
import com.aspa.aspa.data.dto.UserProfileDto
import com.aspa.aspa.data.remote.GoogleRemoteDataSource
import com.aspa.aspa.data.remote.UserRemoteDataSource
import com.google.firebase.auth.GoogleAuthProvider
import org.json.JSONObject
import javax.inject.Inject


class GoogleRepositoryImpl @Inject constructor( // 힐트 생성자
    private val googleDs: GoogleRemoteDataSource,
    private val userDs: UserRemoteDataSource
) : AuthRepository { //1. 현재 inject 주입을 통해서 생성
    override suspend fun signInWithGoogle(idToken: String): Result<UserProfileDto> = runCatching {

        // 1) Firebase 로그인
        val firebaseUser = googleDs.signInWithGoogle(idToken)
            ?: error("로그인 실패")

        // 2) 이메일 확인 현재 providers 안에서 추출이 됨.
        val email = firebaseUser.email
            ?: firebaseUser.providerData.firstOrNull{it.providerId == GoogleAuthProvider.PROVIDER_ID}?.email
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
}
