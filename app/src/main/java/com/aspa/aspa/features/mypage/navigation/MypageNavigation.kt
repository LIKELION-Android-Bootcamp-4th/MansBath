package com.aspa.aspa.features.mypage.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.aspa.aspa.features.mypage.MyPageScreen


fun NavGraphBuilder.mypageGraph(navController: NavController) {
    composable("mypage") {
        MyPageScreen()
    }
}