package com.minlish.app.presentation.screens.auth.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.minlish.app.ui.theme.MinlishError
import com.minlish.app.ui.theme.MinlishOnSurfaceVariant
import com.minlish.app.ui.theme.MinlishOutline
import com.minlish.app.ui.theme.MinlishSuccess

@Composable
private fun ChecklistItem(
    text: String,
    checked: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = if (checked)
                Icons.Outlined.CheckCircle
            else
                Icons.Outlined.RadioButtonUnchecked,
            contentDescription = null,
            tint = if (checked) MinlishSuccess else MinlishError,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = text,
            fontSize = 14.sp,
            color = if (checked) MinlishSuccess else MinlishOutline
        )
    }
}

@Composable
fun SecurityCheckList(
    hasMinLength: Boolean,
    hasUppercase: Boolean,
    hasNumberOrSymbol: Boolean
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFF9FAFB)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Password must contain: ",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = MinlishOnSurfaceVariant
            )
            ChecklistItem(
                text = "At least 8 characters",
                checked = hasMinLength
            )
            ChecklistItem(
                text = "At least 1 uppercase letter (A..Z)",
                checked = hasUppercase
            )
            ChecklistItem(
                text = "At least 1 number or symbol (1..9)",
                checked = hasNumberOrSymbol
            )
        }
    }
}