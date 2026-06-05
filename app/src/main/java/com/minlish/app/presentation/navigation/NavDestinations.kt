package com.minlish.app.presentation.navigation

import androidx.navigation.NavController

sealed class NavDestinations(val route: String) {
    data object Welcome        : NavDestinations("welcome")
    data object Login          : NavDestinations("login")
    data object Register       : NavDestinations("register")
    data object VerifyEmail    : NavDestinations("verify_email")
    data object ForgotPassword : NavDestinations("forgot_password")
    data object ResetPassword  : NavDestinations("reset_password")
    data object LearningGoal   : NavDestinations("learning_goal")
    data object Profile        : NavDestinations("profile")

    // ── Main app screens ─────────────────────────────────────────────────────
    data object Learning  : NavDestinations("learning")
    data object Analytics : NavDestinations("analytics")
    data object Library   : NavDestinations("library")
    data object Practice  : NavDestinations("practice")

    // ── Sub-screens ──────────────────────────────────────────────────────────
    data object WordList : NavDestinations("word_list/{setId}/{setName}?isReadOnly={isReadOnly}") {
        fun createRoute(setId: String, setName: String, isReadOnly: Boolean = false) =
            "word_list/$setId/${java.net.URLEncoder.encode(setName, "UTF-8")}?isReadOnly=$isReadOnly"
    }

    data object CreateNewSet : NavDestinations("create_new_set")
    data object AddWord : NavDestinations("add_word/{setId}") {
        fun createRoute(setId: String) = "add_word/$setId"
    }
    data object Notifications  : NavDestinations("notifications")
    data object FlashCardTest: NavDestinations("flash_card_test?setId={setId}") {
        fun createRoute(setId: String? = null) =
            if (setId != null) "flash_card_test?setId=$setId" else "flash_card_test"
    }
}

fun NavController.clearAllTabStates() {
    val tabs = listOf(
        NavDestinations.Learning.route,
        NavDestinations.Analytics.route,
        NavDestinations.Library.route,
        NavDestinations.Practice.route,
        NavDestinations.Profile.route
    )
    tabs.forEach { route ->
        try {
            clearBackStack(route)
        } catch (e: Exception) {
        }
    }
}
