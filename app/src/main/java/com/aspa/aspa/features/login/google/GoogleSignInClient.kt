package com.aspa.aspa.features.login.google

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.aspa.aspa.R
import com.aspa.aspa.features.login.LoginState
import com.aspa.aspa.features.login.LoginViewModel
import com.aspa.aspa.model.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

@Composable
fun rememberGoogleSignInClient(): GoogleSignInClient {
    val context = LocalContext.current
    val gso = remember {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    }
    return remember { GoogleSignIn.getClient(context, gso) }
}

@Composable
fun googleSignInHandler(
    viewModel: LoginViewModel,
    navController: NavController
): ManagedActivityResultLauncher<Intent, ActivityResult> {

    val context = LocalContext.current
    val loginState by viewModel.loginState.collectAsState()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)!!
            viewModel.signInWithGoogleCredential(account.idToken!!)
        } catch (e: ApiException) {
            Log.e("SIGNIN", e.toString())
            Toast.makeText(context, "Google 로그인에 실패했습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(key1 = loginState) {
        if (loginState is LoginState.Success) {
            val user = (loginState as LoginState.Success).user
            Auth.uid = user.uid
            navController.navigate("nickname") {
                popUpTo("login") { inclusive = true }
            }
        } else if (loginState is LoginState.Error) {
            val message = (loginState as LoginState.Error).message
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }

    return launcher
}