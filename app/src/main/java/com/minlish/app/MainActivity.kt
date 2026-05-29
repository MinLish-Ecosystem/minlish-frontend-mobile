package com.minlish.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.minlish.app.data.local.TokenManager
import com.minlish.app.presentation.navigation.AuthNavHost
import com.minlish.app.ui.theme.MinLishMobileTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TokenManager.init(applicationContext)
        enableEdgeToEdge()
        setContent {
            MinLishMobileTheme {
                AuthNavHost(
                    modifier = Modifier.fillMaxSize(),
                    startDestination = "login"
                )
            }
        }
    }
}