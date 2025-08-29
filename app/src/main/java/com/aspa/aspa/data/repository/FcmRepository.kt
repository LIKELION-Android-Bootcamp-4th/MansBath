package com.aspa.aspa.data.repository

import android.content.SharedPreferences
import com.aspa.aspa.data.remote.UserRemoteDataSource
import javax.inject.Inject

class FcmRepository @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val sharedPreferences: SharedPreferences
) {
    suspend fun updateFcmToken(uid: String, token: String): Result<Boolean> {
        return try {
            val bool = userRemoteDataSource.updateFcmToken(uid, token)
            Result.success(bool)
        }
        catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun  deleteFcmToken(uid: String): Result<Boolean> {
        return try {
            val bool = userRemoteDataSource.deleteFcmToken(uid)
            Result.success(bool)
        }
        catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getToken(): String? = sharedPreferences.getString("fcm_token", null)
}