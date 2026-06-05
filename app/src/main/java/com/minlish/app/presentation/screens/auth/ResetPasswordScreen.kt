package com.minlish.app.presentation.screens.auth
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.outlined.*
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
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.minlish.app.presentation.screens.auth.components.SecurityCheckList
import com.minlish.app.presentation.components.TopBar
import com.minlish.app.presentation.screens.auth.components.OtpInputField
import com.minlish.app.presentation.screens.auth.viewmodels.ForgetPasswordUiEvent
import com.minlish.app.presentation.screens.auth.viewmodels.ForgetPasswordViewModel
import com.minlish.app.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResetPasswordScreen(
    viewModel: ForgetPasswordViewModel = viewModel(),
    onBackClick: () -> Unit = {},
    onResetPasswordSuccess: () -> Unit = {}
) {

    var newPasswordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    val hasMinLength = state.newPassword.length >= 8
    val hasUppercase = state.newPassword.any {it.isUpperCase()}
    val hasNumberOrSymbol = state.newPassword.any {!it.isLetterOrDigit() || it.isDigit()}
    val passwordsMatch = state.newPassword == state.confirmPassword && state.confirmPassword.isNotEmpty()
    val otpComplete = state.otpValue.length == 6
    val isBlank = state.newPassword.isBlank() && state.confirmPassword.isBlank()

    val context = LocalContext.current


    LaunchedEffect(Unit) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is ForgetPasswordUiEvent.ResetPasswordSuccess -> {
                    onResetPasswordSuccess()
                }
                is ForgetPasswordUiEvent.ShowError -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                }

                else -> {}
            }
        }
    }

    LaunchedEffect(Unit) {
        while (state.secondsLeft > 0) {
            delay(1000)
            viewModel.decrementSeconds()
        }
    }
    val timerText = "%d:%02d".format(state.secondsLeft / 60, state.secondsLeft % 60)


    Scaffold(
        topBar = {
            TopBar(
                title = "MinLish",
                onBackClick = onBackClick
            )
        },
        containerColor = MinlishSurface
    ) {paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp, bottom = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            OtpSection(
                otpValue = state.otpValue,
                timerText = timerText,
                secondsLeft = state.secondsLeft,
                onOtpChange = { if (it.length <= 6) viewModel.updateOtp(it) },
                onResendOTP = {
                    viewModel.resendForgotPasswordEmail()
                    viewModel.updateSecondsLeft(599)
                }
            )
            HorizontalDivider(color = MinlishSurfaceContainerHigh)

            ResetPasswordCard(
                isLoading = state.isLoading,
                newPassword = state.newPassword,
                confirmPassword = state.confirmPassword,
                newPasswordVisible = newPasswordVisible,
                confirmPasswordVisible = confirmPasswordVisible,
                hasMinLength = hasMinLength,
                hasUppercase = hasUppercase,
                hasNumberOrSymbol = hasNumberOrSymbol,
                passwordsMatch = passwordsMatch,
                otpComplete = otpComplete,
                isBlank = isBlank,
                onNewPasswordChange = { viewModel.updatePassword(it) },
                onConfirmPasswordChange = { viewModel.updateConfirmPassword(it) },
                onToggleNewPassword = { newPasswordVisible = !newPasswordVisible },
                onToggleConfirmPassword = { confirmPasswordVisible = !confirmPasswordVisible },
                onResetPassword = {
                    viewModel.resetPassword()
                }
            )
        }
    }
}

@Composable
private fun ResetPasswordCard(
    isLoading: Boolean,
    newPassword: String,
    confirmPassword: String,
    newPasswordVisible: Boolean,
    confirmPasswordVisible: Boolean,
    hasMinLength: Boolean,
    hasUppercase: Boolean,
    hasNumberOrSymbol: Boolean,
    passwordsMatch: Boolean,
    otpComplete: Boolean,
    isBlank: Boolean,
    onNewPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onToggleNewPassword: () -> Unit,
    onToggleConfirmPassword: () -> Unit,
    onResetPassword: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MinlishSurfaceLowest,
        shadowElevation = 8.dp
    ) {
        val isEnable = hasMinLength && hasUppercase && hasNumberOrSymbol && passwordsMatch && otpComplete && !isLoading && !isBlank
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            ResetPasswordHeader()
            PasswordInputField(
                label = "New Password",
                value = newPassword,
                onValueChange = onNewPasswordChange,
                visible = newPasswordVisible,
                onToggleVisibility = onToggleNewPassword,
                imeAction = ImeAction.Next,
                isMatch = false
            )
            PasswordInputField(
                label = "Confirm Password",
                value = confirmPassword,
                onValueChange = onConfirmPasswordChange,
                visible = confirmPasswordVisible,
                onToggleVisibility = onToggleConfirmPassword,
                imeAction = ImeAction.Done,
                isMatch = confirmPassword.isNotEmpty() && !passwordsMatch
             )
            SecurityCheckList(
                hasMinLength = hasMinLength,
                hasUppercase = hasUppercase,
                hasNumberOrSymbol = hasNumberOrSymbol
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .alpha(if (!isEnable) 0.4f else 1f)
                    .background(MinlishGradient),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = onResetPassword,
                    modifier = Modifier.fillMaxSize(),
                    enabled = isEnable,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        disabledContentColor = Color.Transparent
                    ),
                    elevation = null
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
                            text = "Reset Password",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (hasMinLength && hasUppercase && hasNumberOrSymbol && passwordsMatch)
                                Color.White
                            else
                                Color.White.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ResetPasswordHeader() {
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
                imageVector = Icons.Filled.Key,
                contentDescription = null,
                tint = MinlishPrimary,
                modifier = Modifier.size(32.dp)
            )
        }
        Text(
            text = "Create New Password",
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            color = MinlishOnSurface,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Your new password must be different from previously used passwords.",
            fontSize = 14.sp,
            color = MinlishOnSurfaceVariant,
            textAlign = TextAlign.Center,
            lineHeight = 21.sp
        )
    }
}

@Composable
private fun PasswordInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    visible: Boolean,
    onToggleVisibility: () -> Unit,
    imeAction: ImeAction = ImeAction.Done,
    isMatch: Boolean = false
) {
    var isTouched by remember { mutableStateOf(false) }
    val isError = isTouched && value.isBlank()
    val errorMessage = when {
        isTouched && isError -> "$label is required"
        isError && !isMatch -> "Password do not match"
        else -> null
    }
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = MinlishOnSurface
        )
        OutlinedTextField(
            value = value,
            onValueChange = {
                isTouched = true
                onValueChange(it)
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("••••••••", color = MinlishOutline) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Lock,
                    contentDescription = null,
                    tint = MinlishOutline
                )
            },
            trailingIcon = {
                IconButton(onClick = onToggleVisibility) {
                    Icon(
                        imageVector = if (visible)
                            Icons.Outlined.Visibility
                        else
                            Icons.Outlined.VisibilityOff,
                        contentDescription = null,
                        tint = MinlishOnSurface
                    )
                }
            },
            visualTransformation = if (visible)
                VisualTransformation.None
            else
                PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = imeAction
            ),
            singleLine = true,
            isError = isError || !isMatch,
            supportingText = {
                if (errorMessage != null) {
                    Text(errorMessage, color = MinlishError)
                }
            },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MinlishPrimary,
                unfocusedBorderColor = MinlishOutlineVariant,
                errorBorderColor = MinlishError,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                cursorColor = MinlishPrimary
            )
        )
    }
}


@Composable
private fun OtpSection(
    otpValue: String,
    timerText: String,
    secondsLeft: Int,
    onOtpChange: (String) -> Unit,
    onResendOTP: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "Enter OTP Code",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MinlishOnSurface
            )
            Text(
                text = "Check your email for the 6-digit code",
                fontSize = 13.sp,
                color = MinlishOnSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }

        OtpInputField(
            otpValue = otpValue,
            onOtpChange = onOtpChange
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Timer,
                    contentDescription = null,
                    tint = if (secondsLeft < 60) MinlishError
                        else MinlishOnSurfaceVariant,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "Expires in $timerText",
                    fontSize = 13.sp,
                    color = if (secondsLeft < 60) MinlishError
                    else MinlishOnSurfaceVariant
                )
            }

            TextButton(
                onClick = {
                    onResendOTP()
                },
                enabled = secondsLeft == 0,
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = "Resend OTP",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (secondsLeft == 0) MinlishPrimary
                        else MinlishOutlineVariant
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ResetPasswordScreenReview() {
    ResetPasswordScreen()
}