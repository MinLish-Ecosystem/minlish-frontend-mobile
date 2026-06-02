package com.minlish.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.minlish.app.data.local.TokenManager
import com.minlish.app.data.sync.SyncManager
import com.minlish.app.presentation.navigation.AuthNavHost
import com.minlish.app.presentation.navigation.NavDestinations
import com.minlish.app.ui.theme.MinLishMobileTheme
import com.minlish.app.util.SessionExpiredEvent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TokenManager.init(applicationContext)
        enableEdgeToEdge()
        setContent {
            MinLishMobileTheme {
                val navController = rememberNavController()
                val syncManager = SyncManager(this)

                LaunchedEffect(Unit) {
                    if (TokenManager.isLoggedIn()) {
                        syncManager.syncAll()
                    }
                }

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
}