package com.minlish.app.presentation.navigation

sealed class NavDestinations(val route: String) {
    data object Welcome: NavDestinations("welcome")
    data object Login: NavDestinations("login")
    data object Register: NavDestinations("register")
    data object VerifyEmail: NavDestinations("verify_email/{email}") {
        fun createRoute(email: String) = "verify_email/$email"
    }
    data object ForgotPassword: NavDestinations("forgot_password")
    data object ResetPassword: NavDestinations("reset_password")

    data object LearningGoal: NavDestinations("learning_goal")
    data object Dashboard: NavDestinations("dashboard")
    data object Profile: NavDestinations("profile")
}