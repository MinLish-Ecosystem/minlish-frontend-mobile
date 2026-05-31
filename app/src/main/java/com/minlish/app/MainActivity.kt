package com.minlish.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.minlish.app.presentation.components.AppHeader
import com.minlish.app.presentation.screens.vocab.CreateNewSetScreen
import com.minlish.app.presentation.screens.vocab.MyLibraryScreen
import com.minlish.app.presentation.screens.vocab.addMockSet
import com.minlish.app.ui.components.Footer
import com.minlish.app.ui.theme.MinLishMobileTheme

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

// ─────────────────────────────────────────────────────────────────────────────
// Màn hình chính — điều hướng đơn giản bằng state
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun MainScreen() {
    // Tab đang chọn trên bottom nav
    var currentRoute by remember { mutableStateOf("Library") }

    // Điều hướng nội bộ trong tab Library:
    //   "list"   → MyLibraryScreen
    //   "create" → CreateNewSetScreen
    var librarySubRoute by remember { mutableStateOf("list") }

    // Khi mở CreateNewSet: ẩn header + footer, hiện màn hình full
    val isFullScreen = currentRoute == "Library" && librarySubRoute == "create"

    if (isFullScreen) {
        CreateNewSetScreen(
            onBackClick = { librarySubRoute = "list" },
            onCreateClick = { title, description, category, isPublic ->
                // ── MOCK: thêm set mới vào danh sách cục bộ ─────────────────
                // TODO: Thay bằng gọi API khi backend sẵn sàng:
                //   vocabViewModel.createSet(title, description, category, isPublic)
                addMockSet(title, description, category, isPublic)
                librarySubRoute = "list"
            }
        )
        return
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppHeader(
                userName = "MinLish",
                userAvatarUrl = "https://api.dicebear.com/7.x/avataaars/png?seed=MinLish",
                onNotificationClick = {}
            )
        },
        bottomBar = {
            Footer(
                currentRoute = currentRoute,
                onNavigate = { newRoute ->
                    currentRoute = newRoute
                    if (newRoute == "Library") librarySubRoute = "list"
                }
            )
        }
    ) { innerPadding ->
        when (currentRoute) {
            "Library" -> {
                MyLibraryScreen(
                    onCreateSetClick = { librarySubRoute = "create" },
                    onSetClick = { /* TODO: mở chi tiết set */ },
                    modifier = Modifier.padding(innerPadding)
                )
            }
            else -> {
                Text(
                    text = "Bạn đang ở trang: $currentRoute",
                    modifier = Modifier.padding(innerPadding).padding(16.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MinLishMobileTheme { MainScreen() }
}