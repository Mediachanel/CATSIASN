package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ExamCategory
import com.example.data.model.Question
import com.example.data.model.UserAnswer
import com.example.ui.theme.CatAnsweredGreen
import com.example.ui.theme.CatAnsweredGreenBg
import com.example.ui.theme.CatDoubtfulAmber
import com.example.ui.theme.CatDoubtfulAmberBg
import com.example.ui.theme.CrimsonRed
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.GoldSecondary
import com.example.ui.theme.NavyPrimary
import com.example.ui.viewmodel.MainViewModel

enum class ReviewFilter {
    ALL,
    WRONG_ONLY,
    CORRECT_ONLY,
    DOUBTFUL_ONLY
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewExamScreen(
    viewModel: MainViewModel,
    resultId: Long,
    onBackNavigate: () -> Unit,
    modifier: Modifier = Modifier
) {
    val detailedState by viewModel.detailedResultState.collectAsState()
    var selectedFilter by remember { mutableStateOf(ReviewFilter.ALL) }

    LaunchedEffect(resultId) {
        viewModel.loadDetailedResult(resultId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Pembahasan & Kunci Jawaban",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackNavigate) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        modifier = modifier.testTag("review_exam_screen")
    ) { innerPadding ->
        if (detailedState.isLoading) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                CircularProgressIndicator()
            }
        } else {
            val questions = detailedState.questions
            val answers = detailedState.userAnswers

            val filteredQuestions = remember(questions, answers, selectedFilter) {
                when (selectedFilter) {
                    ReviewFilter.ALL -> questions
                    ReviewFilter.WRONG_ONLY -> questions.filter { q ->
                        val ans = answers[q.id]?.selectedKey
                        if (q.category == ExamCategory.TKP) {
                            val score = q.options.find { it.key == ans }?.score ?: 0
                            score < 5 // Not max points
                        } else {
                            ans != q.correctKey
                        }
                    }
                    ReviewFilter.CORRECT_ONLY -> questions.filter { q ->
                        val ans = answers[q.id]?.selectedKey
                        if (q.category == ExamCategory.TKP) {
                            val score = q.options.find { it.key == ans }?.score ?: 0
                            score == 5
                        } else {
                            ans == q.correctKey
                        }
                    }
                    ReviewFilter.DOUBTFUL_ONLY -> questions.filter { q ->
                        answers[q.id]?.isDoubtful == true
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.TopCenter
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .widthIn(max = 920.dp)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    // Filter Chips
                item {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                    ) {
                        FilterChip(
                            selected = selectedFilter == ReviewFilter.ALL,
                            onClick = { selectedFilter = ReviewFilter.ALL },
                            label = { Text("Semua (${questions.size})", fontSize = 11.sp) }
                        )
                        FilterChip(
                            selected = selectedFilter == ReviewFilter.WRONG_ONLY,
                            onClick = { selectedFilter = ReviewFilter.WRONG_ONLY },
                            label = { Text("Perlu Review", fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Color(0xFFFEE2E2))
                        )
                        FilterChip(
                            selected = selectedFilter == ReviewFilter.CORRECT_ONLY,
                            onClick = { selectedFilter = ReviewFilter.CORRECT_ONLY },
                            label = { Text("Tepat/Poin 5", fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Color(0xFFDCFCE7))
                        )
                    }
                }

                if (filteredQuestions.isEmpty()) {
                    item {
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 24.dp)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp)
                            ) {
                                Text("Tidak ada soal pada kategori filter ini.")
                            }
                        }
                    }
                } else {
                    items(filteredQuestions) { q ->
                        val userAnswer = answers[q.id]
                        ReviewQuestionCard(
                            question = q,
                            userAnswer = userAnswer,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
    }
}

@Composable
private fun ReviewQuestionCard(
    question: Question,
    userAnswer: UserAnswer?,
    modifier: Modifier = Modifier
) {
    val selectedKey = userAnswer?.selectedKey
    val isTkp = question.category == ExamCategory.TKP
    val userScore = if (isTkp) {
        question.options.find { it.key == selectedKey }?.score ?: 0
    } else {
        if (selectedKey == question.correctKey) 5 else 0
    }
    val isPerfect = if (isTkp) userScore == 5 else userScore == 5

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isPerfect) Color(0xFF86EFAC) else Color(0xFFFECDD3)
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header info
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = when (question.category) {
                            ExamCategory.TWK -> NavyPrimary
                            ExamCategory.TIU -> GoldSecondary
                            ExamCategory.TKP -> EmeraldSuccess
                        }
                    ) {
                        Text(
                            text = question.category.code,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = question.subCategory,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray
                    )
                }

                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = if (isPerfect) Color(0xFFDCFCE7) else Color(0xFFFEE2E2)
                ) {
                    Text(
                        text = if (isTkp) "Poin: $userScore / 5" else if (isPerfect) "Benar (+5)" else "Salah (0)",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = if (isPerfect) Color(0xFF15803D) else CrimsonRed,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Soal No. ${question.number}",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                    color = NavyPrimary
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = question.questionText,
                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Options list with points/correct indicator
            question.options.forEach { opt ->
                val isUserPick = opt.key == selectedKey
                val isCorrectKey = !isTkp && opt.key == question.correctKey
                val isMaxTkp = isTkp && opt.score == 5

                val bgColor = when {
                    isUserPick && (isCorrectKey || isMaxTkp) -> Color(0xFFDCFCE7)
                    isUserPick && !(isCorrectKey || isMaxTkp) -> Color(0xFFFEE2E2)
                    isCorrectKey || isMaxTkp -> Color(0xFFF0FDF4)
                    else -> Color(0xFFF8FAFC)
                }

                val borderColor = when {
                    isUserPick && (isCorrectKey || isMaxTkp) -> Color(0xFF22C55E)
                    isUserPick && !(isCorrectKey || isMaxTkp) -> CrimsonRed
                    isCorrectKey || isMaxTkp -> Color(0xFF86EFAC)
                    else -> Color.Transparent
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(bgColor)
                        .border(1.dp, borderColor, RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 8.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(26.dp)
                            .clip(CircleShape)
                            .background(
                                when {
                                    isUserPick -> if (isCorrectKey || isMaxTkp) EmeraldSuccess else CrimsonRed
                                    isCorrectKey || isMaxTkp -> EmeraldSuccess
                                    else -> Color(0xFFE2E8F0)
                                }
                            )
                    ) {
                        Text(
                            text = opt.key,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = if (isUserPick || isCorrectKey || isMaxTkp) Color.White else Color.Black
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = opt.text,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.weight(1f)
                    )

                    if (isTkp) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = when (opt.score) {
                                5 -> EmeraldSuccess
                                4 -> Color(0xFF3B82F6)
                                3 -> GoldSecondary
                                else -> Color.Gray
                            }
                        ) {
                            Text(
                                text = "${opt.score} Poin",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp),
                                fontSize = 9.sp
                            )
                        }
                    } else if (isCorrectKey) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Kunci Benar",
                            tint = EmeraldSuccess,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Comprehensive Explanation Box
            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = "PEMBAHASAN RESMI:",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = NavyPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = question.explanation,
                        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 20.sp),
                        color = Color(0xFF334155)
                    )

                    if (question.fastTips.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Lightbulb,
                                contentDescription = null,
                                tint = GoldSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Tips & Rumus Sakti Bimbel:",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = GoldSecondary
                            )
                        }
                        Text(
                            text = question.fastTips,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF78350F)
                            ),
                            fontSize = 11.sp,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }
            }
        }
    }
}
