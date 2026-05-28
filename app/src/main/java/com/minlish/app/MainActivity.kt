package com.minlish.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.minlish.app.ui.theme.MinLishMobileTheme
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import com.minlish.app.presentation.components.AppHeader
import com.minlish.app.presentation.navigation.AuthNavHost
import com.minlish.app.ui.components.Footer
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MinLishMobileTheme {
                AuthNavHost(
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
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
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar={
            AppHeader(
                userName = "QuangLe",
                userAvatarUrl = "https://api.dicebear.com/7.x/avataaars/png?seed=QuangLe",
                onNotificationClick = {

                }
            )
        },
        bottomBar = {
            Footer(
                currentRoute = currentRoute,
                onNavigate = { newRoute ->
                    currentRoute = newRoute
                }
            )
        }
    ){innerPadding ->
        // Phần nội dung chính của màn hình (ở giữa)
        // innerPadding giúp nội dung không bị che khuất bởi Footer
        Text(
            text = "Bạn đang ở trang: $currentRoute",
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MinLishMobileTheme {
        Greeting("Android")
    }
}