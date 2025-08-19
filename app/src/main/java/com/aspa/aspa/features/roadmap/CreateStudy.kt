package com.aspa.aspa.features.roadmap

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.functions.FirebaseFunctions
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


@Composable
fun CreateStudy(functions: FirebaseFunctions = FirebaseFunctions.getInstance()){
    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick ={
                scope.launch {
                    DataCreate()
                }
            }
        ) {
            Text("생성하기")
        }
    }
}
suspend fun ensureSignedIn() {
    val auth = FirebaseAuth.getInstance()
    if (auth.currentUser == null) {
        auth.signInAnonymously().await()
    }
}
suspend fun DataCreate(){

    ensureSignedIn()
    val functions = FirebaseFunctions.getInstance("asia-northeast3")
    try {
        val result = functions
            .getHttpsCallable("Study")
            .call()
            .await()

        Log.d("DataCreate", "성공: ${result.getData()}")
    } catch (e: Exception) {
        Log.e("DataCreate", "실패", e)
    }
}