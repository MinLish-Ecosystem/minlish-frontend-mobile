package com.minlish.app.presentation.screens.vocab

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.minlish.app.presentation.screens.library.WordListViewModel

private object AddWordColors {
    val Background      = Color(0xFFF9FAFB)
    val Surface         = Color(0xFFFFFFFF)
    val OnSurface       = Color(0xFF111827)
    val OnSurfaceVariant = Color(0xFF4B5563)
    val Outline         = Color(0xFFD1D5DB)
    val OutlineVariant  = Color(0xFFE5E7EB)
    val Primary         = Color(0xFF4F46E5)         
    val PrimaryContainer= Color(0xFFE0E7FF)           
    val OnPrimary       = Color(0xFFFFFFFF)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWordScreen(
    setId: String,
    onBack: () -> Unit,
    viewModel: WordListViewModel
) {
    val context = LocalContext.current

    var word            by remember { mutableStateOf("") }
    var pronunciation   by remember { mutableStateOf("") }
    var meaning         by remember { mutableStateOf("") }
    var descriptionEN   by remember { mutableStateOf("") }
    var example         by remember { mutableStateOf("") }
    var collocation     by remember { mutableStateOf("") }
    var relatedWord     by remember { mutableStateOf("") }
    var note            by remember { mutableStateOf("") }

    val isAdding by remember { viewModel.isAdding }
    val addSuccess by remember { viewModel.addSuccess }
    val isLookingUp by remember { viewModel.isLookingUp }
    val errorMessage by remember { viewModel.errorMessage }

    LaunchedEffect(addSuccess) {
        if (addSuccess) {
            Toast.makeText(context, "Word added successfully!", Toast.LENGTH_SHORT).show()
            viewModel.resetAddSuccess()
            onBack()
        }
    }

    LaunchedEffect(errorMessage) {
        errorMessage?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
        }
    }

    Scaffold(
        containerColor = AddWordColors.Background,
        topBar = {
            Surface(
                color = AddWordColors.Surface.copy(alpha = 0.9f),
                shadowElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsPadding(WindowInsets.statusBars)
                        .padding(horizontal = 8.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = AddWordColors.OnSurfaceVariant
                        )
                    }
                    Text(
                        text = "Add Vocabulary Word",
                        color = AddWordColors.OnSurface,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        },
        bottomBar = {
            Surface(
                color = AddWordColors.Surface,
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Button(
                        onClick = {
                            if (word.isBlank() || meaning.isBlank()) {
                                Toast.makeText(context, "Word and Meaning are required!", Toast.LENGTH_SHORT).show()
                            } else {
                                viewModel.addWord(
                                    setId = setId,
                                    word = word,
                                    pronunciation = pronunciation,
                                    meaning = meaning,
                                    descriptionEN = descriptionEN,
                                    example = example,
                                    collocation = collocation,
                                    relatedWord = relatedWord,
                                    note = note
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AddWordColors.Primary,
                            contentColor = AddWordColors.OnPrimary
                        ),
                        enabled = !isAdding && word.isNotBlank() && meaning.isNotBlank()
                    ) {
                        if (isAdding) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = "Save Word",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Word Information",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = AddWordColors.Primary
                )
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Word *",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AddWordColors.OnSurface
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = word,
                            onValueChange = { word = it },
                            placeholder = { Text("e.g. ubiquitous") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = AddWordColors.Surface,
                                unfocusedContainerColor = AddWordColors.Surface,
                                focusedBorderColor = AddWordColors.Primary,
                                unfocusedBorderColor = AddWordColors.Outline
                            ),
                            singleLine = true
                        )

                        Button(
                            onClick = {
                                if (word.isNotBlank()) {
                                    viewModel.lookupWord(word) { result ->
                                        pronunciation = result.phonetic ?: ""
                                        val def = result.meanings.firstOrNull()?.definitions?.firstOrNull()
                                        meaning = def?.definition ?: ""
                                        descriptionEN = def?.definition ?: ""
                                        example = result.meanings.flatMap { it.definitions }
                                            .firstOrNull { !it.example.isNullOrBlank() }?.example ?: ""
                                        Toast.makeText(context, "Details auto-filled!", Toast.LENGTH_SHORT).show()
                                    }
                                } else {
                                    Toast.makeText(context, "Please enter a word first!", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier.height(56.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AddWordColors.PrimaryContainer,
                                contentColor = AddWordColors.Primary
                            ),
                            contentPadding = PaddingValues(horizontal = 12.dp),
                            enabled = !isLookingUp
                        ) {
                            if (isLookingUp) {
                                CircularProgressIndicator(
                                    color = AddWordColors.Primary,
                                    modifier = Modifier.size(20.dp),
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = "Auto-fill",
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Auto-fill",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }

            item {
                InputField(
                    label = "Pronunciation",
                    value = pronunciation,
                    onValueChange = { pronunciation = it },
                    placeholder = "e.g. /juːˈbɪkwɪtəs/"
                )
            }

            item {
                InputField(
                    label = "Meaning *",
                    value = meaning,
                    onValueChange = { meaning = it },
                    placeholder = "e.g. có mặt ở khắp nơi / seeming to be everywhere"
                )
            }

            item {
                InputField(
                    label = "Description (English)",
                    value = descriptionEN,
                    onValueChange = { descriptionEN = it },
                    placeholder = "Detailed explanation in English",
                    singleLine = false,
                    maxLines = 4
                )
            }

            item {
                InputField(
                    label = "Example",
                    value = example,
                    onValueChange = { example = it },
                    placeholder = "e.g. Mobile phones are ubiquitous nowadays.",
                    singleLine = false,
                    maxLines = 3
                )
            }

            item {
                InputField(
                    label = "Collocation",
                    value = collocation,
                    onValueChange = { collocation = it },
                    placeholder = "e.g. ubiquitous presence"
                )
            }

            item {
                InputField(
                    label = "Related words",
                    value = relatedWord,
                    onValueChange = { relatedWord = it },
                    placeholder = "e.g. omnipresent, widespread"
                )
            }

            item {
                InputField(
                    label = "Note",
                    value = note,
                    onValueChange = { note = it },
                    placeholder = "Any personal usage notes",
                    singleLine = false,
                    maxLines = 3
                )
            }
        }
    }
}

@Composable
private fun InputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    singleLine: Boolean = true,
    maxLines: Int = 1
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = AddWordColors.OnSurface
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = AddWordColors.OnSurfaceVariant.copy(alpha = 0.5f)) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = AddWordColors.Surface,
                unfocusedContainerColor = AddWordColors.Surface,
                focusedBorderColor = AddWordColors.Primary,
                unfocusedBorderColor = AddWordColors.Outline
            ),
            singleLine = singleLine,
            maxLines = maxLines
        )
    }
}
