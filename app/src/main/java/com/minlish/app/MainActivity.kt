package com.minlish.app

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.minlish.app.data.local.TokenManager
import com.minlish.app.di.DatabaseModule
import com.minlish.app.presentation.navigation.AuthNavHost
import com.minlish.app.presentation.navigation.NavDestinations
import com.minlish.app.ui.theme.MinLishMobileTheme
import com.minlish.app.util.SessionExpiredEvent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TokenManager.init(applicationContext)
        DatabaseModule.init(applicationContext)
        requestNotificationPermissionIfNeeded()
        enableEdgeToEdge()
        setContent {
            MinLishMobileTheme {
                val navController = rememberNavController()
                LaunchedEffect(Unit) {
                    SessionExpiredEvent.flow.collect {
                        navController.navigate(NavDestinations.Login.route) {
                            popUpTo(0) { inclusive = true}
                        }
                    }
                }

                AuthNavHost(
                    navController = navController,
                    modifier = Modifier.fillMaxSize(),
                    startDestination = if (TokenManager.isLoggedIn())
                        NavDestinations.Learning.route
                    else
                        NavDestinations.Welcome.route
                )
            }
        }
    }

    private fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS)
                != android.content.pm.PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
            }
        }
    }
}