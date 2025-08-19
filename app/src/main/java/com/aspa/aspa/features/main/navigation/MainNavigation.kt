package com.aspa.aspa.features.main.navigation

object MainDestinations { const val GRAPH = "main_graph" }

enum class BottomTab(val route: String) {
    Home("home"), Roadmap("roadmap?questionId={questionId}"), Quiz("quiz"), MyPage("mypage")
}

//fun NavGraphBuilder.mainGraph(navController: NavController) {
//    navigation(
//        startDestination = BottomTab.Home.route,
//        route = MainDestinations.GRAPH
//    ) {
//        homeGraph(navController = navController, homeViewModel = HomeViewModel())
//        roadmapGraph(navController = navController)
//        quizGraph(navController = navController)
//        mypageGraph(navController = navController)
//    }
//}