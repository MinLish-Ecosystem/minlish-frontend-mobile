package com.minlish.app.presentation.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.minlish.app.presentation.components.AppHeader
import com.minlish.app.presentation.components.Footer
import com.minlish.app.ui.theme.MlBackground

@Composable
fun ProfileScreen(
    currentRoute: String = "Profile",
    onNavigate: (String) -> Unit = {},
    onNotificationClick: () -> Unit = {}
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppHeader(
                userName = "QuangLe",
                userAvatarUrl = "https://api.dicebear.com/7.x/avataaars/png?seed=QuangLe",
                onNotificationClick = onNotificationClick
            )
        },
        bottomBar = {
            Footer(
                currentRoute = currentRoute,
                onNavigate = onNavigate
            )
        },
        containerColor = MlBackground
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Profile Screen",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "🚧 Coming soon",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
