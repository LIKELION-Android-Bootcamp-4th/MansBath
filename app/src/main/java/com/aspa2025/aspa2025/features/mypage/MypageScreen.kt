package com.aspa2025.aspa2025.features.mypage

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.automirrored.outlined.OpenInNew
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.NoAccounts
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.aspa2025.aspa2025.core.constants.enums.Provider
import com.aspa2025.aspa2025.features.login.AuthViewModel
import com.aspa2025.aspa2025.features.login.LogoutState
import com.aspa2025.aspa2025.features.login.WithdrawState
import com.aspa2025.aspa2025.features.login.navigation.LoginDestinations
import com.aspa2025.aspa2025.features.mypage.components.ConfirmDialog
import com.aspa2025.aspa2025.features.mypage.components.DialogType
import com.aspa2025.aspa2025.ui.theme.AppSpacing

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
        
        // 메뉴 항목들
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppSpacing.xl, vertical = AppSpacing.md)
        ) {

            // 개인정보 처리 방침
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        val intent = Intent(Intent.ACTION_VIEW,
                            "https://branch-run-aae.notion.site/Aspa-25e987807fdd8001aa75fc9d1c4cc77a".toUri())
                        context.startActivity(intent)
                    }
                    .padding(vertical = AppSpacing.md),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.OpenInNew,
                    contentDescription = "개인정보 처리 방침",
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(AppSpacing.md))
                Text(
                    text = "개인정보 처리 방침",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.Outlined.ChevronRight,
                    contentDescription = "이동",
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // 로그아웃
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        dialogType = DialogType.LOGOUT
                    }
                    .padding(vertical = AppSpacing.md),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.Logout,
                    contentDescription = "로그아웃",
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(AppSpacing.md))
                Text(
                    text = "로그아웃",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.Outlined.ChevronRight,
                    contentDescription = "이동",
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            // 회원탈퇴
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        dialogType = DialogType.WITHDRAW
                    }
                    .padding(vertical = AppSpacing.md),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.NoAccounts,
                    contentDescription = "회원탈퇴",
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.width(AppSpacing.md))
                Text(
                    text = "회원탈퇴",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.Outlined.ChevronRight,
                    contentDescription = "이동",
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.error
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

@Preview(showBackground = true)
@Composable
fun MyPageScreenPreview() {
    MaterialTheme {
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
                            text = "사용자 닉네임",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "구글 계정으로 가입",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            
            // 메뉴 항목들
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppSpacing.xl, vertical = AppSpacing.md)
            ) {

                // 개인정보 처리 방침
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = AppSpacing.md),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.OpenInNew,
                        contentDescription = "개인정보 처리 방침",
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(AppSpacing.md))
                    Text(
                        text = "개인정보 처리 방침",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = Icons.Outlined.ChevronRight,
                        contentDescription = "이동",
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // 로그아웃
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = AppSpacing.md),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.Logout,
                        contentDescription = "로그아웃",
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(AppSpacing.md))
                    Text(
                        text = "로그아웃",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = Icons.Outlined.ChevronRight,
                        contentDescription = "이동",
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                // 회원탈퇴
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = AppSpacing.md),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.NoAccounts,
                        contentDescription = "회원탈퇴",
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.width(AppSpacing.md))
                    Text(
                        text = "회원탈퇴",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = Icons.Outlined.ChevronRight,
                        contentDescription = "이동",
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}
