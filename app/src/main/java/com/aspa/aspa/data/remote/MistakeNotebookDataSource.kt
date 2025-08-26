package com.aspa.aspa.data.remote

import com.aspa.aspa.data.dto.MistakeSummary
import com.aspa.aspa.data.remote.dto.QuestionResponseDto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.HttpsCallableResult
import com.google.gson.Gson
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MistakeNotebookDataSource @Inject constructor(
    private val fireStore : FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) {
    val uid = firebaseAuth.uid!!

    private val colRef
        get() = fireStore.collection("users")
            .document(uid)
            .collection("mistakeAnswer")
    suspend fun fetchMistakeList(): List<MistakeSummary>{
        val snap = colRef
            .orderBy("currentAt", Query.Direction.DESCENDING)
            .get()
            .await()
        return snap.documents.map { d ->
            val itemsCount = (d.get("items") as? List<*>)?.size ?: 0
            MistakeSummary(
                id = d.id,
                quizTitle = d.getString("quizTitle").orEmpty(),
                itemsCount = itemsCount,
                currentAt = d.getString("currentAt").orEmpty()
            )
        }

    }
}