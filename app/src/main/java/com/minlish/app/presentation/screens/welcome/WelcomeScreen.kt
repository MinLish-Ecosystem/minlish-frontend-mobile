package com.minlish.app.presentation.screens.welcome

import androidx.compose.animation.animateContentSize
import com.minlish.app.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.minlish.app.ui.theme.MinlishGradient
import com.minlish.app.ui.theme.MinlishPrimary
import com.minlish.app.ui.theme.MinlishOnSurface
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.minlish.app.ui.theme.MinlishOnSurfaceVariant
import com.minlish.app.ui.theme.MinlishOutlineVariant
import kotlinx.coroutines.delay
import kotlinx.serialization.descriptors.SerialDescriptor


data class OnboardingPage(
    val imageRes: Int,
    val title: String,
    val description: String
)

private val onBoardingPages = listOf(
    OnboardingPage(
        imageRes = R.drawable.illustration_img,
        title = "Master English.\nConnect the World.",
        description = "Your journey to fluent communication starts here. Learn at your own pace."
    ),
    OnboardingPage(
        imageRes = R.drawable.illustration2_img,
        title = "Learn Anywhere, Anytime on Your Own Terms.",
        description = "Pocket-sized lessons that fit your schedule. Just 5 minutes a day is enough."
    ),
    OnboardingPage(
        imageRes = R.drawable.illustration3_img,
        title = "Accelerate Your Vocabulary Mastery",
        description = "Smart spacing, zero wasted effort. Review only the words you are about to forget."
    )
)
@Composable
fun WelcomeScreen(
    onGetStartedClick: () -> Unit = {},
    onLogInClick: () -> Unit = {}
) {
    val pagerState = rememberPagerState(pageCount = {onBoardingPages.size})
    LaunchedEffect(Unit) {
        while (true) {
            delay(3000)
            val nextPage = (pagerState.currentPage + 1) % onBoardingPages.size
            pagerState.animateScrollToPage(nextPage)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .size(300.dp)
                .offset(x = (-50).dp, y = (-50).dp)
                .background(
                    Color(0xFF9C48EA).copy(alpha = 0.1f),
                    RoundedCornerShape(50)
                )
                .blur(80.dp)
        )
        Box(
            modifier = Modifier
                .size(360.dp)
                .align(Alignment.BottomEnd)
                .offset(x = 60.dp, y = 60.dp)
                .background(
                    Color(0xFF6063EE).copy(alpha = 0.07f),
                    RoundedCornerShape(50)
                )
                .blur(80.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(48.dp))
                WelcomeHeader()

                Spacer(modifier = Modifier.height(32.dp))
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    page -> OnBoardingPageContent(page = onBoardingPages[page])
                }

                Spacer(modifier = Modifier.height(16.dp))

                PagerDotIndicator(
                    pageCount = onBoardingPages.size,
                    currentPage = pagerState.currentPage
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            Column {
                WelcomeBottomActions(
                    onGetStartedClick,
                    onLogInClick
                )
                Spacer(modifier = Modifier.height(48.dp))
            }
        }
    }
}

@Composable
private fun WelcomeHeader() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.Language,
            contentDescription = null,
            tint = MinlishPrimary,
            modifier = Modifier.size(40.dp)
        )
        Text (
            text = "MinLish",
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = (-0.5).sp,
            style = LocalTextStyle.current.copy(
                brush = MinlishGradient
            )
        )
    }
}

@Composable
private fun WelcomeIllustration(idImg: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(32.dp))
            .background(
                MinlishGradient
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = idImg),
            contentDescription = "Welcome Illustration",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 1f
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    MinlishGradient,
                    alpha = 0.2f
                )
        )
    }
}

@Composable
private fun WelcomeText(title: String, description: String, modifier: Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = title,
            fontSize = 32.sp,
            fontWeight = FontWeight.SemiBold,
            color = MinlishOnSurface,
            textAlign = TextAlign.Center,
            lineHeight = 41.sp
        )
        Text(
            text = description,
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal,
            color = MinlishOnSurfaceVariant,
            textAlign = TextAlign.Center,
            lineHeight = 29.sp,
            modifier = Modifier.widthIn(max = 290.dp)
        )
    }
}

@Composable
private fun WelcomeBottomActions(
    onGetStartedClick: () -> Unit,
    onLogInClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MinlishGradient)
                .clickable { onGetStartedClick() },
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Get Started",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
                Icon(
                    imageVector = Icons.Outlined.ArrowForward,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Already have an account ?",
                fontSize = 16.sp,
                color = MinlishOnSurfaceVariant
            )
            TextButton(onClick = onLogInClick) {
                Text(
                    text = "Log In",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MinlishPrimary
                )
            }
        }
    }
}

@Composable
private fun OnBoardingPageContent(page: OnboardingPage) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        WelcomeIllustration(page.imageRes)

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
           WelcomeText(page.title, page.description, modifier = Modifier)
        }
    }
}

@Composable
private fun PagerDotIndicator(
    pageCount: Int,
    currentPage: Int
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pageCount) {
            index -> val isSelected = index == currentPage
            Box(
                modifier = Modifier
                    .height(8.dp)
                    .width(if (isSelected) 24.dp else 8.dp)
                    .clip(RoundedCornerShape(50))
                    .background(
                        if (isSelected) MinlishPrimary
                        else MinlishOutlineVariant
                    )
                    .animateContentSize()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    WelcomeScreen()
}