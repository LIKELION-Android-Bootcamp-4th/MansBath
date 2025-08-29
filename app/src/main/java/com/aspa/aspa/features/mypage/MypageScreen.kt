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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.aspa.aspa.core.constants.enums.Provider
import com.aspa.aspa.features.login.AuthViewModel
import com.aspa.aspa.features.login.LogoutState
import com.aspa.aspa.features.login.WithdrawState
import com.aspa.aspa.features.login.navigation.LoginDestinations
import com.aspa.aspa.features.mypage.components.ConfirmDialog
import com.aspa.aspa.features.mypage.components.DialogType
import com.aspa.aspa.ui.theme.AppSpacing
import com.aspa.aspa.ui.theme.logout
import com.aspa.aspa.ui.theme.withdraw

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPageScreen(
    rootNavController: NavHostController,
    innerNavController: NavHostController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var dialogType by remember { mutableStateOf(DialogType.NONE) }
    val nickname by authViewModel.nicknameState.collectAsState()
    val providerState by authViewModel.providerState.collectAsState()
    val logoutState by authViewModel.logoutState.collectAsState()
    val withdrawState by authViewModel.withdrawState.collectAsState()

    LaunchedEffect(Unit) {
        authViewModel.getNickname()
        authViewModel.getProvider()
    }

    LaunchedEffect(logoutState, withdrawState) {
        when (logoutState) {
            LogoutState.Idle -> {}
            LogoutState.Success -> {
                rootNavController.navigate(LoginDestinations.LOGIN_GRAPH_ROUTE) {
                    popUpTo(0)
                }
                Toast.makeText(context, "로그아웃 완료", Toast.LENGTH_SHORT).show()
            }
            is LogoutState.Error -> {
                Toast.makeText(context, (logoutState as LogoutState.Error).message, Toast.LENGTH_SHORT).show()
            }
        }

        when (withdrawState) {
            WithdrawState.Idle -> {}
            WithdrawState.Success -> {
                rootNavController.navigate(LoginDestinations.LOGIN_GRAPH_ROUTE) {
                    popUpTo(0)
                }
                Toast.makeText(context, "회원탈퇴 완료", Toast.LENGTH_SHORT).show()
            }
            is WithdrawState.Error -> {
                Toast.makeText(context, (withdrawState as WithdrawState.Error).message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
    ) {
        //프로필 설정
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = AppSpacing.md, horizontal = AppSpacing.md),
            shape = MaterialTheme.shapes.large,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        ) {
            Row(
                modifier = Modifier.padding(horizontal = AppSpacing.lg, vertical = AppSpacing.lg),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "프로필",
                    modifier = Modifier.size(60.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Column(modifier = Modifier.padding(horizontal = AppSpacing.md)) {
                    Text(
                        text = nickname,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        when(providerState) {
                            Provider.GOOGLE -> "구글 계정으로 가입"
                            Provider.KAKAO -> "카카오 계정으로 가입"
                            Provider.NAVER -> "네이버 계정으로 가입"
                            null ->"조회 중.."
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        // 로그아웃 버튼
        Button(
            onClick = {
                dialogType = DialogType.LOGOUT
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppSpacing.xl, vertical = AppSpacing.md),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.logout,
                contentColor = MaterialTheme.colorScheme.onError
            ),
            shape = MaterialTheme.shapes.medium
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.Logout,
                    contentDescription = "로그아웃",
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.onError
                )
                Spacer(modifier = Modifier.width(AppSpacing.sm))
                Text(
                    text = "로그아웃",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
        // 회원탈퇴 버튼
        Button(
            onClick = {
                dialogType = DialogType.WITHDRAW
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppSpacing.xl, vertical = AppSpacing.md),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.withdraw,
                contentColor = MaterialTheme.colorScheme.onError
            ),
            shape = MaterialTheme.shapes.medium
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.NoAccounts,
                    contentDescription = "회원탈퇴",
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.onError
                )
                Spacer(modifier = Modifier.width(AppSpacing.sm))
                Text(
                    text = "회원탈퇴",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }

    when (dialogType) {
        DialogType.LOGOUT -> {
            ConfirmDialog(
                text = "로그아웃",
                onDismiss = { dialogType = DialogType.NONE },
                onConfirm = {
                    authViewModel.signOut(context)
                    authViewModel.resetLogoutState()
                },
            )
        }

        DialogType.WITHDRAW -> {
            ConfirmDialog(
                text = "회원탈퇴",
                onDismiss = { dialogType = DialogType.NONE },
                onConfirm = {
                    authViewModel.withdraw(context)
                    authViewModel.resetWithdrawState()
                },
            )
        }

        DialogType.NONE -> {}
    }
}