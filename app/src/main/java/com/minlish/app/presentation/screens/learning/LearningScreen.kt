package com.minlish.app.presentation.screens.learning


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import androidx.compose.ui.draw.drawBehind

import androidx.compose.ui.graphics.PathEffect

import androidx.compose.ui.graphics.drawscope.Stroke
import com.minlish.app.presentation.components.AppHeader
import com.minlish.app.presentation.components.NotificationColors
import com.minlish.app.ui.components.Footer


// for ui

@Composable
fun HeroSessionCard(newWords: Int,reviewsDue: Int,onStartSessionClick: () -> Unit){
    var isStartSessionActivated by remember {mutableStateOf(false)}
    val scope = rememberCoroutineScope()
    Card(
        modifier=Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        shape=RoundedCornerShape(16.dp),
        colors= CardDefaults.cardColors(containerColor = Color.White),
        elevation= CardDefaults.cardElevation(defaultElevation = 4.dp),
    ){
        Box(
            modifier=Modifier.background(Brush.linearGradient(colors= listOf(Color(0xFFF0F4FF), Color.White))).padding(24.dp)
        ){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement= Arrangement.spacedBy(16.dp),
            ) {
                Text(
                    text = "Today's Session",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = Color.Black,
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(32.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    StatItem(label = "New Words", value = newWords)
                    Divider(
                        color = Color(0xFFC7C4D7),
                        thickness = 1.dp,
                        modifier = Modifier
                            .height(40.dp)
                            .width(1.dp)
                    )
                    StatItem(label = "Reviews Due", value = reviewsDue)
                }
                Button(
                    onClick={
                        isStartSessionActivated=true
                        onStartSessionClick()
                        scope.launch{
                            kotlinx.coroutines.delay(150)
                            isStartSessionActivated=false
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(65.dp),
                    shape= RoundedCornerShape(12.dp),
                    colors= ButtonDefaults.buttonColors(containerColor =if (isStartSessionActivated) Color(0xFF5C7CFA) else Color.Blue),
                ){
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = Icons.Filled.PlayArrow,
                            contentDescription = "Start Session",
                            tint = Color.White,
                            modifier=Modifier.size(26.dp)
                        )
                        Text("Start Session", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}
@Composable
private fun StatItem(label: String, value: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally,modifier = Modifier.width(IntrinsicSize.Min)) {
        Text(text = value.toString(), fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1B1B23))
        Text(text = label, fontSize = 12.sp, color = Color(0xFF464554), letterSpacing = 0.5.sp)
    }
}
// viết thêm cái click để trả tham s
@Composable
fun VocabSetCard(set: VocabSet,modifier: Modifier){
    Card(
        modifier = modifier.width(160.dp).height(140.dp).clickable{}, // viết sau
        shape=RoundedCornerShape(12.dp),
        colors= CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ){
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .background(
                        Color(set.colorHex)
                    )
            )
            Box(
                modifier = Modifier.padding(16.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color(set.colorHex).copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = set.iconName,
                                contentDescription = set.title,
                                modifier = Modifier.size(24.dp),
                                tint=Color(set.colorHex)
                            )
                        }
                        if (set.isDueToday) {
                            Text(
                                text = "Due today",
                                fontSize = 10.sp,
                                color = Color.White,
                                modifier = Modifier
                                    .background(Color(0xFFEF4444), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = set.title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1B1B23),
                        maxLines = 1
                    )
                    Text(
                        text = "${set.wordCount} words",
                        fontSize = 11.sp,
                        color = Color(0xFF464554),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CreateNewSetCard(onCreateNewSetCard: () -> Unit,modifier: Modifier){
    Card(
        modifier = modifier.width(160.dp).height(140.dp).drawBehind {
            drawRoundRect(
                color = Color(0xFFB8B5C9),
                style = Stroke(
                    width = 3f,
                    pathEffect =
                        PathEffect.dashPathEffect(
                            floatArrayOf(12f, 12f)
                        )
                ),
                cornerRadius = CornerRadius(
                    24f,
                    24f
                )
            )
        }.clickable{onCreateNewSetCard()}, // viết sau
        shape=RoundedCornerShape(12.dp),
        colors= CardDefaults.cardColors(containerColor = Color(0xFFF5F2FE)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ){
        Box(contentAlignment=Alignment.Center, modifier=Modifier.fillMaxSize()){
            Column(horizontalAlignment= Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Notifications",
                    tint = Color.Black,
                    )
                Text("Create Set", fontSize = 12.sp, color = Color(0xFF464554),modifier = Modifier.padding(top = 4.dp))
            }
        }
    }
}

@Composable
fun LearningDashBoardScreen(currentRoute: String, viewModel: LearningViewModel ,onNavigate: (String) -> Unit){

    val vocabSets by viewModel.vocabSets
    val newWords by viewModel.newWordsCount
    val reviewsDue by viewModel.reviewsDueCount
    val loading by viewModel.isLoading
    val error by viewModel.errorMessage

    LaunchedEffect(Unit) {
        viewModel.loadDashBoardData("user_001")
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppHeader(
                userName = "QuangLe",
                userAvatarUrl = "https://api.dicebear.com/7.x/avataaars/png?seed=QuangLe",
                onNotificationClick = {}
            )
        },
        bottomBar = {
            Footer(
                currentRoute = currentRoute,
                onNavigate = onNavigate
            )
        }
    ) { paddingValues ->
        // Handle Loading
        if (loading && vocabSets.isEmpty()) {
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
                Button(onClick = { viewModel.loadDashBoardData("user_001") }) {
                    Text("Thử lại")
                }
            }
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item{
                HeroSessionCard(newWords=newWords, reviewsDue =reviewsDue , onStartSessionClick = {})
            }
            item{
                Text(
                    text = "Your Recent Sets",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
            item {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val totalItems= vocabSets.size
                    val isOdd=totalItems % 2 != 0
                    val fullRowsCount = if (isOdd) totalItems - 1 else totalItems
                    for (i in 0 until fullRowsCount step 2) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            VocabSetCard(
                                set = vocabSets[i],
                                modifier = Modifier.weight(1f)
                            )
                            VocabSetCard(
                                set = vocabSets[i + 1],
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                    if (isOdd) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            VocabSetCard(
                                set = vocabSets.last(),
                                modifier = Modifier.weight(1f)
                            )
                            CreateNewSetCard(
                                onCreateNewSetCard = {},
                                modifier = Modifier.weight(1f)
                            )
                        }
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            CreateNewSetCard(
                                onCreateNewSetCard = {},
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}
