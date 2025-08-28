package com.aspa.aspa.features.login

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.aspa.aspa.R
import com.aspa.aspa.OnboardingDestinations
import com.aspa.aspa.data.local.datastore.DataStoreManager
import com.aspa.aspa.features.login.components.SocialButton
import com.aspa.aspa.features.main.navigation.MainDestinations
import com.aspa.aspa.util.DoubleBackExitHandler
import com.navercorp.nid.NaverIdLoginSDK

@Composable
fun LoginScreen(
    navController: NavController,
    dataStoreManager: DataStoreManager,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val naverLauncher = rememberNaverLoginLauncher(
        onAccessToken = { token ->
            authViewModel.signInWithNaver(token)
        },
        onSuccess = {
            navController.navigate(MainDestinations.MAIN)
        },
    )
    val loginState by authViewModel.loginState.collectAsState()
    val isOnboardingCompleted by dataStoreManager.isOnboardingCompleted.collectAsState(initial = false)

    LaunchedEffect(loginState) {
        if (loginState is LoginState.Success) {
            if (isOnboardingCompleted) {
                navController.navigate(MainDestinations.MAIN) {
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }
            } else {
                navController.navigate(OnboardingDestinations.ONBOARDING) {
                    popUpTo(0) { inclusive = true }
                }
            }
        }
    }

    DoubleBackExitHandler()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFD4D4D4)), // 배경 회색
        contentAlignment = Alignment.Center
    ) {
        when (loginState) {
            LoginState.Loading -> {
                CircularProgressIndicator()
            }

            else -> {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.75.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier
                            .padding(24.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.aspalogo),
                            contentDescription = "Aspa",
                            modifier = Modifier.size(120.dp)
                        )

                        // 부제
                        Text(
                            text = "AI와 함께하는 개인 맞춤 학습",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        SocialButton("Google로 계속하기") {
                            authViewModel.signInWithGoogleCredential(
                                activity = navController.context as Activity,
                                onSuccess = {
                                    authViewModel.updateFcmToken()
                                    navController.navigate("main")
                                },
                            ) // TODO : 구글 로그인 성공 응답 처리
                        }

                        SocialButton("카카오톡으로 계속하기") {
                            authViewModel.signInWithKakao(context)
                        }

                        SocialButton("네이버로 계속하기") {
                            NaverIdLoginSDK.authenticate(
                                context = context,
                                launcher = naverLauncher
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun rememberNaverLoginLauncher(
    onAccessToken: (String?) -> Unit,
    onSuccess: () -> Unit
): ActivityResultLauncher<Intent> {

    return rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val accessToken = NaverIdLoginSDK.getAccessToken()

            Log.d("NAVER_LOGIN", "✅ 로그인 성공")
            Log.d("NAVER_LOGIN", "AccessToken: $accessToken")

            onAccessToken(accessToken)
            onSuccess()
        } else {
            val code = NaverIdLoginSDK.getLastErrorCode().code
            val desc = NaverIdLoginSDK.getLastErrorDescription()

            Log.e("NAVER_LOGIN", "❌ 로그인 실패")
            Log.e("NAVER_LOGIN", "Error Code: $code")
            Log.e("NAVER_LOGIN", "Error Desc: $desc")
        }
    }
}
