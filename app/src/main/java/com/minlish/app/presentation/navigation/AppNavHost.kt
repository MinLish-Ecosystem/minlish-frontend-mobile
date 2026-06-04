package com.minlish.app.presentation.navigation

import android.content.Intent
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.minlish.app.GoogleSignInActivity
import com.minlish.app.presentation.screens.analytics.AnalyticsScreen
import com.minlish.app.presentation.screens.analytics.AnalyticsViewModel
import com.minlish.app.presentation.screens.auth.*
import com.minlish.app.presentation.screens.auth.viewmodels.ForgetPasswordViewModel
import com.minlish.app.presentation.screens.auth.viewmodels.RegisterViewModel
import com.minlish.app.presentation.screens.learning.FlashCardScreen
import com.minlish.app.presentation.screens.learning.FlashcardViewModel
import com.minlish.app.presentation.screens.learning.LearningDashBoardScreen
import com.minlish.app.presentation.screens.learning.LearningViewModel
import com.minlish.app.presentation.screens.notifications.NotificationScreen
import com.minlish.app.presentation.screens.notifications.NotificationViewModel
import com.minlish.app.presentation.screens.library.LibraryScreen
import com.minlish.app.presentation.screens.library.WordListScreen
import com.minlish.app.presentation.screens.library.WordListViewModel
import com.minlish.app.presentation.screens.vocab.CreateNewSetScreen
import com.minlish.app.presentation.screens.vocab.VocabViewModel
import com.minlish.app.presentation.screens.vocab.AddWordScreen
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
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String,
    profileViewModel: ProfileViewModel
) {
    val context = LocalContext.current
    val regisViewModel: RegisterViewModel = viewModel()
    val forgotPasswordViewModel: ForgetPasswordViewModel = viewModel()

    val googleLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {}

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(NavDestinations.Welcome.route) {
            WelcomeScreen(
                onGetStartedClick = { navController.navigate(NavDestinations.Login.route) },
            )
        }

        composable(NavDestinations.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(NavDestinations.Learning.route) {
                        popUpTo(NavDestinations.Welcome.route) { inclusive = true }
                    }
                },
                onGoogleSignInClick = {
                    googleLauncher.launch(
                        Intent(context, GoogleSignInActivity::class.java)
                    )
                },
                onForgotPasswordClick = { navController.navigate(NavDestinations.ForgotPassword.route) },
                onSignUpClick = { navController.navigate(NavDestinations.Register.route) }
            )
        }

        composable(NavDestinations.Register.route) {
            RegisterScreen(
                viewModel = regisViewModel,
                onRegisterSuccess = {
                    navController.navigate(NavDestinations.VerifyEmail.route)
                },
                onSignInClick = { navController.navigate(NavDestinations.Login.route) }
            )
        }

        composable(NavDestinations.VerifyEmail.route) {
            VerifyEmailScreen(
                viewModel = regisViewModel,
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

        composable(
            route = NavDestinations.FlashCardTest.route,
            arguments = listOf(
                androidx.navigation.navArgument("setId") {
                    type = androidx.navigation.NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ){ backStackEntry ->
            val setId = backStackEntry.arguments?.getString("setId")
            val flashCardViewModel: FlashcardViewModel = viewModel()
            LaunchedEffect(Unit) {
                FCMHelper.registerFCMToken(context)
            }
            FlashCardScreen(
                viewModel=flashCardViewModel,
                setId = setId,
                onExitClick = {
                    navController.popBackStack(NavDestinations.Learning.route, inclusive = false)
                    flashCardViewModel.resetState()
                },
                onMoreClick = {}
            )
        }

        composable(NavDestinations.Analytics.route) {
            val analyticsViewModel: AnalyticsViewModel = viewModel()
            AnalyticsScreen(
                viewModel = analyticsViewModel
            )
        }

        composable(NavDestinations.Library.route) {
            val vocabViewModel: VocabViewModel = viewModel()
            LibraryScreen(
                onSetClick = { setId, setName ->
                    navController.navigate(NavDestinations.WordList.createRoute(setId, setName))
                },
                onCreateNewSet = {
                    navController.navigate(NavDestinations.CreateNewSet.route)
                },
                viewModel = vocabViewModel
            )
        }

        composable(NavDestinations.WordList.route) { backStackEntry ->
            val setId = backStackEntry.arguments?.getString("setId") ?: ""
            val setName = backStackEntry.arguments?.getString("setName")?.let {
                java.net.URLDecoder.decode(it, "UTF-8")
            } ?: "Vocabulary Set"

            WordListScreen(
                setId = setId,
                setName = setName,
                onBack  = { navController.popBackStack() },
                onStartSession = { id ->
                    navController.navigate(NavDestinations.FlashCardTest.createRoute(id))
                },
                onAddWordClick = { id ->
                    navController.navigate(NavDestinations.AddWord.createRoute(id))
                }
            )
        }

        composable(NavDestinations.AddWord.route) { backStackEntry ->
            val setId = backStackEntry.arguments?.getString("setId") ?: ""
            val parentEntry = remember(backStackEntry) {
                navController.previousBackStackEntry
            }
            val wordListViewModel: WordListViewModel = if (parentEntry != null) {
                viewModel(parentEntry)
            } else {
                viewModel()
            }

            AddWordScreen(
                setId = setId,
                onBack = { navController.popBackStack() },
                viewModel = wordListViewModel
            )
        }

        composable(NavDestinations.CreateNewSet.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                try {
                    navController.getBackStackEntry(NavDestinations.Library.route)
                } catch (e: IllegalArgumentException) {
                    null
                }
            }
            val vocabViewModel: VocabViewModel = if (parentEntry != null) {
                viewModel(parentEntry)
            } else {
                viewModel()
            }

            val uiState by vocabViewModel.uiState.collectAsState()
            val context = LocalContext.current
            LaunchedEffect(uiState.createSuccess) {
                if (uiState.createSuccess) {
                    vocabViewModel.resetCreateSuccess()
                    navController.popBackStack()
                }
            }

            LaunchedEffect(uiState.errorMessage) {
                uiState.errorMessage?.let { msg ->
                    android.widget.Toast.makeText(context, msg, android.widget.Toast.LENGTH_LONG).show()
                    vocabViewModel.clearError()
                }
            }

            CreateNewSetScreen(
                onBackClick = { navController.popBackStack() },
                onCreateClick = { title, description, category, isPublic, words ->
                    vocabViewModel.createSet(title, description, category, isPublic, words)
                }
            )
        }

        // Tab Practice
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
            ProfileScreen(
                viewModel = profileViewModel,
                onLogOutSuccess = {
                    FCMHelper.deleteFCMToken(context) {
                        navController.clearAllTabStates()
                    }
                    navController.navigate(NavDestinations.Welcome.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

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