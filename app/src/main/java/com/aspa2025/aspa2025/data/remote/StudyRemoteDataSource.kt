package com.aspa2025.aspa2025.data.remote

import com.aspa2025.aspa2025.model.Study
import com.google.firebase.functions.FirebaseFunctions
import com.google.gson.Gson
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class StudyRemoteDataSource @Inject constructor(
    private val functions: FirebaseFunctions,
    private val gson: Gson
){
    suspend fun getStudyData() : Study{
        val result = functions
            .getHttpsCallable("Study")
            .call(emptyMap<String,Any>())
            .await()

        val json = gson.toJson(result.getData())
        val map = gson.fromJson(json,Map::class.java) as Map<*, *>
        val studyJson = gson.toJson(map["study"])
        return gson.fromJson(studyJson,Study::class.java)
    }
}