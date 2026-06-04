package com.minlish.app.presentation.screens.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.minlish.app.R
import com.minlish.app.presentation.screens.auth.viewmodels.AuthUiEvent
import com.minlish.app.presentation.screens.auth.viewmodels.AuthViewModel
import com.minlish.app.ui.theme.MinlishGradient
import com.minlish.app.ui.theme.MinlishOnSurface
import com.minlish.app.ui.theme.MinlishOutline
import com.minlish.app.ui.theme.MinlishPrimary
import com.minlish.app.ui.theme.MinlishSurface
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LoginScreen(
    viewModel: AuthViewModel = viewModel(),
    onLoginSuccess: () -> Unit,
    onGoogleSignInClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onSignUpClick: () -> Unit
) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is AuthUiEvent.LoginSuccess -> {
                    onLoginSuccess()
                }

                else -> {

                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .statusBarsPadding()
            .padding(horizontal = 16.dp)
            .padding(top = 24.dp, bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BrandingSection()
        Spacer(modifier = Modifier.height(40.dp))
        EmailField(email = email, onEmailChange = {email = it})
        Spacer(modifier = Modifier.height(16.dp))
        PasswordField(
            password = password,
            passwordVisible = passwordVisible,
            onPasswordChange = {password = it},
            onToggleVisibility = {passwordVisible = !passwordVisible},
            onForgotPasswordClick = {
                onForgotPasswordClick()
            }
        )
        Spacer(modifier = Modifier.height(24.dp))
        SignInButton(
            isLoading = viewModel.isLoading,
            onSignInClick = {
            viewModel.login(email, password)
        })
        Spacer(modifier = Modifier.height(24.dp))
        DividerWithText()
        Spacer(modifier = Modifier.height(16.dp))
        GoogleButton(onGoogleSignInClick = {
            onGoogleSignInClick()
        })
        Spacer(modifier = Modifier.height(32.dp))
        SignUpButton(onSignUpClick = {
            onSignUpClick()
        })
    }
}

@Composable
private fun BrandingSection() {
    Text(
        text = "MinList",
        fontSize = 48.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = (-0.5).sp,
        textAlign = TextAlign.Center,
        style = LocalTextStyle.current.copy(
            brush = MinlishGradient
        )
    )

    Spacer(modifier = Modifier.height(8.dp))

    Text(
        text = "Welcome back! Ready to learn?",
        fontSize = 16.sp,
        color = MinlishOutline,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun EmailField(
    email: String,
    onEmailChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {

        Text (
            text = "Email Address",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = MinlishOnSurface
        )

        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text("you@example.com", color = MinlishOutline)
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
                imeAction = ImeAction.Next
            ),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MinlishPrimary,
                unfocusedBorderColor = MinlishSurface,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                cursorColor = MinlishPrimary
            )
        )
    }
}

@Composable
private fun PasswordField(
    password: String,
    passwordVisible: Boolean,
    onPasswordChange: (String) -> Unit,
    onToggleVisibility: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Password",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MinlishOnSurface
            )

            TextButton(
                onClick = {
                    onForgotPasswordClick()
                },
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = "Forget Password?",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MinlishPrimary
                )
            }
        }

        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text("••••••••", color = MinlishOutline)
            },
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
                        imageVector = if (passwordVisible)
                            Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
                        contentDescription = if (passwordVisible) "Hide Password" else "Show Password",
                        tint = MinlishOutline
                    )
                }
            },
            visualTransformation = if (passwordVisible)
                VisualTransformation.None
            else
                PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MinlishPrimary,
                unfocusedBorderColor = MinlishSurface,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                cursorColor = MinlishPrimary
            )
        )
    }
}

@Composable
private fun SignInButton(
    onSignInClick: () -> Unit,
    isLoading: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MinlishGradient)
            .clickable(
                enabled = !isLoading,
                onClick = {onSignInClick()}
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
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
                    text = "Sign In",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    letterSpacing = 0.1.sp
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowForward,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
private fun DividerWithText() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            color = MinlishSurface
        )
        Text(
            text = "OR CONTINUE WITH",
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = MinlishOutline,
            letterSpacing = 0.8.sp
        )
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            color = MinlishSurface
        )
    }
}

@Composable
private fun GoogleButton(
    onGoogleSignInClick: () -> Unit
) {
    OutlinedButton(
        onClick =  onGoogleSignInClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.5.dp,Color(0xFFC7C4D7).copy(alpha = 0.5f)),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.White
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_google),
                contentDescription = "Google Logo",
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = "Continue with Google",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1B1B23)
            )
        }
    }
}
@Composable
private fun SignUpButton(
    onSignUpClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Don't have an account?",
            fontSize = 14.sp,
            color = MinlishOutline
        )
        TextButton(onClick = onSignUpClick) {
            Text(
                text = "Sign Up",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MinlishPrimary

            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(
        onLoginSuccess = {},
        onForgotPasswordClick = {},
        onSignUpClick = {},
        onGoogleSignInClick = {}

    )
}
