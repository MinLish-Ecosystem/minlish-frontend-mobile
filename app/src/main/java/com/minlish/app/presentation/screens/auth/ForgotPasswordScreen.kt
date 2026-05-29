package com.minlish.app.presentation.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ForgotPasswordScreen(
    onBackClick: () -> Unit = {},
    onSendResetLink: (String) -> Unit = {},
    onReturnToLogin: () -> Unit = {}
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Forgot Password",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { onSendResetLink("user@example.com") },
                modifier = Modifier.width(200.dp)
            ) {
                Text("Send Reset Link")
            }
            Spacer(modifier = Modifier.height(12.dp))
            TextButton(onClick = onReturnToLogin) {
                Text("Return to Login")
            }
            Spacer(modifier = Modifier.height(12.dp))
            TextButton(onClick = onBackClick) {
                Text("Back")
            }
        }
    }
}
