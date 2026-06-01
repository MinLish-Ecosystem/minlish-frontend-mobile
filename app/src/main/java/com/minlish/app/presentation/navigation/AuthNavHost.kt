package com.minlish.app.presentation.navigation

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import com.minlish.app.presentation.screens.notifications.NotificationScreen
import com.minlish.app.presentation.screens.notifications.NotificationViewModel
import com.minlish.app.presentation.screens.practice.PracticeArenaScreen
import com.minlish.app.presentation.screens.profile.ProfileScreen
import com.minlish.app.presentation.screens.profile.ProfileViewModel
import com.minlish.app.presentation.screens.welcome.LearningGoalScreen
import com.minlish.app.presentation.screens.welcome.WelcomeScreen
import com.minlish.app.util.FCMHelper
import com.minlish.app.util.NotificationBadgeManager

@Composable
fun AuthNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = NavDestinations.Welcome.route
) {
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStack?.destination?.route ?: NavDestinations.Learning.route
    val context = LocalContext.current

    // ── Lắng nghe Badge Count sạch sẽ từ Singleton Manager ──
    val unreadBadgeCount by NotificationBadgeManager.badgeCount.collectAsState()

    // Quản lý vòng đời Polling: start khi đã đăng nhập, stop khi logout
    val isLoggedIn = TokenManager.isLoggedIn()
    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            NotificationBadgeManager.startPolling()
        } else {
            NotificationBadgeManager.stopPolling()
        }
    }

    fun onFooterNavigate(label: String) {
        navController.navigate(label) {
            popUpTo(NavDestinations.Learning.route) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // ── Auth screens — giữ nguyên 100% ──

        composable(NavDestinations.Welcome.route) {
            WelcomeScreen(
                onGetStartedClick = { navController.navigate(NavDestinations.Register.route) },
                onLogInClick = { navController.navigate(NavDestinations.Login.route) }
            )
        }

        composable(NavDestinations.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(NavDestinations.Learning.route) {
                        popUpTo(NavDestinations.Welcome.route) { inclusive = true }
                    }
                },
                onGoogleSignInClick = {},
                onForgotPasswordClick = { navController.navigate(NavDestinations.ForgotPassword.route) },
                onSignUpClick = { navController.navigate(NavDestinations.Register.route) }
            )
        }

        composable(NavDestinations.Register.route) {
            RegisterScreen(
                onRegisterSuccess = { email ->
                    navController.navigate("${NavDestinations.VerifyEmail.route}/$email")
                },
                onSignInClick = { navController.navigate(NavDestinations.Login.route) }
            )
        }

        composable(
            route = NavDestinations.VerifyEmail.route,
            arguments = listOf(navArgument("email") { type = NavType.StringType })
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            VerifyEmailScreen(
                email = email,
                onBackClick = { navController.popBackStack() },
                onVerifyClick = {
                    navController.navigate(NavDestinations.LearningGoal.route) {
                        popUpTo(NavDestinations.Welcome.route) { inclusive = true }
                    }
                },
                onChangeEmail = { navController.popBackStack() },
                onResendCode = { }
            )
        }

        composable(NavDestinations.ForgotPassword.route) {
            ForgotPasswordScreen(
                onBackClick = { navController.popBackStack() },
                onSendResetLink = { email ->
                    navController.navigate(NavDestinations.VerifyEmail.createRoute(email))
                },
                onReturnToLogin = {
                    navController.navigate(NavDestinations.Login.route) {
                        popUpTo(NavDestinations.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(NavDestinations.LearningGoal.route) {
            LearningGoalScreen(
                onBackClick = { navController.popBackStack() },
                onContinueClick = {
                    navController.navigate(NavDestinations.Learning.route) {
                        popUpTo(NavDestinations.LearningGoal.route) { inclusive = true }
                    }
                }
            )
        }

        // ── Main App Tabs ──

        // 1. Tab Learning
        composable(NavDestinations.Learning.route) {
            // Tự động đăng ký thiết bị với server qua Helper cực gọn
            LaunchedEffect(Unit) {
                FCMHelper.registerFCMToken(context)
            }

            LearningDashBoardScreen(
                currentRoute = currentRoute,
                onNavigate = ::onFooterNavigate,
                unreadCount = unreadBadgeCount,
                onNotificationClick = {
                    navController.navigate(NavDestinations.Notifications.route)
                }
            )
        }

        // 2. Tab Analytics
        composable(NavDestinations.Analytics.route) {
            AnalyticsScreen(
                currentRoute = currentRoute,
                onNavigate = ::onFooterNavigate,
                viewModel = viewModel<AnalyticsViewModel>(),
                unreadCount = unreadBadgeCount,
                onNotificationClick = {
                    navController.navigate(NavDestinations.Notifications.route)
                }
            )
        }

        // 3. Tab Practice
        composable(NavDestinations.Practice.route) {
            PracticeArenaScreen(
                currentRoute = currentRoute,
                onNavigate = ::onFooterNavigate,
                unreadCount = unreadBadgeCount,
                onNotificationClick = {
                    navController.navigate(NavDestinations.Notifications.route)
                }
            )
        }

        // 4. Tab Profile
        composable(NavDestinations.Profile.route) {
            val profileViewModel = viewModel<ProfileViewModel>()
            ProfileScreen(
                currentRoute = currentRoute,
                onNavigate = ::onFooterNavigate,
                viewModel = profileViewModel,
                unreadCount = unreadBadgeCount,
                onNotificationClick = {
                    navController.navigate(NavDestinations.Notifications.route)
                },
                onLogOutClick = {
                    // Huỷ token trên server trước, sau đó xoá TokenManager local để logout
                    FCMHelper.deleteFCMToken(context) {
                        TokenManager.clear()
                        navController.navigate(NavDestinations.Welcome.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }
            )
        }

        // 5. Màn hình Notifications
        composable(NavDestinations.Notifications.route) {
            val notifViewModel = viewModel<NotificationViewModel>()
            NotificationScreen(
                onBackClick = { navController.popBackStack() },
                viewModel = notifViewModel,
                onNotificationClick = { notif ->
                    when (notif.type) {
                        "daily_reminder", "review_due" -> {
                            navController.navigate(NavDestinations.Practice.route) {
                                popUpTo(NavDestinations.Learning.route)
                            }
                        }
                        "streak_milestone", "achievement" -> {
                            navController.navigate(NavDestinations.Analytics.route) {
                                popUpTo(NavDestinations.Learning.route)
                            }
                        }
                    }
                    // Làm tươi nóng badge ngay lập tức
                    NotificationBadgeManager.refreshBadge()
                }
            )
        }
    }
}
