package com.minlish.app.presentation.screens.auth

import android.widget.Toast
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
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.minlish.app.R
import com.minlish.app.presentation.screens.auth.components.ProgressHeader
import com.minlish.app.presentation.screens.auth.components.SecurityCheckList
import com.minlish.app.presentation.screens.auth.viewmodels.AuthUiEvent
import com.minlish.app.presentation.screens.auth.viewmodels.AuthViewModel
import com.minlish.app.ui.theme.MinlishGradient
import com.minlish.app.ui.theme.MinlishOutline
import com.minlish.app.ui.theme.MinlishPrimary
import com.minlish.app.ui.theme.MinlishSurface
import kotlinx.coroutines.flow.collectLatest

@Composable
fun RegisterScreen(
    currentStep: Int = 1,
    totalSteps: Int = 2,
    viewModel: AuthViewModel = viewModel(),
    onRegisterSuccess: () -> Unit,
    onSignInClick: () -> Unit
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()
    var passwordVisible by remember { mutableStateOf(false) }

    val hasMinLength = state.value.password.length >= 8
    val hasUppercase = state.value.password.any {it.isUpperCase()}
    val hasNumberOrSymbol = state.value.password.any {!it.isLetterOrDigit() || it.isDigit()}
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is AuthUiEvent.RegisterSuccess -> {
                    onRegisterSuccess()
                }
                is AuthUiEvent.ShowError -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                }

                else -> {}
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .statusBarsPadding()
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp, bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProgressHeader(
            currentStep = currentStep,
            totalSteps = totalSteps,
            onBackClick = null
        )
        Spacer(modifier = Modifier.height(20.dp))
        BrandingSection()
        Spacer(modifier = Modifier.height(20.dp))
        FullNameField(fullName = state.value.name, onFullNameChange = {viewModel.updateName(it)})
        Spacer(modifier = Modifier.height(16.dp))
        EmailField(email = state.value.email, onEmailChange = {viewModel.updateEmail(it)})
        Spacer(modifier = Modifier.height(16.dp))
        PasswordField(
            password = state.value.password,
            passwordVisible = passwordVisible,
            onPasswordChange = {viewModel.updatePassword(it)},
            onToggleVisibility = {passwordVisible = !passwordVisible}
        )
        SecurityCheckList(
            hasMinLength = hasMinLength,
            hasUppercase = hasUppercase,
            hasNumberOrSymbol = hasNumberOrSymbol
        )
        Spacer(modifier = Modifier.height(16.dp))
        SignUpButton(
            onSignUpClick = {
                val currentState = state.value
                viewModel.register(
                    currentState.name,
                    currentState.password,
                    currentState.email
                )
            } ,
            hasMinLength = hasMinLength,
            hasUppercase = hasUppercase,
            hasNumberOrSymbol = hasNumberOrSymbol,
            isLoading = state.value.isLoading
        )
        Spacer(modifier = Modifier.height(12.dp))
        DividerWithText()
        Spacer(modifier = Modifier.height(12.dp))
        GoogleButton(onGoogleSignInClick = {})
        Spacer(modifier = Modifier.height(12.dp))
        SignInButton(onSignInClick = {
            onSignInClick()
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
        text = "Start your learning journey today",
        fontSize = 16.sp,
        color = Color(0xFF464554),
        textAlign = TextAlign.Center
    )
}

@Composable
private fun FullNameField(
    fullName: String,
    onFullNameChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = "Full Name",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF1B1B23)
        )

        OutlinedTextField(
            value = fullName,
            onValueChange = onFullNameChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text("e.g. Jane Doe", color = MinlishOutline)
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = null,
                    tint = MinlishOutline
                )
            },
            keyboardOptions = KeyboardOptions (
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            singleLine = true,
            shape = RoundedCornerShape(size = 12.dp),
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
private fun EmailField(
    email: String,
    onEmailChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = "Email",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF1B1B23)
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
            shape = RoundedCornerShape(size = 12.dp),
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
    onToggleVisibility: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {

        Text(
            text = "Passoword",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF1B1B23)
        )

        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text("••••••••", color = Color(0xFF767586))
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
                        contentDescription = null,
                        tint = MinlishOutline
                    )
                }
            },
            visualTransformation = if (passwordVisible)
                VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            singleLine = true,
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
private fun SignUpButton(
    hasMinLength: Boolean,
    hasUppercase: Boolean,
    hasNumberOrSymbol: Boolean,
    onSignUpClick: () -> Unit,
    isLoading: Boolean,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MinlishGradient)
            .clickable { onSignUpClick() },
        contentAlignment = Alignment.Center,
    ) {
        Button(
            onClick = onSignUpClick,
            modifier = Modifier.fillMaxSize(),
            enabled = hasMinLength && hasUppercase && hasNumberOrSymbol && !isLoading,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                disabledContentColor = Color.Transparent
            ),
            elevation = null
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
                        text = "Sign Up",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (hasMinLength && hasUppercase && hasNumberOrSymbol)
                            Color.White
                        else
                            Color.White.copy(alpha = 0.5f)
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.ArrowForward,
                        contentDescription = null,
                        tint = if (hasMinLength && hasUppercase && hasNumberOrSymbol)
                            Color.White
                        else
                            Color.White.copy(alpha = 0.5f),
                        modifier = Modifier.size(18.dp)
                    )
                }
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
            color = Color(0xFFE9E6F3)
        )
        Text(
            text = "OR",
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF464554),
            letterSpacing = 0.8.sp
        )
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            color = Color(0xFFE9E6F3)
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
private fun SignInButton(
    onSignInClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Already have an account ?",
            fontSize = 14.sp,
            color = MinlishOutline
        )
        TextButton(onClick = onSignInClick) {
            Text(
                text = "Log In",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MinlishPrimary

            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    RegisterScreen(
        onRegisterSuccess = {},
        onSignInClick = {}
    )
}
