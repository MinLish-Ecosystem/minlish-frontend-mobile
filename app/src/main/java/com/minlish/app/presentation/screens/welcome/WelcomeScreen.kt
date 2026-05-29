package com.minlish.app.presentation.screens.welcome

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun WelcomeScreen(
    onGetStartedClick: () -> Unit = {},
    onLogInClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Welcome to MinLish!",
                style = MaterialTheme.typography.headlineLarge
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onGetStartedClick,
                modifier = Modifier.width(200.dp)
            ) {
                Text("Get Started")
            }
            Spacer(modifier = Modifier.height(12.dp))
            TextButton(onClick = onLogInClick) {
                Text("Log In")
            }
        }
    }
}
