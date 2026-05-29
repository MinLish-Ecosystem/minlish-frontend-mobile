package com.minlish.app.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

// Dark scheme giữ màu cũ (chưa thiết kế dark mode cho MinLish)
private val DarkColorScheme = darkColorScheme(
    primary   = Purple80,
    secondary = PurpleGrey80,
    tertiary  = Pink80
)

// Light scheme dùng màu MinLish
private val MinLishLightColorScheme = lightColorScheme(
    primary              = MlPrimary,
    onPrimary            = MlOnPrimary,
    primaryContainer     = MlPrimaryContainer,
    onPrimaryContainer   = MlOnPrimaryContainer,
    inversePrimary       = MlInversePrimary,
    secondary            = MlSecondary,
    onSecondary          = MlOnSecondary,
    secondaryContainer   = MlSecondaryContainer,
    onSecondaryContainer = MlOnSecondaryContainer,
    tertiary             = MlTertiary,
    onTertiary           = MlOnTertiary,
    tertiaryContainer    = MlTertiaryContainer,
    background           = MlBackground,
    onBackground         = MlOnBackground,
    surface              = MlSurface,
    onSurface            = MlOnSurface,
    surfaceVariant       = MlSurfaceVariant,
    onSurfaceVariant     = MlOnSurfaceVariant,
    outline              = MlOutline,
    outlineVariant       = MlOutlineVariant,
    inverseSurface       = MlInverseSurface,
    inverseOnSurface     = MlInverseOnSurface,
    error                = MlError,
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