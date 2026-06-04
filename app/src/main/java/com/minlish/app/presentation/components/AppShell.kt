package com.minlish.app.presentation.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.minlish.app.data.local.TokenManager
import com.minlish.app.presentation.navigation.AppNavHost
import com.minlish.app.presentation.navigation.NavDestinations
import com.minlish.app.presentation.navigation.clearAllTabStates
import com.minlish.app.presentation.screens.profile.ProfileViewModel
import com.minlish.app.util.NotificationBadgeManager
import com.minlish.app.util.SessionExpiredEvent

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppShell(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = NavDestinations.Welcome.route,
) {
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStack?.destination?.route ?: NavDestinations.Learning.route
    val context = LocalContext.current

    val globalProfileViewModel: ProfileViewModel = viewModel()
    val unreadBadgeCount by NotificationBadgeManager.badgeCount.collectAsState()

    val isLoggedIn = TokenManager.isLoggedIn()
    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            NotificationBadgeManager.startPolling()
            globalProfileViewModel.loadProfileData()
        } else {
            NotificationBadgeManager.stopPolling()
        }
    }

    LaunchedEffect(Unit) {
        SessionExpiredEvent.flow.collect {
            globalProfileViewModel.resetState()
            navController.clearAllTabStates()
            navController.navigate(NavDestinations.Login.route) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    val showBars = currentRoute in listOf(
        NavDestinations.Learning.route,
        NavDestinations.Analytics.route,
        NavDestinations.Library.route,
        NavDestinations.Practice.route,
        NavDestinations.Profile.route
    )

    fun onFooterNavigate(label: String) {
        navController.navigate(label) {
            popUpTo(navController.graph.startDestinationId) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            if (showBars) {
                AppHeader(
                    unreadCount = unreadBadgeCount,
                    onNotificationClick = {
                        navController.navigate(NavDestinations.Notifications.route)
                    },
                    onUserClick = {
                        onFooterNavigate(NavDestinations.Profile.route)
                    },
                    viewModel = globalProfileViewModel
                )
            }
        },
        bottomBar = {
            if (showBars) {
                Footer(
                    currentRoute = currentRoute,
                    onNavigate = ::onFooterNavigate
                )
            }
        }
    ) { innerPadding ->
        AppNavHost(
            navController = navController,
            startDestination = startDestination,
            profileViewModel = globalProfileViewModel,
            modifier = if (showBars) Modifier.padding(innerPadding) else Modifier,
        )
    }
}