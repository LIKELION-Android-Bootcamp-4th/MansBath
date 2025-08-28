package com.aspa.aspa

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.aspa.aspa.core.constants.enums.RedirectType
import com.aspa.aspa.features.login.navigation.LoginDestinations
import com.aspa.aspa.features.main.navigation.MainDestinations
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

object SplashDestinations {
    const val SPLASH = "splash"
}

@Composable
fun SplashScreen (navController: NavHostController) {
    val auth = FirebaseAuth.getInstance()
    val activity = LocalActivity.current
    val deepLinkData = activity?.intent?.data
    val deepLinkQueryParam = deepLinkData?.getQueryParameter("fromWidget")?.toBoolean() ?: false
    val redirectType = if (deepLinkQueryParam) RedirectType.ROADMAP_STATUS else RedirectType.ROADMAP

    LaunchedEffect(Unit) {
        delay(1500)

        if (auth.currentUser != null) {
            if (deepLinkData?.host == "roadmap") {
                navController.navigate("${MainDestinations.MAIN}?redirect=${redirectType.name}") {
                    popUpTo(SplashDestinations.SPLASH) { inclusive = true }
                }
            } else {
                navController.navigate(MainDestinations.MAIN) {
                    popUpTo(SplashDestinations.SPLASH) { inclusive = true }
                }
            }
        } else {
            // 로그인 필요 → redirect 정보 넘기기
            navController.navigate("${LoginDestinations.LOGIN}?redirect=${RedirectType.ROADMAP.name}") {
                popUpTo(SplashDestinations.SPLASH) { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Aspa", style = MaterialTheme.typography.headlineLarge)
    }
}