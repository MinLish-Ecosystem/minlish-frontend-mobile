package com.minlish.app.presentation.components
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.minlish.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String = "MinLish",
    onBackClick: (() -> Unit)
    ) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                style = LocalTextStyle.current.copy(brush = MinlishGradient),
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Outlined.ArrowBack,
                    contentDescription = "Go Back",
                    tint = MinlishOnSurfaceVariant
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MinlishSurface.copy(alpha = 0.8f)
        )
    )
}