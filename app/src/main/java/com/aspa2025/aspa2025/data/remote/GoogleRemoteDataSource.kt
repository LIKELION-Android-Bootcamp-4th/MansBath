package com.aspa2025.aspa2025.data.remote


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class GoogleRemoteDataSource
@Inject constructor( //생성자 선언하기
    private val auth: FirebaseAuth
){
   suspend fun signInWithGoogle(idToken: String): FirebaseUser? {
       // 자격 증명 및 사용자 정보 리턴 반환
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        val result = auth.signInWithCredential(credential).await()
        return result.user
    }

}