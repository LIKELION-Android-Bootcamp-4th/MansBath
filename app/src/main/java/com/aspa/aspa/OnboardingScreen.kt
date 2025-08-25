package com.aspa.aspa

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.aspa.aspa.data.local.datastore.DataStoreManager
import com.aspa.aspa.features.main.navigation.MainDestinations
import kotlinx.coroutines.launch

object OnboardingDestinations {
    const val ONBOARDING = "onboarding"
}

@Composable
fun OnboardingScreen(
    navController: NavHostController,
    dataStoreManager: DataStoreManager
) {
    val pagerState = rememberPagerState(pageCount = { 3 }) // 총 3페이지
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            when (page) {
                0 -> OnboardingPage(imageRes = R.drawable.onboarding1, text = "첫 번째 페이지")
                1 -> OnboardingPage(imageRes = R.drawable.onboarding2, text = "두 번째 페이지")
                2 -> OnboardingPage(imageRes = R.drawable.onboarding3, text = "세 번째 페이지")
            }
        }
        // 👇 페이지 인디케이터
        Row(
            modifier = Modifier
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(3) { index ->
                val isSelected = pagerState.currentPage == index
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(if (isSelected) 12.dp else 8.dp)
                        .background(
                            color = if (isSelected) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                            shape = CircleShape
                        )
                )
            }
        }

        if (pagerState.currentPage == pagerState.pageCount - 1) {
            Row(
                modifier = Modifier
                    .background(Color.DarkGray)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "다시 보지 않기",
                    modifier = Modifier.clickable {
                        scope.launch {
                            dataStoreManager.setIsOnboardingCompleted(true)
                        }

                        navController.navigate(MainDestinations.MAIN) {
                            popUpTo(OnboardingDestinations.ONBOARDING) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = "닫기",
                    modifier = Modifier.clickable {
                        scope.launch {
                            dataStoreManager.setIsOnboardingCompleted(false)
                        }

                        navController.navigate(MainDestinations.MAIN) {
                            popUpTo(OnboardingDestinations.ONBOARDING) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun OnboardingPage(imageRes: Int, text: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = text, style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(24.dp))
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
        )
    }
}
