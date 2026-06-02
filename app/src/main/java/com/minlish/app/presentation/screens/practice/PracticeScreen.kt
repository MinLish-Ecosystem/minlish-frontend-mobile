package com.minlish.app.presentation.screens.practice

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.util.copy
import com.minlish.app.presentation.components.AppHeader
import com.minlish.app.presentation.components.Footer


data class PracticeMode(
    val id: String,
    val title: String,
    val description: String,
    var isComingSoon: Boolean,
    val iconName: ImageVector,
    val gradientColor: Brush,
    val buttonText: String,
)
@Composable
fun PracticeArenaCard(practiceMode: PracticeMode, onClick: () -> Unit){
    val cardAlpha = if (!practiceMode.isComingSoon) 1f else 0.6f
    val clickModifier = if (!practiceMode.isComingSoon) {
        Modifier.clickable { onClick()}
    } else {
        Modifier
    }
    Card(
        modifier=Modifier
            .fillMaxWidth()
            .then(clickModifier)
            .alpha(cardAlpha),
        shape = RoundedCornerShape(16.dp),
        colors= CardDefaults.cardColors(containerColor = Color.White),
        elevation= CardDefaults.cardElevation(defaultElevation = 4.dp),
    ){
        Box(
            modifier=Modifier
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFFF0F4FF), Color.White)
                    )
                )
                .padding(16.dp)
        ){
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ){
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.Top,
                ){
                    Box(
                        modifier=Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(practiceMode.gradientColor), // truyền đối số color
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = practiceMode.iconName,
                            contentDescription = practiceMode.title,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                    ){
                        Text(
                            text = practiceMode.title,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = Color(0xFF111827)
                        )
                        Text(
                            text = practiceMode.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF464554) // on-surface-variant
                        )
                    }
                }

                Button(
                    onClick = { if (!practiceMode.isComingSoon) onClick() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent
                    ),
                    enabled = !practiceMode.isComingSoon,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = practiceMode.gradientColor,
                                shape = RoundedCornerShape(12.dp)  // ← Bo góc ở đây!
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                practiceMode.buttonText,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                            Icon(
                                imageVector = Icons.Filled.ArrowForward,
                                contentDescription = practiceMode.buttonText,
                                tint = Color.White,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                    }
                }
            }
            if (practiceMode.isComingSoon){
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = (-8).dp, y = -8.dp),
                    shape = RoundedCornerShape(4.dp),
                    color = Color(0xFFE4E1ED),
                    tonalElevation = 2.dp
                ) {
                    Text(
                        text = "Coming Soon",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp

                        ),
                        color = Color(0xFF464554),
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }
    }
}
@Composable
fun PracticeArenaScreen(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    userName: String = "User",
    userAvatarUrl: String = "",
    unreadCount: Int = 0,
    onNotificationClick: () -> Unit = {},
    onUserClick: () -> Unit = {}
) {
    val practiceModes= listOf(
        PracticeMode(
            id="smart_review",
            title = "Smart Review (SRS)",
            description = "Optimize your memory with spaced repetition.",
            isComingSoon=false,
            iconName= Icons.Default.Psychology,
            gradientColor = Brush.horizontalGradient(colors = listOf(Color(0xFF667EEA), Color(0xFF764BA2))),
            buttonText = "Start Review"
        ),
        PracticeMode(
            id="vocabulary_quiz",
            title = "Vocabulary Quiz",
            description = "Test your knowledge on recent words.",
            isComingSoon=true,
            iconName= Icons.Default.Quiz,
            gradientColor = Brush.horizontalGradient(colors = listOf(Color(0xFF667EEA), Color(0xFF764BA2))),
            buttonText = "Start Quiz"
        ),
        PracticeMode(
            id="listening_challenge",
            title = "Listening Challenge",
            description = "Improve comprehension with native audio.",
            isComingSoon=true,
            iconName= Icons.Default.Headphones,
            gradientColor = Brush.horizontalGradient(colors = listOf(Color(0xFF667EEA), Color(0xFF764BA2))),
            buttonText = "Start Challenge"
        ),PracticeMode(
            id="writing_practice",
            title = "Writing Practice",
            description = "Construct sentences and get feedback.",
            isComingSoon=true,
            iconName= Icons.Default.Edit,
            gradientColor = Brush.horizontalGradient(colors = listOf(Color(0xFF667EEA), Color(0xFF764BA2))),
            buttonText = "Start Writing"
        )
    )
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppHeader(
                unreadCount = unreadCount,
                onNotificationClick = onNotificationClick,
                onUserClick = onUserClick
            )
        },
        bottomBar = {
            Footer(
                currentRoute = currentRoute,
                onNavigate = onNavigate
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFFCF8FF)) // surface background
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Section
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                Text(
                    text = "Practice Arena",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp
                    ),
                    color = Color(0xFF1B1B23) // on-background
                )
                Text(
                    text = "Choose a mode to sharpen your English skills today.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF464554), // on-surface-variant
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // Practice Modes List
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(practiceModes) { mode ->
                    PracticeArenaCard(practiceMode = mode, onClick = {})
                }
            }
        }
    }
}

