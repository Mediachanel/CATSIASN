package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.data.model.UserRole
import com.example.ui.screens.AdminDashboardScreen
import com.example.ui.screens.BookmarkScreen
import com.example.ui.screens.CatExamScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LeaderboardScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.PackageSelectScreen
import com.example.ui.screens.ReviewExamScreen
import com.example.ui.screens.ScoreAnalysisScreen
import com.example.ui.screens.StudyMaterialScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.ExamViewModel
import com.example.ui.viewmodel.MainViewModel

object AppRoutes {
    const val HOME = "home"
    const val LOGIN = "login"
    const val ADMIN_DASHBOARD = "admin_dashboard"
    const val PACKAGE_SELECT = "package_select"
    const val CAT_EXAM = "cat_exam/{packageId}"
    const val SCORE_ANALYSIS = "score_analysis/{resultId}"
    const val REVIEW_EXAM = "review_exam/{resultId}"
    const val LEADERBOARD = "leaderboard"
    const val STUDY_MATERIAL = "study_material"
    const val BOOKMARKS = "bookmarks"

    fun catExamRoute(packageId: String) = "cat_exam/$packageId"
    fun scoreAnalysisRoute(resultId: Long) = "score_analysis/$resultId"
    fun reviewExamRoute(resultId: Long) = "review_exam/$resultId"
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CatCpnsAppNavigation()
                }
            }
        }
    }
}

@Composable
fun CatCpnsAppNavigation() {
    val navController = rememberNavController()
    val mainViewModel: MainViewModel = viewModel()
    val examViewModel: ExamViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = AppRoutes.HOME
    ) {
        composable(AppRoutes.HOME) {
            HomeScreen(
                viewModel = mainViewModel,
                onStartSimulation = { packageId ->
                    navController.navigate(AppRoutes.catExamRoute(packageId))
                },
                onSelectPackage = {
                    navController.navigate(AppRoutes.PACKAGE_SELECT)
                },
                onViewResult = { resultId ->
                    navController.navigate(AppRoutes.scoreAnalysisRoute(resultId))
                },
                onOpenLeaderboard = {
                    navController.navigate(AppRoutes.LEADERBOARD)
                },
                onOpenStudyMaterial = {
                    navController.navigate(AppRoutes.STUDY_MATERIAL)
                },
                onOpenBookmarks = {
                    navController.navigate(AppRoutes.BOOKMARKS)
                },
                onOpenLogin = {
                    navController.navigate(AppRoutes.LOGIN)
                },
                onOpenAdminDashboard = {
                    navController.navigate(AppRoutes.ADMIN_DASHBOARD)
                }
            )
        }

        composable(AppRoutes.PACKAGE_SELECT) {
            PackageSelectScreen(
                viewModel = mainViewModel,
                onSelectPackage = { packageId ->
                    navController.navigate(AppRoutes.catExamRoute(packageId)) {
                        popUpTo(AppRoutes.HOME)
                    }
                },
                onBackNavigate = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = AppRoutes.CAT_EXAM,
            arguments = listOf(navArgument("packageId") { type = NavType.StringType })
        ) { backStackEntry ->
            val packageId = backStackEntry.arguments?.getString("packageId") ?: "pkg_full_skd_1"
            CatExamScreen(
                viewModel = examViewModel,
                packageId = packageId,
                onFinishNavigate = { resultId ->
                    navController.navigate(AppRoutes.scoreAnalysisRoute(resultId)) {
                        popUpTo(AppRoutes.HOME)
                    }
                },
                onBackNavigate = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = AppRoutes.SCORE_ANALYSIS,
            arguments = listOf(navArgument("resultId") { type = NavType.LongType })
        ) { backStackEntry ->
            val resultId = backStackEntry.arguments?.getLong("resultId") ?: 0L
            ScoreAnalysisScreen(
                viewModel = mainViewModel,
                resultId = resultId,
                onReviewQuestions = { resId ->
                    navController.navigate(AppRoutes.reviewExamRoute(resId))
                },
                onRetakeExam = { packageId ->
                    navController.navigate(AppRoutes.catExamRoute(packageId)) {
                        popUpTo(AppRoutes.HOME)
                    }
                },
                onBackToHome = {
                    navController.navigate(AppRoutes.HOME) {
                        popUpTo(AppRoutes.HOME) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = AppRoutes.REVIEW_EXAM,
            arguments = listOf(navArgument("resultId") { type = NavType.LongType })
        ) { backStackEntry ->
            val resultId = backStackEntry.arguments?.getLong("resultId") ?: 0L
            ReviewExamScreen(
                viewModel = mainViewModel,
                resultId = resultId,
                onBackNavigate = {
                    navController.popBackStack()
                }
            )
        }

        composable(AppRoutes.LEADERBOARD) {
            LeaderboardScreen(
                viewModel = mainViewModel,
                onBackNavigate = {
                    navController.popBackStack()
                }
            )
        }

        composable(AppRoutes.STUDY_MATERIAL) {
            StudyMaterialScreen(
                viewModel = mainViewModel,
                onBackNavigate = {
                    navController.popBackStack()
                }
            )
        }

        composable(AppRoutes.BOOKMARKS) {
            BookmarkScreen(
                viewModel = mainViewModel,
                onBackNavigate = {
                    navController.popBackStack()
                }
            )
        }

        composable(AppRoutes.LOGIN) {
            LoginScreen(
                viewModel = mainViewModel,
                onLoginSuccess = { role ->
                    if (role == UserRole.ADMIN) {
                        navController.navigate(AppRoutes.ADMIN_DASHBOARD) {
                            popUpTo(AppRoutes.LOGIN) { inclusive = true }
                        }
                    } else {
                        navController.navigate(AppRoutes.HOME) {
                            popUpTo(AppRoutes.LOGIN) { inclusive = true }
                        }
                    }
                },
                onBackNavigate = {
                    navController.popBackStack()
                }
            )
        }

        composable(AppRoutes.ADMIN_DASHBOARD) {
            AdminDashboardScreen(
                viewModel = mainViewModel,
                onSwitchToUserMode = {
                    mainViewModel.switchRole(UserRole.USER)
                    navController.navigate(AppRoutes.HOME)
                },
                onLogout = {
                    navController.navigate(AppRoutes.LOGIN) {
                        popUpTo(AppRoutes.HOME) { inclusive = true }
                    }
                }
            )
        }
    }
}
