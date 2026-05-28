package com.minlish.app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import com.minlish.app.presentation.screens.auth.*
import com.minlish.app.presentation.screens.welcome.LearningGoalScreen
import com.minlish.app.presentation.screens.profile.ProfileScreen
import com.minlish.app.presentation.screens.welcome.WelcomeScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun AuthNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = NavDestinations.Welcome.route
) {
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
                    navController.navigate(NavDestinations.Profile.route) {
                        popUpTo(NavDestinations.Welcome.route) {inclusive = true}
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
                onRegisterSuccess = {
                    navController.navigate(
                        NavDestinations.LearningGoal.route
                    )
                },
                onSignInClick = {
                    navController.navigate(NavDestinations.Login.route)
                }
            )
        }

        composable(
            route = "${NavDestinations.VerifyEmail.route}/{email}",
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
                    navController.navigate("${NavDestinations.VerifyEmail.route}/$email")
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
                    navController.navigate(NavDestinations.Dashboard.route) {
                        popUpTo(NavDestinations.LearningGoal.route) { inclusive = true}
                    }
                }
            )
        }

        composable(NavDestinations.Dashboard.route) {

        }

        composable(NavDestinations.Profile.route) {
            ProfileScreen(
                onNotificationClick = { }
            )
        }
    }
}
