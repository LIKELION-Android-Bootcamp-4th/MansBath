package com.aspa.aspa.features.login

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aspa.aspa.R
import com.aspa.aspa.ui.theme.AspaTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(),
    onLoginSuccess: (com.google.firebase.auth.FirebaseUser) -> Unit = {},
    onNeedNickname: (com.google.firebase.auth.FirebaseUser) -> Unit = {}
) {
    val context = LocalContext.current
    val loginState by viewModel.loginState.collectAsState()
    
    // Google Sign-In 설정 (Firebase 설정이 없을 때를 대비한 예외 처리)
    val gso = remember {
        try {
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("YOUR_WEB_CLIENT_ID") // Firebase Console에서 가져온 Web Client ID
                .requestEmail()
                .build()
        } catch (e: Exception) {
            // Firebase 설정이 없을 때 기본 설정
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        }
    }
    
    val googleSignInClient = remember {
        try {
            GoogleSignIn.getClient(context, gso)
        } catch (e: Exception) {
            null
        }
    }
    
    // Google 로그인 결과 처리
    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            try {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                viewModel.signInWithGoogle(task)
            } catch (e: Exception) {
                // Firebase 설정이 없을 때 처리
                println("Google 로그인 처리 중 오류: ${e.message}")
            }
        }
    }
    
    // 로그인 상태에 따른 처리
    LaunchedEffect(loginState) {
        when (loginState) {
            is LoginState.Success -> {
                onLoginSuccess((loginState as LoginState.Success).user)
                viewModel.resetState()
            }
            is LoginState.NeedNickname -> {
                onNeedNickname((loginState as LoginState.NeedNickname).user)
                viewModel.resetState()
            }
            is LoginState.Error -> {
                println("로그인 실패: ${(loginState as LoginState.Error).message}")
                viewModel.resetState()
            }
            else -> {}
        }
    }
    
    LoginScreenContent(
        loginState = loginState,
        onGoogleSignInClick = {
            try {
                googleSignInClient?.let { client ->
                    googleSignInLauncher.launch(client.signInIntent)
                }
            } catch (e: Exception) {
                println("Google 로그인 버튼 클릭 중 오류: ${e.message}")
            }
        }
    )
}

@Composable
private fun LoginScreenContent(
    loginState: LoginState,
    onGoogleSignInClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // 메인 카드
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
                .align(Alignment.Center),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 앱 로고
                Text(
                    text = "🧠",
                    fontSize = 48.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                // 제목
                Text(
                    text = "ASPA에 오신 것을 환영합니다",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                // 설명
                Text(
                    text = "소셜 계정으로 간편하게 로그인하세요",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 32.dp)
                )
                
                // Google 로그인 버튼
                SocialLoginButton(
                    icon = R.drawable.ic_google,
                    text = "Google로 계속하기",
                    backgroundColor = Color.White,
                    textColor = Color.Black,
                    borderColor = Color(0xFFE0E0E0),
                    modifier = Modifier.padding(bottom = 12.dp),
                    enabled = loginState !is LoginState.Loading,
                    onClick = onGoogleSignInClick
                )
                
                // 카카오 로그인 버튼
                SocialLoginButton(
                    icon = R.drawable.ic_kakao,
                    text = "카카오톡으로 계속하기",
                    backgroundColor = Color(0xFFFEE500),
                    textColor = Color(0xFF191919),
                    borderColor = Color(0xFFFEE500),
                    modifier = Modifier.padding(bottom = 12.dp),
                    enabled = false, // 아직 구현되지 않음
                    onClick = { /* TODO: 카카오 로그인 구현 */ }
                )
                
                // 네이버 로그인 버튼
                SocialLoginButton(
                    icon = R.drawable.ic_naver,
                    text = "네이버로 계속하기",
                    backgroundColor = Color(0xFF03C75A),
                    textColor = Color.White,
                    borderColor = Color(0xFF03C75A),
                    modifier = Modifier.padding(bottom = 24.dp),
                    enabled = false, // 아직 구현되지 않음
                    onClick = { /* TODO: 네이버 로그인 구현 */ }
                )
                
                // 로딩 상태 표시
                if (loginState is LoginState.Loading) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            color = Color.Black,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "로그인 중...",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SocialLoginButton(
    icon: Int,
    text: String,
    backgroundColor: Color,
    textColor: Color,
    borderColor: Color,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            disabledContainerColor = backgroundColor.copy(alpha = 0.6f)
        ),
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
        enabled = enabled
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = text,
                modifier = Modifier.size(24.dp),
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = text,
                color = textColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    AspaTheme {
        LoginScreenContent(
            loginState = LoginState.Idle,
            onGoogleSignInClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenLoadingPreview() {
    AspaTheme {
        LoginScreenContent(
            loginState = LoginState.Loading,
            onGoogleSignInClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenErrorPreview() {
    AspaTheme {
        LoginScreenContent(
            loginState = LoginState.Error("로그인에 실패했습니다."),
            onGoogleSignInClick = {}
        )
    }
}