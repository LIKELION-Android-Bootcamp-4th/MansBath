package com.aspa.aspa.features.mypage

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.outlined.NoAccounts
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.aspa.aspa.features.login.AuthViewModel
import com.aspa.aspa.features.login.LogoutState
import com.aspa.aspa.features.login.WithdrawState
import com.aspa.aspa.features.login.navigation.LoginDestinations
import com.aspa.aspa.features.mypage.components.ConfirmDialog
import com.aspa.aspa.features.mypage.components.DialogType
import com.aspa.aspa.ui.theme.AppSpacing

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
    val provider by authViewModel.providerState.collectAsState()
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
                        text = provider,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
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
        }

        Button(
            onClick = {
                authViewModel.signOut(context)
                authViewModel.resetLogoutState()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppSpacing.xl, vertical = AppSpacing.md),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error,
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

        Button(
            onClick = {
                authViewModel.withdraw(context)
                authViewModel.resetWithdrawState()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppSpacing.xl, vertical = AppSpacing.md),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error,
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
}
