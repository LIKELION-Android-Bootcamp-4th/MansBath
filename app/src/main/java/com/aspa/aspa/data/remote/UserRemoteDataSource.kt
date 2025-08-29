package com.aspa.aspa.data.remote

import com.aspa.aspa.data.dto.UserProfileDto
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

    suspend fun fecthProvider(): String {
        val snapshot = firestore.collection("users").document(auth.uid!!).get().await()

        return snapshot.getString("provider") ?: return ""
    }
}