package com.aspa.aspa.data.remote

import android.util.Log
import androidx.compose.animation.core.snap
import com.aspa.aspa.data.dto.QuizDto
import com.aspa.aspa.data.dto.QuizDtoAlpha
import com.aspa.aspa.data.dto.QuizzesDto
import com.aspa.aspa.data.dto.RoadmapDto
import com.aspa.aspa.data.dto.RoadmapDtoAlpha
import com.aspa.aspa.model.Roadmap
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.HttpsCallableResult
import com.google.gson.Gson
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class QuizRemoteDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val functions: FirebaseFunctions
) {

    suspend fun getQuizzes(uid: String): List<QuizzesDto> {
        val resultList = mutableListOf<QuizzesDto>()

        val quizzesSnapshot = firestore.collection("users")
            .document(uid)
            .collection("quizzes")
            .get()
            .await()

        for (quizDoc in quizzesSnapshot) {
            val quizSnapshot = quizDoc.reference
                .collection("quiz")
                .orderBy("createdAt")
                .get()
                .await()
            Log.d("Quizzes", "quizSnapshot detected!!!!!!")

            val quizList = quizSnapshot.toObjects(QuizDto::class.java)
            resultList.add(QuizzesDto(roadmapId = quizDoc.id, quiz = quizList))
        }

        return resultList
    }

    suspend fun getQuiz(uid: String, roadmapId: String, quizTitle: String): QuizDto? {
        val snapshot = firestore.collection("users/$uid/quizzes/$roadmapId/quiz")
            .whereEqualTo("quizTitle", quizTitle)
            .limit(1)
            .get()
            .await()

        return snapshot.documents.firstOrNull()?.toObject(QuizDto::class.java)
    }

    suspend fun deleteQuiz(uid: String, roadmapId: String, quizTitle: String): Boolean {
        return try {
            val snapshot = firestore.collection("users/$uid/quizzes/$roadmapId/quiz")
                .whereEqualTo("quizTitle", quizTitle)
                .get()
                .await()

            if (snapshot.isEmpty) {
                println("❌ No quiz found with title '$quizTitle'")
                return false
            }

            // 여러 개일 수 있으니 전부 삭제
            snapshot.documents.forEach { doc ->
                doc.reference.delete().await()
                println("✅ Deleted quiz with id: ${doc.id}")
            }

            true
        } catch (e: Exception) {
            println("❌ Failed to delete quiz: ${e.message}")
            false
        }

    }

    suspend fun sendToMakeQuiz(studyId: String) : QuizDto {
        val data = hashMapOf(
            "studyId" to studyId
        )
        // 로컬 테스트 용
//        functions.useEmulator("10.0.2.2", 5001)
        val result = functions.getHttpsCallable("makeQuiz")
            .call(data).await()

        val gson = Gson()
        return gson.fromJson(gson.toJson(result.getData()), QuizDto::class.java)
    }

    suspend fun updateQuizSolveResult(uid: String, roadmapId: String, quizTitle: String, chosenList: List<String>): Boolean {
        try {
            val snapshot = firestore.collection("users/$uid/quizzes/$roadmapId/quiz")
                .whereEqualTo("quizTitle", quizTitle)
                .limit(1)
                .get()
                .await()
            val doc = snapshot.documents.firstOrNull()
            if (doc == null) {
                println("❌ Error: No document found with quizTitle='$quizTitle'")
                return false
            }

            val docRef = doc.reference

            // 앱의 메모리에서 배열 데이터를 수정
            val oldQuestions = doc.get("questions") as? List<HashMap<String, Any>>
                ?: throw IllegalStateException("Questions field is missing or has a wrong type.")

            if (oldQuestions.size != chosenList.size) {
                throw IllegalArgumentException("The number of answers does not match the number of questions.")
            }

            // 기존 배열을 기반으로 'chosen' 값이 업데이트된 새로운 배열을 생성
            val newQuestions = oldQuestions.mapIndexed { index, questionMap ->
                questionMap.toMutableMap().apply {
                    this["chosen"] = chosenList[index]
                }
            }

            // 'questions' 필드를 새로 만든 배열로 덮어씁니다.
            docRef.update("questions", newQuestions).await()
            docRef.update("status", true).await()

            println("✅ Successfully updated chosen answers for quiz '$quizTitle'.")
            return true
        } catch (e: Exception) {
            println("❌ Failed to update chosen answers: ${e.message}")
            return false
        }

    }

}