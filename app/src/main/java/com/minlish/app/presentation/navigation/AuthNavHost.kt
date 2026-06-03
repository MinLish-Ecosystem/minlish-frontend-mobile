package com.minlish.app.presentation.navigation

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.minlish.app.data.local.TokenManager
import com.minlish.app.presentation.screens.analytics.AnalyticsScreen
import com.minlish.app.presentation.screens.analytics.AnalyticsViewModel
import com.minlish.app.presentation.screens.auth.*
import com.minlish.app.presentation.screens.auth.viewmodels.AuthViewModel
import com.minlish.app.presentation.screens.auth.viewmodels.ForgetPasswordViewModel
import com.minlish.app.presentation.screens.learning.FlashCardScreen
import com.minlish.app.presentation.screens.learning.FlashcardViewModel
import com.minlish.app.presentation.screens.learning.LearningDashBoardScreen
import com.minlish.app.presentation.screens.learning.LearningViewModel
import com.minlish.app.presentation.screens.notifications.NotificationScreen
import com.minlish.app.presentation.screens.notifications.NotificationViewModel
import com.minlish.app.presentation.screens.library.LibraryScreen
import com.minlish.app.presentation.screens.library.WordListScreen
import com.minlish.app.presentation.screens.vocab.CreateNewSetScreen
import com.minlish.app.presentation.screens.practice.PracticeArenaScreen
import com.minlish.app.presentation.screens.practice.PracticeViewModel
import com.minlish.app.presentation.screens.practice.getRoute
import com.minlish.app.presentation.screens.profile.ProfileScreen
import com.minlish.app.presentation.screens.profile.ProfileViewModel
import com.minlish.app.presentation.screens.welcome.LearningGoalScreen
import com.minlish.app.presentation.screens.welcome.WelcomeScreen
import com.minlish.app.util.FCMHelper
import com.minlish.app.util.NotificationBadgeManager

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AuthNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = NavDestinations.Welcome.route,
) {
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStack?.destination?.route ?: NavDestinations.Learning.route
    val context = LocalContext.current
    val profileViewModel: ProfileViewModel = viewModel()
    val profileState by profileViewModel.uiState.collectAsState()
    val learningViewModel: LearningViewModel = viewModel()
    val practiceViewModel: PracticeViewModel = viewModel()
    val flashCardViewModel: FlashcardViewModel = viewModel()

    // ── Lắng nghe Badge Count sạch sẽ từ Singleton Manager ──
    val unreadBadgeCount by NotificationBadgeManager.badgeCount.collectAsState()

    // Quản lý vòng đời Polling: start khi đã đăng nhập, stop khi logout
    val isLoggedIn = TokenManager.isLoggedIn()
    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            NotificationBadgeManager.startPolling()
            profileViewModel.loadProfileData()
        } else {
            NotificationBadgeManager.stopPolling()
            TokenManager.clear()
        }
    }

    fun onFooterNavigate(label: String) {
        navController.navigate(label) {
            popUpTo(NavDestinations.Learning.route) { saveState = true }
            launchSingleTop = true
        }
    }
    val authViewModel: AuthViewModel = viewModel()
    val forgotPasswordViewModel: ForgetPasswordViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // ── Auth screens — giữ nguyên 100% ──

        composable(NavDestinations.Welcome.route) {
            WelcomeScreen(
                onGetStartedClick = { navController.navigate(NavDestinations.Login.route) },
            )
        }

        composable(NavDestinations.Login.route) {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate(NavDestinations.Learning.route) {
                        popUpTo(NavDestinations.Welcome.route) { inclusive = true }
                    }
                },
                onGoogleSignInClick = {},
                onForgotPasswordClick = { navController.navigate(NavDestinations.ForgotPassword.route) },
                onSignUpClick = {
                    authViewModel.resetViewModel()
                    navController.navigate(NavDestinations.Register.route)
                }
            )
        }

        composable(NavDestinations.Register.route) {
            RegisterScreen(
                viewModel = authViewModel,
                onRegisterSuccess = {
                    navController.navigate(NavDestinations.VerifyEmail.route)
                },
                onSignInClick = {
                    authViewModel.resetViewModel()
                    navController.navigate(NavDestinations.Login.route)
                }
            )
        }

        composable(NavDestinations.VerifyEmail.route) {
            VerifyEmailScreen(
                viewModel = authViewModel,
                onBackClick = { navController.popBackStack() },
                onVerifyEmailSuccess = {
                    navController.navigate(NavDestinations.LearningGoal.route) {
                        popUpTo(NavDestinations.Welcome.route) { inclusive = true }
                    }
                },
                onChangeEmail = { navController.popBackStack() },
            )
        }

        composable(NavDestinations.ForgotPassword.route) {
            ForgotPasswordScreen(
                viewModel = forgotPasswordViewModel,
                onBackClick = { navController.popBackStack()},
                onSendResetSuccess = {
                    navController.navigate(NavDestinations.ResetPassword.route)
                },
                onReturnToLogin = {
                    navController.navigate(NavDestinations.Login.route) {
                        popUpTo(NavDestinations.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(NavDestinations.ResetPassword.route) {
            ResetPasswordScreen(
                viewModel = forgotPasswordViewModel,
                onBackClick = { navController.popBackStack() },
                onResetPasswordSuccess = {
                    navController.navigate(NavDestinations.Login.route) {
                        popUpTo(NavDestinations.Login.route) {inclusive = true}
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
            LaunchedEffect(Unit) {
                FCMHelper.registerFCMToken(context)
            }

            LearningDashBoardScreen(
                currentRoute = currentRoute,
                onNavigate = ::onFooterNavigate,
                unreadCount = unreadBadgeCount,
                onNotificationClick = {
                    navController.navigate(NavDestinations.Notifications.route)
                },
                onUserClick = {
                    navController.navigate(NavDestinations.Profile.route)
                },
                viewModel = learningViewModel,
                onCreateNewSetCard = {
                    navController.navigate(NavDestinations.CreateNewSet.route)
                },
                onStartSessionClick = {
                    navController.navigate(NavDestinations.FlashCardTest.route)
                }
            )
        }
        composable(NavDestinations.FlashCardTest.route){
            LaunchedEffect(Unit) {
                FCMHelper.registerFCMToken(context)
            }
            FlashCardScreen(
                viewModel=flashCardViewModel,
                onExitClick = {
                    navController.popBackStack()
                },
                onMoreClick = {}
            )
        }

        composable(NavDestinations.Analytics.route) {
            AnalyticsScreen(
                currentRoute = currentRoute,
                onNavigate = ::onFooterNavigate,
                viewModel = viewModel<AnalyticsViewModel>(),
                unreadCount = unreadBadgeCount,
                onNotificationClick = {
                    navController.navigate(NavDestinations.Notifications.route)
                },
                onUserClick = {
                    navController.navigate(NavDestinations.Profile.route)
                }
            )
        }

        // 3. Tab Library
        composable(NavDestinations.Library.route) {
            LibraryScreen(
                currentRoute      = currentRoute,
                onNavigate        = ::onFooterNavigate,
                onSetClick        = { setName ->
                    navController.navigate(NavDestinations.WordList.createRoute(setName))
                },
                onCreateNewSet    = {
                    navController.navigate(NavDestinations.CreateNewSet.route)
                },
                onNotificationClick = {
                    navController.navigate(NavDestinations.Notifications.route)
                },
                onUserClick = {
                    navController.navigate(NavDestinations.Profile.route)
                }
            )
        }

        // Sub-screen Word List
        composable(NavDestinations.WordList.route) { backStackEntry ->
            val setName = backStackEntry.arguments?.getString("setName")?.let {
                java.net.URLDecoder.decode(it, "UTF-8")
            } ?: "Vocabulary Set"

            WordListScreen(
                setName = setName,
                onBack  = { navController.popBackStack() }
            )
        }

        // Sub-screen Create New Set
        composable(NavDestinations.CreateNewSet.route) {
            CreateNewSetScreen(
                onBackClick   = { navController.popBackStack() },
                onCreateClick = { _, _, _, _ ->
                    // TODO: gọi API tạo set, sau đó quay về Library
                    navController.popBackStack()
                }
            )
        }

        composable(NavDestinations.Practice.route) {
            PracticeArenaScreen(
                currentRoute = currentRoute,
                onNavigate = ::onFooterNavigate,
                userName = profileState.displayName.ifEmpty { "User" },
                userAvatarUrl = profileState.avatar ?: "",
                unreadCount = unreadBadgeCount,
                onNotificationClick = {
                    navController.navigate(NavDestinations.Notifications.route)
                },
                onUserClick = {
                    navController.navigate(NavDestinations.Profile.route)
                },
                viewModel = practiceViewModel,
                onPracticeClick = { mode ->
                    val route = mode.getRoute()
                    if (route != null) {
                        navController.navigate(route)
                    } else {
                        android.util.Log.w("PracticeArena", "Unknown practice mode type: ${mode.id}")
                    }
                }
            )
        }

        // 4. Tab Profile
        composable(NavDestinations.Profile.route) {
            ProfileScreen(
                currentRoute = currentRoute,
                onNavigate = ::onFooterNavigate,
                viewModel = profileViewModel,
                unreadCount = unreadBadgeCount,
                onNotificationClick = {
                    navController.navigate(NavDestinations.Notifications.route)
                },
                onLogOutClick = {
                    FCMHelper.deleteFCMToken(context) {
                        TokenManager.clear()
                    }
                    profileViewModel.resetViewModel()
                    navController.navigate(NavDestinations.Welcome.route) {
                        popUpTo(0) { inclusive = true }
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

