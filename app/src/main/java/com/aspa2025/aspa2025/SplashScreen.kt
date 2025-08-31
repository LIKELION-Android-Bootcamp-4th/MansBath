package com.aspa2025.aspa2025

import android.util.Log
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.aspa2025.aspa2025.core.constants.enums.RedirectType
import com.aspa2025.aspa2025.features.login.navigation.LoginDestinations
import com.aspa2025.aspa2025.features.main.navigation.MainDestinations
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay


object SplashDestinations {
    const val SPLASH = "splash"
}

@Composable
fun SplashScreen (
    navController: NavHostController,
    viewModel: DataStoreViewModel = hiltViewModel()
) {
    val auth = FirebaseAuth.getInstance()  // todo: 뷰모델 사용
    val isOnboardingCompleted by viewModel.isOnboardingCompleted.collectAsState(initial = false)
    val activity = LocalActivity.current
    val deepLinkData = activity?.intent?.data
    val deepLinkQueryParam = deepLinkData?.getQueryParameter("fromWidget")?.toBoolean() ?: false
    val redirectType = if (deepLinkQueryParam) RedirectType.ROADMAP_STATUS else RedirectType.ROADMAP

    LaunchedEffect(Unit) {
        delay(1000)

        Log.i("ONBOARDING", "onboarding completed : $isOnboardingCompleted")


        if (auth.currentUser != null) { // 로그인 상태
            if (deepLinkData?.host == "roadmap") {  // 위젯에서 진입 → 로드맵 이동
                navController.navigate("${MainDestinations.MAIN}?redirect=${redirectType.name}") {
                    popUpTo(SplashDestinations.SPLASH) { inclusive = true }
                }
            } else {    // 일반 실행 → 온보딩 여부 분기
                if (isOnboardingCompleted) {  // 메인화면으로 이동
                    navController.navigate(MainDestinations.MAIN) {
                        popUpTo(SplashDestinations.SPLASH) { inclusive = true }
                        launchSingleTop = true
                    }
                } else {  // 온보딩 이동
                    navController.navigate(OnboardingDestinations.ONBOARDING) {
                        popUpTo(SplashDestinations.SPLASH) { inclusive = true }
                    }
                }
            }
        } else { // 비로그인 상태
            if (deepLinkData?.host == "roadmap") {  // 위젯에서 진입 → 로그인 이동 & redirect 전달
                navController.navigate("${LoginDestinations.LOGIN}?redirect=${RedirectType.ROADMAP.name}") {
                    popUpTo(SplashDestinations.SPLASH) { inclusive = true }
                    launchSingleTop = true
                }
            } else {    // 런처로 진입 → 로그인 이동
                navController.navigate(LoginDestinations.LOGIN_GRAPH_ROUTE) {
                    popUpTo(SplashDestinations.SPLASH) { inclusive = true }
                    launchSingleTop = true
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.aspalogo),
            contentDescription = "Aspa",
            modifier = Modifier.size(120.dp)
        )
    }
}