package com.aspa2025.aspa2025.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ===== Brand Colors =====
private val BrandBlue = Color(0xFF2B7FFF)      // Primary
private val BrandLavender = Color(0xFFC0C7FF)  // Secondary
private val BrandBackground = Color(0xFFFFFFFF)

// === Action Colors ===
val ColorScheme.logout: Color
    @Composable get() = BrandLogout
val ColorScheme.withdraw: Color
    @Composable get() = BrandWithdraw

val BrandLogout = Color(0xFF607D8B)   // 로그아웃 버튼
val BrandWithdraw = Color(0xFFD32F2F)    // 회원탈퇴 버튼

object AppSpacing {
    val xs = 4.dp
    val sm = 8.dp
    val md = 12.dp
    val lg = 16.dp
    val xl = 20.dp
}


val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp), // 작은 chip, tag
    small = RoundedCornerShape(12.dp), // 작은 카드/리스트 아이템
    medium = RoundedCornerShape(16.dp), // 기본 카드/시트(권장 기본)
    large = RoundedCornerShape(20.dp), // 강조 카드/상세 카드
    extraLarge = RoundedCornerShape(28.dp) // BottomSheet, 대형 컨테이너
)


private val BaseTypography = Typography()

val AppTypography = BaseTypography.copy(
    titleLarge = BaseTypography.titleLarge.copy(fontSize = 22.sp, fontWeight = FontWeight.SemiBold),
    titleMedium = BaseTypography.titleMedium.copy(fontSize = 18.sp, fontWeight = FontWeight.SemiBold),
    titleSmall = BaseTypography.titleSmall.copy(fontSize = 16.sp, fontWeight = FontWeight.Medium),

    bodyLarge = BaseTypography.bodyLarge.copy(fontSize = 16.sp, lineHeight = 22.sp),
    bodyMedium = BaseTypography.bodyMedium.copy(fontSize = 14.sp, lineHeight = 20.sp),
    bodySmall = BaseTypography.bodySmall.copy(fontSize = 12.sp, lineHeight = 18.sp),

    labelLarge = BaseTypography.labelLarge.copy(fontSize = 14.sp, fontWeight = FontWeight.Medium),
    labelMedium = BaseTypography.labelMedium.copy(fontSize = 12.sp, fontWeight = FontWeight.Medium),
    labelSmall = BaseTypography.labelSmall
)
// ===== Light Color Scheme =====
private val LightColors = lightColorScheme(
    primary = BrandBlue,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE3F0FF),      // 밝은 블루 톤 컨테이너
    onPrimaryContainer = Color(0xFF001B3F),

    secondary = BrandLavender,
    onSecondary = Color(0xFF1C1B1F),
    secondaryContainer = Color(0xFFE6E8FF),
    onSecondaryContainer = Color(0xFF121536),

    // tertiary는 브랜드 2색 체계 기준으로 보조 포인트 컬러를 살짝 가미
    tertiary = Color(0xFF5E6AD2),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFE3E6FF),
    onTertiaryContainer = Color(0xFF111A3A),

    background = BrandBackground,
    onBackground = Color(0xFF1B1B1F),

    surface = Color.White,
    onSurface = Color(0xFF1B1B1F),

    surfaceVariant = Color(0xFFE7E9F2),
    onSurfaceVariant = Color(0xFF44474F),
    outline = Color(0xFF75777F)
)

// ===== Dark Color Scheme =====
// 브랜드 아이덴티티를 유지하면서 가독성/대비 확보용으로 톤만 낮췄습니다.
private val DarkColors = darkColorScheme(
    primary = Color(0xFF9CC6FF),
    onPrimary = Color(0xFF00315E),
    primaryContainer = Color(0xFF004A8E),
    onPrimaryContainer = Color(0xFFD2E4FF),

    secondary = Color(0xFFE0E4FF),
    onSecondary = Color(0xFF23263A),
    secondaryContainer = Color(0xFF3C4060),
    onSecondaryContainer = Color(0xFFE0E4FF),

    tertiary = Color(0xFFBAC2FF),
    onTertiary = Color(0xFF222A59),
    tertiaryContainer = Color(0xFF3C4476),
    onTertiaryContainer = Color(0xFFDEE1FF),

    background = Color(0xFF121316),
    onBackground = Color(0xFFE3E2E6),

    surface = Color(0xFF121316),
    onSurface = Color(0xFFE3E2E6),

    surfaceVariant = Color(0xFF424654),
    onSurfaceVariant = Color(0xFFC3C6D3),
    outline = Color(0xFF8D90A0)
)
@Composable
fun AspaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // 브랜드 컬러 고정권장: 다이내믹 컬러를 켜면 시스템 팔레트로 바뀌니 기본 false
    useDynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colors,
        typography = MaterialTheme.typography, // 기본값 사용 (원하면 교체)
        shapes = MaterialTheme.shapes,         // 기본값 사용 (원하면 교체)
        content = content
    )
}