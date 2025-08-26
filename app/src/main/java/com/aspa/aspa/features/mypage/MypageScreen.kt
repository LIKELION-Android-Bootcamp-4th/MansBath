package com.aspa.aspa.features.mypage

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.outlined.NoAccounts
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.aspa.aspa.features.login.AuthViewModel
import com.aspa.aspa.features.login.LogoutState
import com.aspa.aspa.features.login.WithdrawState
import com.aspa.aspa.features.login.navigation.LoginDestinations
import com.aspa.aspa.ui.theme.Gray10

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPageScreen(
    rootNavController: NavHostController,
    innerNavController: NavHostController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val provider by authViewModel.providerState.collectAsState()
    val logoutState by authViewModel.logoutState.collectAsState()
    val withdrawState by authViewModel.withdrawState.collectAsState()

    LaunchedEffect(Unit) {
        authViewModel.getProvider()
    }

    LaunchedEffect(logoutState, withdrawState) {
        when (logoutState) {
            LogoutState.Idle -> {}

            LogoutState.Success -> {
                rootNavController.navigate(LoginDestinations.LOGIN_GRAPH_ROUTE) {
                    popUpTo(0)
                }
            }

            is LogoutState.Error -> {
                Toast.makeText(
                    context,
                    (logoutState as LogoutState.Error).message,
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }

        when (withdrawState) {
            WithdrawState.Idle -> {}

            WithdrawState.Success -> {
                rootNavController.navigate(LoginDestinations.LOGIN_GRAPH_ROUTE) {
                    popUpTo(0)
                }
                Toast.makeText(context, "회원탈퇴 성공", Toast.LENGTH_SHORT)
                    .show()
            }

            is WithdrawState.Error -> {
                Toast.makeText(
                    context,
                    (withdrawState as WithdrawState.Error).message,
                    Toast.LENGTH_SHORT
                )
                    .show()
            }

        }
    }

    Column(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
    ) {
        //프로필 설정
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 12.dp),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, Gray10),
            colors = CardDefaults.cardColors(containerColor = Color.White),
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 15.dp, vertical = 15.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "이미지 아이콘",
                    modifier = Modifier.size(60.dp),
                    tint = Color.Black
                )
                Column(modifier = Modifier.padding(horizontal = 10.dp)) {
                    Text(
                        "닉네임",
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 15.sp,
                        color = Color.Black
                    )
                    Text(
                        provider,
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
        }
        // 로그아웃 버튼
        Button(
            onClick = {
                authViewModel.signOut(context)
                authViewModel.resetLogoutState()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 15.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(5.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.Logout,
                    contentDescription = "로그아웃",
                    modifier = Modifier.size(20.dp),
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "로그아웃",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Button(
            onClick = {
                authViewModel.withdraw(context)
                authViewModel.resetWithdrawState()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 15.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Magenta,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(5.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.NoAccounts,
                    contentDescription = "회원탈퇴",
                    modifier = Modifier.size(20.dp),
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "회원탈퇴",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}