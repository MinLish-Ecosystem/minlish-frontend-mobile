package com.minlish.app.presentation.screens.auth.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.minlish.app.ui.theme.MinlishOnSurface
import com.minlish.app.ui.theme.MinlishOutlineVariant
import com.minlish.app.ui.theme.MinlishPrimary

@Composable
fun OtpInputField(
    otpValue: String,
    onOtpChange: (String) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Box(contentAlignment = Alignment.Center) {
        BasicTextField(
            value = otpValue,
            onValueChange = { input ->
                val filtered = input.filter { it.isDigit() }.take(6)
                onOtpChange(filtered)
            },
            modifier = Modifier
                .size(1.dp)
                .focusRequester(focusRequester),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword
            ),
            cursorBrush = SolidColor(Color.Transparent)
        )
    }

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        repeat(6) {index ->
            val char = otpValue.getOrNull(index)
            val isFocused = index == otpValue.length

            Box(
                modifier = Modifier
                    .size(width = 44.dp, height = 52.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White)
                    .border(
                        width = if (isFocused) 2.dp else 1.dp,
                        color = when {
                            isFocused -> MinlishPrimary
                            char != null -> MinlishPrimary.copy(alpha = 0.5f)
                            else -> MinlishOutlineVariant
                        },
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (char != null) {
                    Text(
                        text = char.toString(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MinlishOnSurface
                    )
                }
                if (isFocused && char == null) {
                    Box(
                        modifier = Modifier
                            .width(2.dp)
                            .height(24.dp)
                            .background(MinlishPrimary)
                    )
                }
            }
        }
    }
}