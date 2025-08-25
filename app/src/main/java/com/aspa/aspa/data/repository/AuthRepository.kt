package com.aspa.aspa.data.repository

import com.aspa.aspa.data.dto.UserProfileDto

interface AuthRepository {

    //토큰 받아온 뒤 로그인까지 수행
    suspend fun signInWithGoogle(idToken : String) : Result<UserProfileDto>
    suspend fun signInWithNaver(accessToken: String?): Result<Any>
}