package com.minlish.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.minlish.app.presentation.components.AppHeader
import com.minlish.app.ui.components.Footer
import com.minlish.app.ui.theme.MinLishMobileTheme
import com.minlish.app.presentation.screens.learning.HeroSessionCard
import com.minlish.app.presentation.screens.learning.LearningDashBoardScreen
import com.minlish.app.presentation.screens.learning.flashCardScreen
import com.minlish.app.presentation.screens.practice.PracticeArenaScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MinLishMobileTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    flashCardScreen()
}