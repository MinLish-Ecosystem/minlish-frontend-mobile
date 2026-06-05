package com.minlish.app.presentation.screens.auth

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.minlish.app.ui.theme.*
import com.minlish.app.presentation.components.TopBar
import com.minlish.app.presentation.screens.auth.viewmodels.ForgetPasswordUiEvent
import com.minlish.app.presentation.screens.auth.viewmodels.ForgetPasswordViewModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    viewModel: ForgetPasswordViewModel = viewModel(),
    onBackClick: () -> Unit = {},
    onSendResetSuccess: () -> Unit = {},
    onReturnToLogin: () -> Unit = {}
) {

    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val isError = state.email.isBlank()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is ForgetPasswordUiEvent.ForgotPasswordSuccess -> {
                    onSendResetSuccess()
                }
                is ForgetPasswordUiEvent.ShowError -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                }

                else -> {

                }
            }
        }
    }


    Scaffold(
        topBar = {
            TopBar(onBackClick = onBackClick)
        },
        containerColor = MinlishSurface
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .padding(top = 32.dp, bottom = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ForgotPasswordCard(
                isLoading = state.isLoading,
                email = state.email,
                onEmailChange = {viewModel.updateEmail(it)},
                onSendResetLink = {email ->
                    viewModel.forgotPassword(email)
                },
                onReturnToLogin = onReturnToLogin,
                isError = isError
            )
        }
    }
}

@Composable
private fun ForgotPasswordCard(
    isLoading: Boolean,
    email: String,
    onEmailChange: (String) -> Unit,
    onSendResetLink: (String) -> Unit,
    onReturnToLogin: () -> Unit,
    isError: Boolean
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MinlishSurfaceLowest,
        shadowElevation = 8.dp
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            ForgotPasswordHeader()
            ForgotPasswordEmailField(
                email = email,
                onEmailChange = onEmailChange
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .alpha(if (isError) 0.4f else 1f)
                    .background(MinlishGradient),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        onSendResetLink(email)
                    },
                    modifier = Modifier.fillMaxSize(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    elevation = null,
                    enabled = !isLoading && !isError
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White.copy(alpha = 0.5f),
                            strokeWidth = 2.5.dp
                        )
                    }
                    else {
                        Text(
                            text = "Send Reset Link",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White
                        )
                    }
                }
            }
        TextButton(
            onClick = onReturnToLogin
            ) {
                Text(
                    text = "Return to Log In",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MinlishPrimary,
                )
            }
        }
    }
}

@Composable
private fun ForgotPasswordHeader() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(Color(0xFFF0F4FF)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.LockReset,
                contentDescription = null,
                tint = MinlishPrimary,
                modifier = Modifier.size(32.dp)
            )
        }
    }

    Text(
        text = "Forget Password?",
        fontSize = 24.sp,
        fontWeight = FontWeight.SemiBold,
        color = MinlishOnSurface,
        textAlign = TextAlign.Center
    )

    Text(
        text = "No worries! Enter the email associated with your account and we'll send you a link to reset your password.",
        fontSize = 14.sp,
        color = MinlishOnSurfaceVariant,
        textAlign = TextAlign.Center,
        lineHeight = 21.sp
    )
}

@Composable
private fun ForgotPasswordEmailField(
    email: String,
    onEmailChange: (String) -> Unit
) {
    var isTouched by remember { mutableStateOf(false) }
    val isError = isTouched && email.isBlank()
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "Email Address",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = MinlishOnSurface
        )
        OutlinedTextField(
            value = email,
            onValueChange = {
                isTouched = true
                onEmailChange(it)
            },
            isError = isError,
            supportingText = {
                if (isError) {
                    Text("Email is required", color = MaterialTheme.colorScheme.error)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text("Enter your email", color = MinlishOutline)
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Email,
                    contentDescription = null,
                    tint = MinlishOutline
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            ),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MinlishOnSurface,
                unfocusedBorderColor = MinlishOutlineVariant,
                focusedContainerColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = MinlishPrimary
            )

        )
    }
}

@Preview(showBackground = true)
@Composable
fun ForgotPasswordScreenPreview() {
    ForgotPasswordScreen()
}