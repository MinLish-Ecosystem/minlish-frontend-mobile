package com.minlish.app.presentation.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import com.minlish.app.presentation.components.TopBar
import com.minlish.app.presentation.screens.auth.components.OtpInputField
import com.minlish.app.presentation.screens.auth.viewmodels.AuthViewModel
import com.minlish.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerifyEmailScreen(
    onBackClick: () -> Unit = {},
    onVerifyEmailSuccess: () -> Unit = {},
    onChangeEmail: () -> Unit = {},
    viewModel: AuthViewModel = viewModel()
) {
    val email = viewModel.email
    val name = viewModel.name
    val password =  viewModel.password

    var otpValue by remember { mutableStateOf("") }
    var secondsLeft by remember { mutableIntStateOf(599) }

    LaunchedEffect(Unit) {
        while (secondsLeft > 0) {
            delay(1000)
            secondsLeft--
        }
    }

    LaunchedEffect(viewModel.verifyEmailSuccess) {
        if (viewModel.verifyEmailSuccess) {
            onVerifyEmailSuccess()
        }
    }

    val minutes = secondsLeft / 60
    val seconds = secondsLeft % 60
    val timerText = "%d:%02d".format(minutes, seconds)

    Scaffold(
        topBar = {
            TopBar(
                title = "MinLish",
                onBackClick = onBackClick
            )
        },
        containerColor = MinlishSurface
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .padding(top = 40.dp, bottom = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            VerifyEmailCard(
                email = email,
                otpValue = otpValue,
                timerText = timerText,
                secondsLeft = secondsLeft,
                onChangeEmail = onChangeEmail,
                onOtpChange = { if (it.length <= 6) otpValue = it },
                onResendCode = {
                    viewModel.resendVerifyEmailOtp()
                },
                onVerifyClick = {
                    viewModel.verifyEmail(email, otpValue)
                }
            )
        }
    }
}

@Composable
private fun VerifyEmailHeader(
    email: String,
    onChangeEmail: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(Color(0xFFF0F4FF)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Email,
                contentDescription = null,
                tint = MinlishPrimary,
                modifier = Modifier.size(32.dp)
            )
        }

        Text(
            text = "Verify Your Email",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MinlishOnSurface,
            textAlign = TextAlign.Center
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "An 6-digit code has been sent to ",
                    fontSize = 16.sp,
                    color = MinlishOnSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = email,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MinlishOnSurface
                )
                Spacer(modifier = Modifier.width(4.dp))
                TextButton(
                    onClick = onChangeEmail,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = "Change",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MinlishPrimary
                    )
                }
            }
        }
    }
}

@Composable
private fun VerifyEmailHints(
    timerText: String,
    secondsLeft: Int,
    onResendCode: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "• ", fontSize = 14.sp, color = MinlishOnSurfaceVariant)
            Text(
                text = "The OTP will expired in ",
                fontSize = 14.sp,
                color = MinlishOnSurfaceVariant
            )
            Text(
                text = timerText,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = if (secondsLeft < 60) MinlishError else MinlishOnSurface
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "• ", fontSize = 14.sp, color = MinlishOnSurfaceVariant)
            Text(
                text = "Didn't receive the code?",
                fontSize = 16.sp,
                color = MinlishOnSurfaceVariant
            )
            TextButton(
                onClick = onResendCode,
                enabled = secondsLeft == 0,
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = "Resend",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (secondsLeft == 0) MinlishPrimary
                            else MinlishOutlineVariant
                )
            }
        }
    }
}

@Composable
private fun VerifyEmailCard(
    email: String,
    otpValue: String,
    timerText: String,
    secondsLeft: Int,
    onOtpChange: (String) -> Unit,
    onVerifyClick: (otp: String) -> Unit,
    onResendCode: () -> Unit,
    onChangeEmail: () -> Unit,
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            VerifyEmailHeader(
                email = email,
                onChangeEmail = onChangeEmail
            )
            OtpInputField(
                otpValue = otpValue,
                onOtpChange = onOtpChange
            )
            VerifyEmailHints(
                timerText = timerText,
                secondsLeft = secondsLeft,
                onResendCode = onResendCode
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MinlishGradient),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = { onVerifyClick(otpValue) },
                    modifier = Modifier.fillMaxSize(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent
                    ),
                    elevation = null
                ) {
                    Text(
                        text = "Verify",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (otpValue.length == 6)
                            Color.White
                        else
                            Color.White.copy(alpha = 0.5f)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VerifyEmailScreenPreview() {
    VerifyEmailScreen()
}