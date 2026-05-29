package com.minlish.app.presentation.screens.welcome

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LearningGoalScreen(
    onBackClick: () -> Unit = {},
    onContinueClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Choose Your Learning Goal",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onContinueClick,
                modifier = Modifier.width(200.dp)
            ) {
                Text("Continue")
            }
            Spacer(modifier = Modifier.height(12.dp))
            TextButton(onClick = onBackClick) {
                Text("Back")
            }
        }
    }
}
