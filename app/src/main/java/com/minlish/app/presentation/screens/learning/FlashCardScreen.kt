package com.minlish.app.presentation.screens.learning

import android.media.MediaPlayer
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.graphicsLayer
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
fun FlashCardCard(flashcardData: FlashcardData){
    var isFlipped by remember { mutableStateOf(false) }
    LaunchedEffect(flashcardData) {
        isFlipped = false
    }
    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(
            durationMillis = 500,
            easing = FastOutSlowInEasing
        )
    )
    val mediaPlayer = remember { MediaPlayer() }
    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer.release()
        }
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.85f)
            .clickable { isFlipped = !isFlipped }
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 12 * density
            },
        shape=RoundedCornerShape(16.dp),
        colors= CardDefaults.cardColors(containerColor = Color(0xFFF5F2FE)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ){
        Box(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        alpha = if (rotation <= 90f) 1f else 0f
                    }
            ) {
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
                                onClick = {
                                    if (flashcardData.audioUrl.isNotEmpty()) {
                                        mediaPlayer.reset()
                                        mediaPlayer.setDataSource(flashcardData.audioUrl)
                                        mediaPlayer.setOnPreparedListener { mp ->
                                            mp.start()
                                        }
                                        mediaPlayer.setOnErrorListener { mp, what, extra ->
                                            true
                                        }
                                        mediaPlayer.prepareAsync()
                                    }
                                },
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
                            color = Color(0xFF1B1B23),
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
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "(Nhấn để lật xem nghĩa)",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 12.sp,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                            ),
                            color = Color(0xFF8B8D98),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        rotationY = 180f
                        alpha = if (rotation > 90f) 1f else 0f
                    }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = flashcardData.word,
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 28.sp
                        ),
                        color = Color(0xFF4648D4),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = flashcardData.partOfSpeech.uppercase(),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            letterSpacing = 0.5.sp
                        ),
                        color = Color(0xFF581C87),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Color(0xFFE4E1ED))
                            .padding(vertical = 12.dp)
                    )

                    Column(
                        modifier = Modifier.fillMaxWidth().weight(1f, fill = false),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = "Định nghĩa:",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFF64748B)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = flashcardData.definition,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 16.sp,
                                lineHeight = 24.sp
                            ),
                            color = Color(0xFF1B1B23),
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        Text(
                            text = "Ví dụ:",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFF64748B)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFFCF8FF),
                            border = BorderStroke(
                                width = 1.dp,
                                color = Color(0xFFE4E1ED)
                            )
                        ) {
                            Text(
                                text = flashcardData.example,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 14.sp,
                                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                                ),
                                color = Color(0xFF464554),
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "(Nhấn để lật lại mặt trước)",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 12.sp,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        ),
                        color = Color(0xFF8B8D98),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
@Composable
fun CompletionScreen(onDoneClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("🎉", fontSize = 64.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Hoàn thành!", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Kết quả đã được lưu", color = Color(0xFF464554))
        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onDoneClick,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Quay về Dashboard")
        }
    }
}
@Composable
fun FlashCardScreen(
    viewModel: FlashcardViewModel,
    onExitClick: () -> Unit,
    onMoreClick: () -> Unit,
    setId: String? = null
) {
    val isLoading by viewModel.isLoading
    val error by viewModel.errorMessage
    val selectedAnswer by viewModel.selectedAnswer
    val currentCardData by viewModel.currentCard
    val totalCards by viewModel.totalCards
    val isCompleted by viewModel.isCompleted
    LaunchedEffect(setId) {
        viewModel.loadFlashcardSet(setId)
    }
    // Thành công
    if (isCompleted) {
        CompletionScreen(onDoneClick = onExitClick)
        return
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            FlashCardTopBar(
                currentProgress =viewModel.currentIndex.value+1,
                totalQuestion = totalCards,
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
                Button(onClick = { viewModel.loadFlashcardSet() }) {
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
                FlashCardCard(flashcardData = card)
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
            }
        }

    }
}

