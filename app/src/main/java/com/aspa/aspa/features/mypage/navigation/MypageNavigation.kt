package com.aspa.aspa.features.mypage.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.aspa.aspa.features.mypage.MyPageScreen

object MypageDestination {
    const val MYPAGE_GRAPH_ROUTE = "mypage_graph"
    const val MYPAGE = "mypage"
}
fun NavGraphBuilder.mypageGraph(rootNavController: NavHostController,innerNavController: NavHostController) {
    navigation(
        startDestination = MypageDestination.MYPAGE,
        route = MypageDestination.MYPAGE_GRAPH_ROUTE
    ) {
        composable(MypageDestination.MYPAGE) {
            MyPageScreen(rootNavController, innerNavController)
        }
    }
}
