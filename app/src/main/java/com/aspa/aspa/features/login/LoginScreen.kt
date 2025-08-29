package com.aspa.aspa.features.login

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.aspa.aspa.OnboardingDestinations
import com.aspa.aspa.R
import com.aspa.aspa.core.constants.enums.Provider
import com.aspa.aspa.data.local.datastore.DataStoreManager
import com.aspa.aspa.features.login.components.SocialButton
import com.aspa.aspa.features.main.navigation.MainDestinations
import com.aspa.aspa.features.roadmap.navigation.RoadmapDestinations
import com.aspa.aspa.ui.theme.AppSpacing
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
        onAccessToken = { token -> authViewModel.signInWithNaver(token) },
        onSuccess = {},
    )
    val loginState by authViewModel.loginState.collectAsState()
    val isOnboardingCompleted by dataStoreManager.isOnboardingCompleted.collectAsState(initial = false)
    val lastLoginProvider by dataStoreManager.lastLoginProvider.collectAsState(initial = null)

    val permissionState by authViewModel.permissionState.collectAsStateWithLifecycle()
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            // shouldShowRationale 값을 얻기 위해 Activity context가 필요합니다.
            val activity = context as? Activity
            val shouldShowRationale = activity?.shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) ?: false
            authViewModel.onPermissionResult(
                isGranted = isGranted,
                shouldShowRationale = shouldShowRationale
            )
        }
    )

    LaunchedEffect(loginState, permissionState) {
        if (loginState is LoginState.Success) {
            val redirect = navController.currentBackStackEntry
                ?.arguments?.getString("redirect")

            when {
                redirect == "roadmap" -> {  // 위젯의 경우 바로 이동
                    navController.navigate(RoadmapDestinations.roadmapList(fromWidget = true)) {
                        popUpTo(0) { inclusive = true }
                    }
                }
                isOnboardingCompleted -> {  // 온보딩 검사
                    navController.navigate(MainDestinations.MAIN) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
                else -> {  // 온보딩 검사
                    navController.navigate(OnboardingDestinations.ONBOARDING) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            }
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if(permissionState is PermissionState.Idle) {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    DoubleBackExitHandler()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if(permissionState is PermissionState.Denied && (permissionState as PermissionState.Denied).shouldShowRationale) {
                RationaleDialog(
                    onConfirm = { permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS) },
                    onDismiss = {
                        Toast.makeText(context,
                            "퀴즈 알림 권한이 거부되었습니다.", Toast.LENGTH_SHORT)
                            .show()
                    }
                )
            }
        }

        when (loginState) {
            LoginState.Loading -> {
                CircularProgressIndicator()
            }
            else -> {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    shape = MaterialTheme.shapes.large,
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    modifier = Modifier.fillMaxWidth(0.85f)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(AppSpacing.lg),
                        modifier = Modifier.padding(AppSpacing.xl)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.aspalogo),
                            contentDescription = "Aspa",
                            modifier = Modifier.size(120.dp)
                        )

                        Text(
                            text = "AI와 함께하는 개인 맞춤 학습",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(AppSpacing.sm))

                        val buttons: List<Pair<Provider, @Composable (Boolean) -> Unit>> = listOf(
                            Provider.GOOGLE to { isLastLogin ->
                                SocialButton(
                                    provider = Provider.GOOGLE,
                                    isLastLogin = isLastLogin
                                ) {
                                    authViewModel.signInWithGoogleCredential(
                                        activity = navController.context as Activity,
                                        onSuccess = {}
                                    )
                                }
                            },
                            Provider.KAKAO to { isLastLogin ->
                                SocialButton(
                                    provider = Provider.KAKAO,
                                    isLastLogin = isLastLogin,
                                ) {
                                    authViewModel.signInWithKakao(context)
                                }
                            },
                            Provider.NAVER to { isLastLogin ->
                                SocialButton(
                                    provider = Provider.NAVER,
                                    isLastLogin = isLastLogin,
                                ) {
                                    NaverIdLoginSDK.authenticate(
                                        context = context,
                                        launcher = naverLauncher
                                    )
                                }
                            }
                        )

                        buttons.forEach { (provider, buttonContent) ->
                            buttonContent( lastLoginProvider == provider )
                        }

                        Spacer(modifier = Modifier.height(4.dp))
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
