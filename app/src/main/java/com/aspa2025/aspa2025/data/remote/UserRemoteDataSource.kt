package com.aspa2025.aspa2025.data.remote

import android.util.Log
import com.aspa2025.aspa2025.data.dto.UserProfileDto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRemoteDataSource @Inject constructor(
//1} 파이어 스토어 불러오기
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    //2) 양식에 맞게 변환 후 파이어 스토어 저장
    suspend fun upsertProfile(dto : UserProfileDto) : UserProfileDto{
        val docId = dto.uid
        val data = hashMapOf(
            "uid" to docId,
            "email" to dto.email,
            "name" to dto.name,
            "provider" to dto.provider,
            "lastLogin" to FieldValue.serverTimestamp()
        )
        firestore.collection("users").document(docId).set(data, SetOptions.merge()).await()
        return dto.copy(uid = docId)
    }

    suspend fun updateFcmToken(uid: String, token: String): Boolean {
        try {
            firestore.collection("users").document(uid).update("fcmToken", token).await()
            Log.d("UserInfo", "FCM 토큰 정보 업데이트 완료")
            return true
        }
        catch (e: Exception) {
            Log.e("UserInfo", "FCM 토큰 정보 업데이트 중 문제 발생", e)
            return false
        }
    }

    suspend fun deleteFcmToken(uid: String): Boolean {
        return try {
            firestore.collection("users").document(uid)
                .update("fcmToken", FieldValue.delete()) // 🔥 필드 삭제
                .await()
            Log.d("UserInfo", "FCM 토큰 정보 삭제 완료")
            true
        } catch (e: Exception) {
            Log.e("UserInfo", "FCM 토큰 정보 삭제 중 문제 발생", e)
            false
        }
    }


    suspend fun fecthProvider(): String {
        val snapshot = firestore.collection("users").document(auth.uid!!).get().await()

        return snapshot.getString("provider") ?: return ""
    }

    suspend fun fetchNickname(): String {
        val snapshot = firestore.collection("users").document(auth.uid!!).get().await()

        return snapshot.getString("name") ?: return ""
    }

    suspend fun fetchEmail(): String {
        val snapshot = firestore.collection("users").document(auth.uid!!).get().await()

        return snapshot.getString("email") ?: return ""
    }
}