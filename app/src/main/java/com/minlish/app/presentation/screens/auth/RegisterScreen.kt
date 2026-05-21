package com.minlish.app.presentation.screens.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import com.minlish.app.R

private val startColor = Color(0xFF667EEA)
private val endColor = Color(0xFF764BA2)

@Composable
fun RegisterScreen() {
    var fullName by remember {  mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(top = 64.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BrandingSection()
        Spacer(modifier = Modifier.height(40.dp))
        FullNameField(fullName = fullName, onFullNameChange = {fullName = it})
        Spacer(modifier = Modifier.height(16.dp))
        EmailField(email = email, onEmailChange = {email = it})
        Spacer(modifier = Modifier.height(16.dp))
        PasswordField(
            password = password,
            passwordVisible = passwordVisible,
            onPasswordChange = {password = it},
            onToggleVisibility = {passwordVisible = !passwordVisible}
        )
        Spacer(modifier = Modifier.height(24.dp))
        SignUpButton(onSignUpClick = {})
        Spacer(modifier = Modifier.height(24.dp))
        DividerWithText()
        Spacer(modifier = Modifier.height(16.dp))
        GoogleButton(onGoogleSignInClick = {})
        Spacer(modifier = Modifier.height(32.dp))
        SignInButton(onSignInClick = {})
    }
}

@Composable
private fun BrandingSection() {
    val colorBrush = Brush.linearGradient(
        colors = listOf(startColor, endColor)
    )

    Text(
        text = "MinList",
        fontSize = 48.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = (-0.5).sp,
        textAlign = TextAlign.Center,
        style = LocalTextStyle.current.copy(
            brush = colorBrush
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
                Text("e.g. Jane Doe", color = Color(0xFF767586))
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = null,
                    tint = Color(0xFF767586)
                )
            },
            keyboardOptions = KeyboardOptions (
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            singleLine = true,
            shape = RoundedCornerShape(size = 12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF4648D4),
                unfocusedBorderColor = Color(0xFFC7C4D7),
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                cursorColor = Color(0xFF4648D4)
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
                Text("you@example.com", color = Color(0xFF767586))
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Email,
                    contentDescription = null,
                    tint = Color(0xFF767586)
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            singleLine = true,
            shape = RoundedCornerShape(size = 12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF4648D4),
                unfocusedBorderColor = Color(0xFFC7C4D7),
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                cursorColor = Color(0xFF4648D4)
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
                    tint = Color(0xFF767586)
                )
            },
            trailingIcon = {
                IconButton(onClick = onToggleVisibility) {
                    Icon(
                        imageVector = if (passwordVisible)
                            Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
                        contentDescription = null,
                        tint = Color(0xFF767586)
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
                focusedBorderColor = Color(0xFF4648D4),
                unfocusedBorderColor = Color(0xFFC7C4D7),
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                cursorColor = Color(0xFF4648D4)
            )
        )
    }
}

@Composable
private fun SignUpButton(
    onSignUpClick: () -> Unit
) {
    val colorBrush = Brush.linearGradient(
        colors = listOf(
            Color(0xFF667EEA),
            Color(0xFF764BA2)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(colorBrush)
            .clickable {onSignUpClick()},
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Sign Up",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White,
                letterSpacing = 0.1.sp
            )
            Icon(
                imageVector = Icons.Outlined.ArrowForward,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
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
private fun GoogleLogo() {
    Canvas(modifier = Modifier.size(20.dp)) {
        val w = size.width
        val h = size.height

        drawArc(
            color = Color(0xFF4285F4),
            startAngle = -90f,
            sweepAngle = 180f,
            useCenter = false,
            size = androidx.compose.ui.geometry.Size(w, h),
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = w * 0.18f)
        )
        drawArc(
            color = Color(0xFFEA4335),
            startAngle = -90f,
            sweepAngle = -180f,
            useCenter = false,
            size = androidx.compose.ui.geometry.Size(w, h),
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = w * 0.18f)
        )
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
            color = Color(0xFF464554)
        )
        TextButton(onClick = onSignInClick) {
            Text(
                text = "Log In",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF4648D4)

            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    RegisterScreen()
}
