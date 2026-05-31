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
<<<<<<< Updated upstream
=======
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import com.minlish.app.presentation.components.AppHeader
import com.minlish.app.ui.components.Footer
import com.minlish.app.presentation.screens.library.LibraryScreen
import com.minlish.app.presentation.screens.library.WordListScreen
>>>>>>> Stashed changes

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TokenManager.init(applicationContext)
        enableEdgeToEdge()
        setContent {
            MinLishMobileTheme {
                AuthNavHost(
                    modifier = Modifier.fillMaxSize(),
                    startDestination = "analytics"
                )
            }
        }
    }
<<<<<<< Updated upstream
=======
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}
@Composable
fun MainScreen(){
    var currentRoute by remember { mutableStateOf("Analytics") }
    var selectedSet by remember { mutableStateOf<String?>(null) }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar={
            if (selectedSet == null) {
                AppHeader(
                    userName = "QuangLe",
                    userAvatarUrl = "https://api.dicebear.com/7.x/avataaars/png?seed=QuangLe",
                    onNotificationClick = {

                    }
                )
            }
        },
        bottomBar = {
            Footer(
                currentRoute = currentRoute,
                onNavigate = { newRoute ->
                    currentRoute = newRoute
                    selectedSet = null
                }
            )
        }
    ){innerPadding ->
        // Phần nội dung chính của màn hình (ở giữa)
        // innerPadding giúp nội dung không bị che khuất bởi Footer
        when (currentRoute) {
            "Library" -> {
                val currentSelectedSet = selectedSet
                if (currentSelectedSet != null) {
                    WordListScreen(
                        setName = currentSelectedSet,
                        onBack = { selectedSet = null },
                        modifier = Modifier.padding(innerPadding)
                    )
                } else {
                    LibraryScreen(
                        onSetClick = { selectedSet = it },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
            else -> {
                Text(
                    text = "Bạn đang ở trang: $currentRoute",
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(16.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MinLishMobileTheme {
        Greeting("Android")
    }
>>>>>>> Stashed changes
}