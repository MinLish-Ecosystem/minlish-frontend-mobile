package com.minlish.app.presentation.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.minlish.app.data.local.TokenManager
import com.minlish.app.presentation.screens.analytics.AnalyticsScreen
import com.minlish.app.presentation.screens.analytics.AnalyticsViewModel
import com.minlish.app.presentation.screens.auth.*
import com.minlish.app.presentation.screens.learning.LearningDashBoardScreen
import com.minlish.app.presentation.screens.practice.PracticeArenaScreen
import com.minlish.app.presentation.screens.profile.ProfileScreen
import com.minlish.app.presentation.screens.welcome.LearningGoalScreen
import com.minlish.app.presentation.screens.welcome.WelcomeScreen

@Composable
fun AuthNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = NavDestinations.Welcome.route
) {
    // 1. Lắng nghe route hiện tại từ backstack để truyền xuống các Footer
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStack?.destination?.route ?: NavDestinations.Learning.route

    // 2. Hàm điều hướng chung từ Footer - Do Footer và Route đã đồng bộ chữ thường
    fun onFooterNavigate(label: String) {
        navController.navigate(label) {
            // Quay về Learning (màn hình gốc) để tránh phình to backstack
            popUpTo(NavDestinations.Learning.route) { saveState = true }
            launchSingleTop = true
            restoreState    = true
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(NavDestinations.Welcome.route) {
            WelcomeScreen(
                onGetStartedClick = {
                    navController.navigate(NavDestinations.Register.route)
                },
                onLogInClick = {
                    navController.navigate(NavDestinations.Login.route)
                }
            )
        }

        composable(NavDestinations.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    // Đăng nhập thành công -> Vào thẳng Learning (màn hình chính học tập)
                    navController.navigate(NavDestinations.Learning.route) {
                        popUpTo(NavDestinations.Welcome.route) { inclusive = true }
                    }
                },
                onGoogleSignInClick = {},
                onForgotPasswordClick = {
                    navController.navigate(NavDestinations.ForgotPassword.route)
                },
                onSignUpClick = {
                    navController.navigate(NavDestinations.Register.route)
                }
            )
        }

        composable(NavDestinations.Register.route) {
            RegisterScreen(
                onRegisterSuccess = { email ->
                    navController.navigate("${NavDestinations.VerifyEmail.route}/$email")
                },
                onSignInClick = {
                    navController.navigate(NavDestinations.Login.route)
                }
            )
        }

        composable(
            route = NavDestinations.VerifyEmail.route, // Sử dụng route chuẩn từ NavDestinations
            arguments = listOf(
                navArgument("email") {type = NavType.StringType}
            )
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            VerifyEmailScreen(
                email = email,
                onBackClick = { navController.popBackStack() },
                onVerifyClick = {
                    navController.navigate(NavDestinations.LearningGoal.route) {
                        popUpTo(NavDestinations.Welcome.route) {
                            inclusive = true
                        }
                    }
                },
                onChangeEmail = { navController.popBackStack() },
                onResendCode = { }
            )
        }

        composable(NavDestinations.ForgotPassword.route) {
            ForgotPasswordScreen(
                onBackClick = { navController.popBackStack()},
                onSendResetLink = { email ->
                    // Sử dụng helper createRoute để truyền email an toàn
                    navController.navigate(NavDestinations.VerifyEmail.createRoute(email))
                },
                onReturnToLogin = {
                    navController.navigate(NavDestinations.Login.route) {
                        popUpTo(NavDestinations.Login.route) { inclusive = true}
                    }
                }
            )
        }

        composable(NavDestinations.LearningGoal.route) {
            LearningGoalScreen(
                onBackClick = { navController.popBackStack()},
                onContinueClick = {
                    // Chọn mục tiêu xong -> Vào thẳng Learning học tập
                    navController.navigate(NavDestinations.Learning.route) {
                        popUpTo(NavDestinations.LearningGoal.route) { inclusive = true }
                    }
                }
            )
        }

        // ── Main App Tabs (Liên kết chặt chẽ với Footer Navigation) ──────────

        // 1. Tab chính Learning
        composable(NavDestinations.Learning.route) {
            LearningDashBoardScreen(
                currentRoute = currentRoute,
                onNavigate = ::onFooterNavigate
            )
        }

        // 2. Tab Analytics
        composable(NavDestinations.Analytics.route) {
            AnalyticsScreen(
                currentRoute = currentRoute,
                onNavigate = ::onFooterNavigate,
                viewModel = viewModel<AnalyticsViewModel>()
            )
        }

        // 3. Tab Practice Arena
        composable(NavDestinations.Practice.route) {
            PracticeArenaScreen(
                currentRoute = currentRoute,
                onNavigate = ::onFooterNavigate
            )
        }

        // 4. Tab Profile
        composable(NavDestinations.Profile.route) {
            ProfileScreen(
                currentRoute = currentRoute,
                onNavigate = ::onFooterNavigate,
                onNotificationClick = {},
                onLogOutClick = {
                    TokenManager.clear()
                    navController.navigate(NavDestinations.Welcome.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}
