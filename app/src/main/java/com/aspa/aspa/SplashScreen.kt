package com.aspa.aspa

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
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

    LaunchedEffect(Unit) {
        delay(1500)

        if (auth.currentUser != null) {
            navController.navigate(MainDestinations.MAIN) {
                popUpTo(SplashDestinations.SPLASH) { inclusive = true }
            }
        } else {
            navController.navigate(LoginDestinations.LOGIN_GRAPH_ROUTE) {
                popUpTo(SplashDestinations.SPLASH) { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.aspalogo),
            contentDescription = "Aspa",
            modifier = Modifier.size(120.dp)
        )
    }
}