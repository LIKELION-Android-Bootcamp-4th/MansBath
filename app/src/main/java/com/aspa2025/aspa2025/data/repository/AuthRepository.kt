package com.aspa2025.aspa2025.data.repository

import com.aspa2025.aspa2025.data.dto.UserProfileDto
import com.aspa2025.aspa2025.core.constants.enums.Provider

interface AuthRepository {

    //토큰 받아온 뒤 로그인까지 수행
    suspend fun signInWithGoogle(idToken : String) : Result<UserProfileDto>
    suspend fun signInWithNaver(accessToken: String?): Result<Any>

    suspend fun fetchProvider(): Result<Provider>
    suspend fun fetchNickname(): Result<String>
    suspend fun fetchEmail(): Result<String>
}