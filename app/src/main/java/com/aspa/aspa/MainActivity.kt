package com.aspa.aspa

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.functions.functions
import com.navercorp.nid.NaverIdLoginSDK

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        initNaverLoginSDK(this)

        setContent {
            val context = LocalContext.current
            val launcher = rememberNaverLoginLauncher()

            Column {
                Box(modifier = Modifier.height(200.dp)) {}
                Button(onClick = {
                    NaverIdLoginSDK.authenticate(context, launcher)
                }) {
                    Text("네이버로 로그인")
                }
            }

        }
    }
}

fun initNaverLoginSDK(activity: Activity) {

    NaverIdLoginSDK.initialize(
        context = activity.applicationContext,
        clientId = BuildConfig.NAVER_CLIENT_ID,
        clientSecret = BuildConfig.NAVER_CLIENT_SECRET,
        clientName = BuildConfig.APPLICATION_ID
    )
}

@Composable
fun rememberNaverLoginLauncher(): ActivityResultLauncher<Intent> {

    return rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val accessToken = NaverIdLoginSDK.getAccessToken()

            Log.d("NAVER_LOGIN", "✅ 로그인 성공")
            Log.d("NAVER_LOGIN", "AccessToken: $accessToken")

            sendAccessTokenToFunctions(accessToken)
        } else {
            val code = NaverIdLoginSDK.getLastErrorCode().code
            val desc = NaverIdLoginSDK.getLastErrorDescription()

            Log.e("NAVER_LOGIN", "❌ 로그인 실패")
            Log.e("NAVER_LOGIN", "Error Code: $code")
            Log.e("NAVER_LOGIN", "Error Desc: $desc")
        }
    }
}

fun sendAccessTokenToFunctions(accessToken: String?) {
    if (!accessToken.isNullOrEmpty()) {
        val functions = Firebase.functions("asia-northeast3")

        val data = hashMapOf(
            "accessToken" to accessToken
        )

        functions
            .getHttpsCallable("loginWithNaver")
            .call(data)

            .addOnSuccessListener { result ->
                val customToken = result.data as String
                FirebaseAuth.getInstance().signInWithCustomToken(customToken)
                    .addOnSuccessListener {
                        Log.d("NAVER_LOGIN", "✅ Firebase 세션 로그인 성공")
                    }
                    .addOnFailureListener { e ->
                        Log.e("NAVER_LOGIN", "❌ Firebase 세션 로그인 실패", e)
                    }
            }
            .addOnFailureListener { e ->
                Log.e("NAVER_LOGIN", "❌ Functions 호출 실패", e)
            }
    } else {
        Log.e("NAVER_LOGIN", "❌ Access Token이 없습니다.")
    }

}
