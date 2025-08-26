package com.aspa.aspa.features.quiz

import android.Manifest
import android.app.Activity
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aspa.aspa.features.quiz.component.QuizListCard
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    navController: NavController,
    viewModel: QuizViewModel,
    roadmapId: String?
) {
    val expandedIndex = remember { mutableStateOf(-1) }
    val quizListState by viewModel.quizListState.collectAsState()
    val context = LocalContext.current
    val permissionState by viewModel.permissionState.collectAsStateWithLifecycle()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            // shouldShowRationale 값을 얻기 위해 Activity context가 필요합니다.
            val activity = context as? Activity
            val shouldShowRationale = activity?.shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) ?: false
            viewModel.onPermissionResult(
                isGranted = isGranted,
                shouldShowRationale = shouldShowRationale
            )
        }
    )
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        if(roadmapId != null && roadmapId != "") {
            // viewModel.requestQuizFromRoadmap(roadmapId)
            Toast.makeText(context, "퀴즈 생성중입니다. 기다려주세요..", Toast.LENGTH_SHORT).show()
        }
        else {
            viewModel.getQuizzes()
        }
    }


    Scaffold(
        /*topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            textAlign = TextAlign.Center,
                            text = "Aspa"
                        )
                    }

                },
            )
        },*/
        containerColor = Color.White,
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 8.dp)
            ) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    when(val state = permissionState) {
                        is PermissionState.Denied -> {
                            if(state.shouldShowRationale) {
                                RationaleDialog(
                                    onConfirm = { launcher.launch(Manifest.permission.POST_NOTIFICATIONS) },
                                    onDismiss = {
                                        Toast.makeText(context,
                                            "퀴즈 알림 권한이 거부되었습니다.", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                )
                            }
                        }
                        PermissionState.Granted -> {}
                        PermissionState.Idle -> {
                            launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                    }
                }


                Text(
                    modifier = Modifier.padding(vertical = 4.dp),
                    text = "퀴즈",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                )

                Text(
                    text = "학습한 내용을 확인해 보세요.",
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(12.dp))

                when(val state = quizListState) {
                    QuizListState.Loading -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("퀴즈 조회 중..")
                            Spacer(Modifier.height(16.dp))
                            CircularProgressIndicator()
                        }
                    }
                    is QuizListState.Success ->
                        LazyColumn {
                            itemsIndexed(state.quizzes) { index, item ->
                                QuizListCard(
                                    index = index,
                                    item = item,
                                    expandedIndex = expandedIndex.value,
                                    onClick = { expandedIndex.value = it },
                                    navController = navController,
                                    viewModel = viewModel
                                )
                            }
                        }
                    is QuizListState.Error -> Text("에러 발생: ${state.error}")
                }

            }
        }
    )
}

@Composable
fun RationaleDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("권한 필요") },
        text = { Text("Aspa에서는 안 푼 퀴즈가 있다면 지속적으로 알림을 보내드립니다. 허용해주시겠어요?") },
        confirmButton = {
            Button(onClick = onConfirm) { Text("허용") }
        },
        dismissButton = {
            Button(onClick = onDismiss) { Text("거부") }
        }
    )
}


/*
@Preview(showBackground = true)
@Composable
fun QuizScreenPreview() {
    val navController = rememberNavController()
    QuizScreen(navController = navController)
}*/
