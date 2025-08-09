package com.aspa.aspa

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aspa.aspa.ui.theme.AspaTheme
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthCredential

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Column {
                Box(modifier = Modifier.height(200.dp)) {}
                Button(
                    onClick = { loginWithNaver(this@MainActivity) }
                ) {
                    Text("naver login")
                }
            }

        }
    }
}

fun loginWithNaver(activity: Activity) {
    val providerBuilder = OAuthProvider.newBuilder("oidc.naver").apply {
        // 네이버 OIDC가 지원하는 스코프
        scopes = listOf("openid", "profile")
    }
    val firebaseAuth = FirebaseAuth.getInstance()


    val pendingResultTask = firebaseAuth.pendingAuthResult
    if (pendingResultTask != null) {
        // There's something already here! Finish the sign-in for your user.
        pendingResultTask
            .addOnSuccessListener { result ->
                // User is signed in.
                // IdP data available in
                // authResult.getAdditionalUserInfo().getProfile().
                // The OAuth access token can also be retrieved:
                // ((OAuthCredential)authResult.getCredential()).getAccessToken().
                // The OAuth secret can be retrieved by calling:
                // ((OAuthCredential)authResult.getCredential()).getSecret().
                handleAuthSuccess(result)
            }
            .addOnFailureListener { e ->
                // Handle failure.
                handleAuthFailure(e)
            }
    } else {
        // There's no pending result so you need to start the sign-in flow.
        // See below.

        firebaseAuth
            .startActivityForSignInWithProvider(activity, providerBuilder.build())
            .addOnSuccessListener { result ->
                // User is signed in.
                // IdP data available in
                // authResult.getAdditionalUserInfo().getProfile().
                // The OAuth access token can also be retrieved:
                // ((OAuthCredential)authResult.getCredential()).getAccessToken().
                // The OAuth secret can be retrieved by calling:
                // ((OAuthCredential)authResult.getCredential()).getSecret().
                handleAuthSuccess(result)
            }
            .addOnFailureListener { e ->
                // Handle failure.
                handleAuthFailure(e)
            }
    }
}

private fun handleAuthSuccess(result: AuthResult) {
    val user = result.user
    val profileMap = result.additionalUserInfo?.profile // IdP 원본 프로필(Map)
    val cred = result.credential as? OAuthCredential
    val accessToken = cred?.accessToken
    val idToken = cred?.idToken

    Log.d("AUTH_SUCCESS", "user: $user")
    Log.d("AUTH_SUCCESS", "profileMap: $profileMap")
    Log.d("AUTH_SUCCESS", "accessToken: $accessToken")
    Log.d("AUTH_SUCCESS", "idToken: $idToken")
}

private fun handleAuthFailure(e: Exception) {
    val stack = e.stackTrace.joinToString("\n") { element ->
        "    at $element"
    }
    val fe = e as? com.google.firebase.auth.FirebaseAuthException
    Log.e(
        "AUTH_ERROR",
        "code=${fe?.errorCode}\n" +
                "msg: ${fe?.message}\n" +
                "cause: ${e.cause}\n" +
                "localizedMessage: ${e.localizedMessage}\n" +
                Log.getStackTraceString(e)
    )
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AspaTheme {
        Greeting("Android")
    }
}