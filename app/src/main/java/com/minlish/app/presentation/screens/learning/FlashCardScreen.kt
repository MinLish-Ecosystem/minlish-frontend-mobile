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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.ButtonDefaults
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
data class QuizQuestion(
    val id: Int,
    val type: String,
    val question: String,
    val options: List<String>,
    val answer: String,
    val explanation: String
)

@Composable
fun QuizQuestionCard(
    question: QuizQuestion,
    selectedAnswer: String?,
    isAnswered: Boolean,
    onOptionClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            // Question type badge
            Surface(
                modifier = Modifier.padding(bottom = 16.dp),
                shape = RoundedCornerShape(50.dp),
                color = Color(0xFFF0F4FF)
            ) {
                Text(
                    text = question.type.replace("_", " ").uppercase(),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp
                    ),
                    color = Color(0xFF4648D4),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }

            // Question text
            Text(
                text = question.question,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                ),
                color = Color(0xFF1B1B23),
                lineHeight = 28.sp,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Options
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                question.options.forEachIndexed { index, option ->
                    val isSelected = selectedAnswer == option
                    val isCorrect = option == question.answer
                    val backgroundColor = when {
                        !isAnswered -> Color(0xFFF5F2FE)
                        isSelected && isCorrect -> Color(0xFFD1FAE5)
                        isSelected && !isCorrect -> Color(0xFFFEE2E2)
                        !isSelected && isCorrect -> Color(0xFFD1FAE5)
                        else -> Color(0xFFF5F2FE)
                    }
                    val borderColor = when {
                        !isAnswered -> Color(0xFFE4E1ED)
                        isSelected && isCorrect -> Color(0xFF10B981)
                        isSelected && !isCorrect -> Color(0xFFEF4444)
                        !isSelected && isCorrect -> Color(0xFF10B981)
                        else -> Color(0xFFE4E1ED)
                    }

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(enabled = !isAnswered) { onOptionClick(option) },
                        shape = RoundedCornerShape(12.dp),
                        color = backgroundColor,
                        border = BorderStroke(2.dp, borderColor)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = option,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 16.sp
                                ),
                                color = Color(0xFF1B1B23),
                                modifier = Modifier.weight(1f)
                            )
                            if (isAnswered && isCorrect) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Correct",
                                    tint = Color(0xFF10B981),
                                    modifier = Modifier.size(24.dp)
                                )
                            } else if (isAnswered && isSelected && !isCorrect) {
                                Icon(
                                    imageVector = Icons.Default.Error,
                                    contentDescription = "Wrong",
                                    tint = Color(0xFFEF4444),
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Explanation (show after answered)
            if (isAnswered) {
                Spacer(modifier = Modifier.height(20.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFFCF8FF),
                    border = BorderStroke(1.dp, Color(0xFFE4E1ED))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Giải thích:",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            ),
                            color = Color(0xFF4648D4)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = question.explanation,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 14.sp,
                                lineHeight = 20.sp
                            ),
                            color = Color(0xFF464554)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun QuizCompletionScreen(
    score: Int,
    totalQuestions: Int,
    onDoneClick: () -> Unit
) {
    val percentage = (score.toFloat() / totalQuestions * 100).toInt()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("🎉", fontSize = 64.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Hoàn thành!",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F2FE))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "$score / $totalQuestions",
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 48.sp
                    ),
                    color = Color(0xFF4648D4)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "$percentage% chính xác",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = 20.sp
                    ),
                    color = Color(0xFF464554)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onDoneClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4648D4))
        ) {
            Text(
                "Quay về Dashboard",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                ),
                color = Color.White
            )
        }
    }
}

@Composable
fun QuizScreen(
    questions: List<QuizQuestion>,
    onExitClick: () -> Unit,
    onComplete: () -> Unit
) {
    var currentIndex by remember { mutableStateOf(0) }
    var selectedAnswer by remember { mutableStateOf<String?>(null) }
    var isAnswered by remember { mutableStateOf(false) }
    var score by remember { mutableStateOf(0) }
    var isCompleted by remember { mutableStateOf(false) }

    if (isCompleted) {
        QuizCompletionScreen(
            score = score,
            totalQuestions = questions.size,
            onDoneClick = onComplete
        )
        return
    }

    val currentQuestion = questions[currentIndex]

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            // ✅ REUSE FlashCardTopBar từ FlashCardScreen
            FlashCardTopBar(
                currentProgress = currentIndex + 1,
                totalQuestion = questions.size,
                onExitClick = onExitClick,
                onMoreClick = { /* TODO: Handle more options */ }
            )
        },
        bottomBar = {
            // ✅ Footer giống FlashCardBottomBar nhưng với nút Next
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
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                ) {
                    Button(
                        onClick = {
                            if (currentIndex < questions.size - 1) {
                                currentIndex++
                                selectedAnswer = null
                                isAnswered = false
                            } else {
                                isCompleted = true
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        enabled = isAnswered,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4648D4),
                            disabledContainerColor = Color(0xFFE4E1ED)
                        )
                    ) {
                        Text(
                            text = if (currentIndex == questions.size - 1) "Hoàn thành" else "Câu tiếp theo",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            ),
                            color = Color.White
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F2FE))
                .verticalScroll(rememberScrollState())
        ) {
            QuizQuestionCard(
                question = currentQuestion,
                selectedAnswer = selectedAnswer,
                isAnswered = isAnswered,
                onOptionClick = { answer ->
                    selectedAnswer = answer
                    isAnswered = true
                    if (answer == currentQuestion.answer) {
                        score++
                    }
                }
            )
        }
    }
}

// Preview với data mẫu
@Composable
fun QuizScreenPreview() {
    val sampleQuestions = List(20) { index ->
        QuizQuestion(
            id = index + 1,
            type = if (index % 2 == 0) "fill_blank" else "multiple_choice",
            question = "Question ${index + 1}: She decided to ____ her old car because it was no longer safe.",
            options = listOf("abandon", "repair", "improve", "achieve"),
            answer = "abandon",
            explanation = "The verb 'abandon' means to leave something behind permanently."
        )
    }

    QuizScreen(
        questions = sampleQuestions,
        onExitClick = {},
        onComplete = {}
    )
}
