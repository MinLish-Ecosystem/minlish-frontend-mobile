package com.minlish.app.presentation.screens.learning

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.minlish.app.R

@Composable
fun FlashCardTopBar(
    currentProgress: Int,
    totalQuestion: Int,
    onExitClick: () -> Unit,
    onMoreClick: () -> Unit
){
    Surface(
        color = Color.White,
        shadowElevation = 6.dp
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            IconButton(onClick=onExitClick){
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint=Color.Black
                )
            }
            Column(
                modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ){
                    Text(
                        text="Mastering C1",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp
                        ),
                        color = Color(0xFF4648D4)
                    )
                    Text(
                        text="${currentProgress}/${totalQuestion}",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp
                        ),
                        color = Color.Black
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFFE4E1ED))
                ){
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(currentProgress.toFloat() / totalQuestion)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(4.dp))
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(Color(0xFF667EEA), Color(0xFF764BA2))
                                )
                            )
                    )
                }

            }
            IconButton(onClick=onMoreClick){
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More",
                    tint=Color.Black
                )
            }
        }
    }
}
@Composable
fun FlashCardBottomBar(answerOptions: List<String>, onClick: (String) -> Unit){
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
        color = Color.White.copy(alpha = 0.98f),
        shadowElevation = 24.dp,
        border = BorderStroke(
            width = 1.dp,
            color = Color(0xFFE4E1ED).copy(alpha = 0.5f)
        )
    ){
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp)
        ){
            Row(
                modifier=Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ){
                answerOptions.forEach { option->
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .height(72.dp)
                            .clickable {
                                onClick(option)
                            },
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFE4E1ED) ,
                        tonalElevation = 1.dp
                    ){
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(vertical = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = option,
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 14.sp
                                ),
                                color = Color(0xFF4648D4)
                            )
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun FlashCardCard(flashcardData: FlashcardData,onPronunciationClick: () -> Unit){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.85f),
        shape=RoundedCornerShape(16.dp),
        colors= CardDefaults.cardColors(containerColor = Color(0xFFF5F2FE)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ){
        Column{
            Box(modifier = Modifier){
                Surface(
                    modifier = Modifier.padding(16.dp).align(Alignment.TopStart),
                    shape=RoundedCornerShape(50.dp),
                    color = Color(0xFFFCF8FF).copy(alpha = 0.9f),
                    tonalElevation = 1.dp
                ){
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ){
                        Text(
                            text="IELTS SCORE",
                            style= MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            ),
                            color = Color.Blue,
                        )
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(RoundedCornerShape(50))
                                .background(Color(0xFF4648D4))
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(192.dp)
                        .background(Color(0xFFF0F4FF))
                ){
                    AsyncImage(
                        model = R.drawable.images,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFFF0F4FF)),
                        contentScale = ContentScale.Crop,
                        alpha = 0.9f
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color(0xFFF5F2FE).copy(alpha = 0.8f)
                                    ),
                                    startY = 100f
                                )
                            )
                    )
                    FloatingActionButton(
                        onClick = onPronunciationClick,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(16.dp),
                        containerColor = Color(0xFF4648D4),
                        contentColor = Color.White,
                        elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = "Play pronunciation",
                            modifier = Modifier.size(24.dp)
                        )
                    }

                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ){
                Text(
                    text = flashcardData.word,
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 36.sp
                    ),
                    color = Color(0xFF1B1B23), // on-surface
                    textAlign = TextAlign.Center,
                    letterSpacing = (-0.02).em
                )
                Text(
                    text = flashcardData.phonetic,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = 18.sp
                    ),
                    color = Color(0xFF464554),
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                    modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
                )
                Box(
                    modifier=Modifier
                        .width(48.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Color(0xFF6063EE))
                        .padding(bottom = 24.dp)
                )
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment= Alignment.Start,
                ){
                    Text(
                        text = flashcardData.partOfSpeech,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        ),
                        color = Color(0xFF581C87),
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = flashcardData.definition,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 16.sp,
                            lineHeight = 24.sp
                        ),
                        color = Color(0xFF1B1B23),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape=RoundedCornerShape(8.dp),
                        color = Color(0xFFFCF8FF),
                        border = BorderStroke(
                            width = 10.dp,
                            brush = Brush.horizontalGradient(
                                colors = listOf(Color(0xFF06B6D4), Color.Transparent),
                                startX = 0f,
                                endX = 4f
                            )
                        )
                    ){
                        Text(
                            text=flashcardData.example,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 14.sp,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                            ),
                            color = Color(0xFF464554),
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun FlashCardScreen(viewModel: FlashcardViewModel,onExitClick: () -> Unit, onMoreClick: () -> Unit) {
    val isLoading by viewModel.isLoading
    val error by viewModel.errorMessage
    val selectedAnswer by viewModel.selectedAnswer
    val currentCardData = viewModel.currentCard.value
    val totalCards = viewModel.totalCards
    LaunchedEffect("user_001") {
        viewModel.loadFlashcardSet("user_001")
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            FlashCardTopBar(
                currentProgress =viewModel.currentIndex.value+1,
                totalQuestion = totalCards.value,
                onExitClick = onExitClick,
                onMoreClick = onMoreClick,
            )
        },
        bottomBar = {
            FlashCardBottomBar(
                answerOptions = listOf("Again", "Hard", "Good", "Easy"),
                onClick = {answer ->
                    viewModel.onAnswerSelected(answer)}
            )
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        if (error != null) {
            Column(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Lỗi: $error", color = MaterialTheme.colorScheme.error)
                Button(onClick = { viewModel.loadFlashcardSet("user_001") }) {
                    Text("Thử lại")
                }
            }
            return@Scaffold
        }
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(Color(0xFFF5F2FE))
            .padding(horizontal = 16.dp, vertical = 8.dp),
            ){
            currentCardData?.let { card ->
                FlashCardCard(flashcardData = card,{})
                selectedAnswer?.let { answer ->
                    Text(
                        text = "Bạn đang bấm ${answer}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 14.sp,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        ),
                        color = Color(0xFF464554),
                        modifier = Modifier.padding(16.dp)
                    )
                }
            } ?: run {
                Text("No card data available", modifier = Modifier.padding(16.dp))
                }
        }

    }
}
