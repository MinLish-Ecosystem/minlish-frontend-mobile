package com.minlish.app.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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
    navController: NavHostController,
    startDestination: String
) {
    val context = LocalContext.current
    val authViewModel: AuthViewModel = viewModel()
    val forgotPasswordViewModel: ForgetPasswordViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(NavDestinations.Welcome.route) {
            WelcomeScreen(
                onGetStartedClick = { navController.navigate(NavDestinations.Register.route) },
                onLogInClick = { navController.navigate(NavDestinations.Login.route) }
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
                onSignUpClick = { navController.navigate(NavDestinations.Register.route) }
            )
        }

        composable(NavDestinations.Register.route) {
            RegisterScreen(
                viewModel = authViewModel,
                onRegisterSuccess = {
                    navController.navigate(NavDestinations.VerifyEmail.route)
                },
                onSignInClick = { navController.navigate(NavDestinations.Login.route) }
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

        // ── Main App Tabs (ViewModels được khởi tạo cục bộ tại đây để tự hủy khi Logout) ──

        // 1. Tab Learning
        composable(NavDestinations.Learning.route) {
            val learningViewModel: LearningViewModel = viewModel()
            LaunchedEffect(Unit) {
                FCMHelper.registerFCMToken(context)
            }

            LearningDashBoardScreen(
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
            val flashCardViewModel: FlashcardViewModel = viewModel()
            LaunchedEffect(Unit) {
                FCMHelper.registerFCMToken(context)
            }
            FlashCardScreen(
                viewModel = flashCardViewModel,
                onExitClick = {
                    navController.popBackStack()
                },
                onMoreClick = {}
            )
        }

        // 2. Tab Analytics
        composable(NavDestinations.Analytics.route) {
            val analyticsViewModel: AnalyticsViewModel = viewModel()
            AnalyticsScreen(
                viewModel = analyticsViewModel
            )
        }

        // 3. Tab Library
        composable(NavDestinations.Library.route) {
            LibraryScreen(
                onSetClick = { setName ->
                    navController.navigate(NavDestinations.WordList.createRoute(setName))
                },
                onCreateNewSet = {
                    navController.navigate(NavDestinations.CreateNewSet.route)
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
                onBack = { navController.popBackStack() }
            )
        }

        // Sub-screen Create New Set
        composable(NavDestinations.CreateNewSet.route) {
            CreateNewSetScreen(
                onBackClick = { navController.popBackStack() },
                onCreateClick = { _, _, _, _ ->
                    navController.popBackStack()
                }
            )
        }

        // 4. Tab Practice
        composable(NavDestinations.Practice.route) {
            val practiceViewModel: PracticeViewModel = viewModel()
            PracticeArenaScreen(
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

        // 5. Tab Profile
        composable(NavDestinations.Profile.route) {
            val profileViewModel: ProfileViewModel = viewModel()
            ProfileScreen(
                viewModel = profileViewModel,
                onLogOutClick = {
                    FCMHelper.deleteFCMToken(context) {
                        TokenManager.clear()
                        navController.navigate(NavDestinations.Welcome.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }
            )
        }

        // 6. Màn hình Notifications
        composable(NavDestinations.Notifications.route) {
            val notifViewModel: NotificationViewModel = viewModel()
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
                    NotificationBadgeManager.refreshBadge()
                }
            )
        }
    }
}