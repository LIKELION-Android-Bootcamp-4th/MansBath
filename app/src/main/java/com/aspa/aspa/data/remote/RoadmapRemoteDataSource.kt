package com.aspa.aspa.data.remote

import com.aspa.aspa.data.dto.RoadmapDocumentDto
import com.aspa.aspa.data.mapper.toRoadmap
import com.aspa.aspa.model.Roadmap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RoadmapRemoteDataSource @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val functions: FirebaseFunctions  // 주입을 통한 리전 자동 설정
) {

    suspend fun fetchRoadmaps(): List<Roadmap> {
        val snapshot = firestore.collection("users")
            .document(auth.currentUser!!.uid)
            .collection("roadmap")
            .get()
            .await()

        val docDtos = snapshot.toObjects(RoadmapDocumentDto::class.java)
        val ids = snapshot.documents.map { it.id }  // roadmapId 추출
        return docDtos.mapIndexed { i, dto -> dto.roadmap.toRoadmap(ids[i], dto.questionId, dto.createdAt) }  // dto -> Roadmap  // with roadmapId, questionId, createdAt
    }

    suspend fun fetchRoadmap(roadmapId: String): Roadmap? {
        val snapshot = firestore.collection("users")
            .document(auth.currentUser!!.uid)
            .collection("roadmap")
            .document(roadmapId)
            .get()
            .await()

        val docsDto = snapshot.toObject(RoadmapDocumentDto::class.java)!!

        return snapshot.toObject(RoadmapDocumentDto::class.java)
            ?.roadmap
            ?.toRoadmap(roadmapId, docsDto.questionId, docsDto.createdAt)
    }

    suspend fun generateRoadmap(questionId: String): String {
        val result = functions
            .getHttpsCallable("generateRoadmap")
            .call(mapOf("uid" to auth.currentUser!!.uid, "questionId" to questionId))
            .await()

        val payload = result.getData() as? Map<*, *>
            ?: error("Empty payload")

        val roadmapId = payload["docId"] as? String ?: error("docId missing")

        return roadmapId
    }

    suspend fun isStudyExist(roadmapId: String, sectionId: Int): Boolean {
        val snapshot = firestore.collection("users")
            .document(auth.currentUser!!.uid)
            .collection("studies")
            .whereEqualTo("roadmapId", roadmapId)
            .whereEqualTo("sectionId", sectionId)
            .get()
            .await()

        return !snapshot.isEmpty
        // return snapshot.exists()
    }

    suspend fun isQuizExist(roadmapId: String, sectionId: Int): Boolean {
        val snapshot = firestore.collection("users")
            .document(auth.currentUser!!.uid)
            .collection("quizzes")
            .document(roadmapId)
            .collection("quiz")
            .whereEqualTo("sectionId", sectionId)
            .get()
            .await()

        return !snapshot.isEmpty
        // return snapshot.exists()
    }
}
