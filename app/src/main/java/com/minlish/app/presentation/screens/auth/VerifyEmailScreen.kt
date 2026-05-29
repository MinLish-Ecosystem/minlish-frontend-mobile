package com.minlish.app.presentation.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun VerifyEmailScreen(
    email: String = "",
    onBackClick: () -> Unit = {},
    onVerifyClick: () -> Unit = {},
    onChangeEmail: () -> Unit = {},
    onResendCode: () -> Unit = {}
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Verify Your Email",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Sent code to: $email",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onVerifyClick,
                modifier = Modifier.width(200.dp)
            ) {
                Text("Verify")
            }
            Spacer(modifier = Modifier.height(12.dp))
            TextButton(onClick = onChangeEmail) {
                Text("Change Email")
            }
            Spacer(modifier = Modifier.height(6.dp))
            TextButton(onClick = onResendCode) {
                Text("Resend Code")
            }
            Spacer(modifier = Modifier.height(12.dp))
            TextButton(onClick = onBackClick) {
                Text("Back")
            }
        }
    }
}
