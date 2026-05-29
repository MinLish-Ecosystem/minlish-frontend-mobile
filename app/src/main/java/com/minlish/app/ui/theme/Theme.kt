package com.minlish.app.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext

// Dark scheme giữ màu baseline (chưa thiết kế dark mode cho MinLish)
private val DarkColorScheme = darkColorScheme(
    primary   = Purple80,
    secondary = PurpleGrey80,
    tertiary  = Pink80
)

// Light scheme dùng màu MinLish
private val MinLishLightColorScheme = lightColorScheme(
    primary              = MinlishPrimary,
    onPrimary            = MinlishSurfaceLowest,
    primaryContainer     = MinlishSecondaryContainer,
    onPrimaryContainer   = MinlishInverseSurface,
    inversePrimary       = MinlishInversePrimary,
    secondary            = MinlishSecondary,
    onSecondary          = MinlishOnSecondary,
    secondaryContainer   = MinlishSecondaryContainer,
    onSecondaryContainer = MinlishOnSecondaryContainer,
    tertiary             = MinlishTertiary,
    onTertiary           = MinlishOnTertiary,
    tertiaryContainer    = MinlishTertiaryContainer,
    background           = MinlishSurface,
    onBackground         = MinlishOnSurface,
    surface              = MinlishSurface,
    onSurface            = MinlishOnSurface,
    surfaceVariant       = MinlishSurfaceHighest,
    onSurfaceVariant     = MinlishOnSurfaceVariant,
    outline              = MinlishOutline,
    outlineVariant       = MinlishOutlineVariant,
    inverseSurface       = MinlishInverseSurface,
    inverseOnSurface     = MinlishInverseOnSurface,
    error                = MinlishError,
)

// Gradient Brush dùng chung — TopBar, Auth, Welcome, Profile, Analytics
val MinlishGradient = Brush.linearGradient(
    colors = listOf(MinlishGradientStart, MinlishGradientEnd)
)

@Composable
fun MinLishMobileTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else      -> MinLishLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography  = Typography,
        content     = content
    )
}