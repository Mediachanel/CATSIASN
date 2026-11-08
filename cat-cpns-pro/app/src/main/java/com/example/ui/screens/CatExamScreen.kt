package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.example.data.model.QuestionOption
import com.example.ui.components.CatTimerDisplay
import com.example.ui.components.QuestionNumberGrid
import com.example.ui.components.RealtimeScoreTickerBar
import com.example.ui.theme.CatActiveBlue
import com.example.ui.theme.CatActiveBlueBg
import com.example.ui.theme.CatAnsweredGreen
import com.example.ui.theme.CatAnsweredGreenBg
import com.example.ui.theme.CatDoubtfulAmber
import com.example.ui.theme.CatDoubtfulAmberBg
import com.example.ui.theme.CatUnansweredGray
import com.example.ui.theme.CatUnansweredGrayBg
import com.example.ui.theme.CrimsonRed
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.GoldSecondary
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyPrimary
import com.example.ui.viewmodel.ExamNavigationEvent
import com.example.ui.viewmodel.ExamViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatExamScreen(
    viewModel: ExamViewModel,
    packageId: String,
    onFinishNavigate: (Long) -> Unit,
    onBackNavigate: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(packageId) {
        if (state.currentPackage == null || state.currentPackage?.id != packageId || state.isExamFinished || packageId == "pkg_random_skd") {
            viewModel.startExam(packageId, forceNewSession = true)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is ExamNavigationEvent.NavigateToResult -> onFinishNavigate(event.resultId)
                is ExamNavigationEvent.NavigateBack -> onBackNavigate()
            }
        }
    }

    BackHandler {
        viewModel.showFinishConfirmation(true)
    }

    val currentQuestion = state.questions.getOrNull(state.currentIndex)
    val currentAnswer = currentQuestion?.let { state.answers[it.id] }
    val isDoubtful = currentAnswer?.isDoubtful == true
    val selectedOptionKey = currentAnswer?.selectedKey

    val answeredIndices = remember(state.answers, state.questions) {
        state.questions.mapIndexedNotNull { idx, q ->
            val ans = state.answers[q.id]?.selectedKey
            if (!ans.isNullOrEmpty()) idx else null
        }.toSet()
    }

    val doubtfulIndices = remember(state.answers, state.questions) {
        state.questions.mapIndexedNotNull { idx, q ->
            val ans = state.answers[q.id]
            if (ans?.isDoubtful == true) idx else null
        }.toSet()
    }

    BoxWithConstraints(modifier = modifier.fillMaxSize().testTag("cat_exam_screen")) {
        val isTabletOrPc = maxWidth >= 650.dp

        Scaffold(
            topBar = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(NavyDark)
                        .padding(horizontal = if (isTabletOrPc) 24.dp else 14.dp, vertical = 10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (isTabletOrPc) {
                                Icon(
                                    imageVector = Icons.Default.Computer,
                                    contentDescription = null,
                                    tint = GoldSecondary,
                                    modifier = Modifier.size(28.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                            }
                            Column {
                                Text(
                                    text = if (isTabletOrPc) "SIMULASI SISTEM CAT BKN (DESKTOP MODE)" else "SIMULASI CAT BKN",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Black,
                                        letterSpacing = 1.sp
                                    ),
                                    color = GoldSecondary
                                )
                                Text(
                                    text = state.currentPackage?.title ?: "Ujian SKD CPNS",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    ),
                                    maxLines = 1
                                )
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CatTimerDisplay(remainingSeconds = state.remainingTimeSeconds)

                            if (!isTabletOrPc) {
                                Spacer(modifier = Modifier.width(6.dp))
                                IconButton(
                                    onClick = { viewModel.toggleQuestionGridSheet(true) },
                                    modifier = Modifier.testTag("btn_open_question_grid")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.GridOn,
                                        contentDescription = "Daftar Soal",
                                        tint = Color.White
                                    )
                                }
                            }
                        }
                    }

                    if (!isTabletOrPc) {
                        Spacer(modifier = Modifier.height(6.dp))
                        RealtimeScoreTickerBar(
                            stats = state.realtimeStats,
                            isExpanded = state.showLiveScoreOverlay,
                            onToggleExpand = { viewModel.toggleLiveScoreOverlay() }
                        )
                    }
                }
            },
            bottomBar = {
                // Bottom Bar only rendered in compact mode; Tablet/PC has inline navigation controls
                if (!isTabletOrPc) {
                    Surface(
                        color = MaterialTheme.colorScheme.surface,
                        shadowElevation = 8.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        ExamBottomActionBar(
                            currentIndex = state.currentIndex,
                            totalQuestions = state.questions.size,
                            isDoubtful = isDoubtful,
                            onPrevious = { viewModel.previousQuestion() },
                            onToggleDoubtful = { viewModel.toggleDoubtful() },
                            onNext = { viewModel.nextQuestion() },
                            onFinish = { viewModel.showFinishConfirmation(true) }
                        )
                    }
                }
            }
        ) { innerPadding ->
            if (isTabletOrPc) {
                // ==================== TABLET & PC TWO-PANE CANONICAL LAYOUT ====================
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // LEFT PANE: Question card, options, and desktop controls
                    Column(
                        modifier = Modifier
                            .weight(1.35f)
                            .fillMaxHeight()
                            .verticalScroll(rememberScrollState())
                    ) {
                        if (currentQuestion != null) {
                            QuestionDetailCard(
                                question = currentQuestion,
                                totalQuestions = state.questions.size,
                                isBookmarked = state.isBookmarked,
                                onToggleBookmark = { viewModel.toggleBookmarkCurrent() }
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = "PILIHAN JAWABAN:",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Gray,
                                    letterSpacing = 1.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(6.dp))

                            currentQuestion.options.forEach { option ->
                                OptionSelectionCard(
                                    option = option,
                                    isSelected = selectedOptionKey == option.key,
                                    onClick = { viewModel.selectOption(option.key) },
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Desktop Navigation Action Bar
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.surface,
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                ExamBottomActionBar(
                                    currentIndex = state.currentIndex,
                                    totalQuestions = state.questions.size,
                                    isDoubtful = isDoubtful,
                                    onPrevious = { viewModel.previousQuestion() },
                                    onToggleDoubtful = { viewModel.toggleDoubtful() },
                                    onNext = { viewModel.nextQuestion() },
                                    onFinish = { viewModel.showFinishConfirmation(true) }
                                )
                            }
                        }
                    }

                    // RIGHT PANE: Candidate profile, Live Score BKN Monitor, and Persistent 1-110 Grid
                    Column(
                        modifier = Modifier
                            .weight(0.95f)
                            .fillMaxHeight()
                    ) {
                        // Desktop Live Score Monitor
                        RealtimeScoreTickerBar(
                            stats = state.realtimeStats,
                            isExpanded = true,
                            onToggleExpand = {},
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        // Candidate badge & Grid Card
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.AccountCircle,
                                            contentDescription = null,
                                            tint = NavyPrimary,
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Column {
                                            Text(
                                                text = "Peserta: SIM-CPNS-2026",
                                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                                            )
                                            Text(
                                                text = "Ruang Komputer CAT BKN",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = Color.Gray,
                                                fontSize = 9.sp
                                            )
                                        }
                                    }

                                    TextButton(
                                        onClick = { viewModel.showFinishConfirmation(true) },
                                        colors = ButtonDefaults.textButtonColors(contentColor = CrimsonRed)
                                    ) {
                                        Text("Selesai Ujian", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                // Grid Legend
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    LegendItem("Dijawab (${answeredIndices.size})", CatAnsweredGreen)
                                    LegendItem("Ragu (${doubtfulIndices.size})", CatDoubtfulAmber)
                                    LegendItem("Kosong (${state.questions.size - answeredIndices.size})", CatUnansweredGray)
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                // Persistent 1-110 Grid with internal scroll
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f)
                                        .verticalScroll(rememberScrollState())
                                ) {
                                    QuestionNumberGrid(
                                        totalQuestions = state.questions.size,
                                        currentIndex = state.currentIndex,
                                        answeredIndices = answeredIndices,
                                        doubtfulIndices = doubtfulIndices,
                                        onSelectIndex = { viewModel.jumpToQuestion(it) }
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                // ==================== MOBILE COMPACT LAYOUT ====================
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    if (currentQuestion != null) {
                        QuestionDetailCard(
                            question = currentQuestion,
                            totalQuestions = state.questions.size,
                            isBookmarked = state.isBookmarked,
                            onToggleBookmark = { viewModel.toggleBookmarkCurrent() }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "PILIHAN JAWABAN:",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.Gray,
                                letterSpacing = 1.sp
                            ),
                            modifier = Modifier.padding(start = 4.dp, bottom = 6.dp)
                        )

                        currentQuestion.options.forEach { option ->
                            OptionSelectionCard(
                                option = option,
                                isSelected = selectedOptionKey == option.key,
                                onClick = { viewModel.selectOption(option.key) },
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            TextButton(
                                onClick = { viewModel.showFinishConfirmation(true) },
                                colors = ButtonDefaults.textButtonColors(contentColor = CrimsonRed)
                            ) {
                                Text("Akhiri & Kumpulkan Ujian", fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            }
        }
    }

    // Modal BottomSheet for Mobile Grid navigation
    if (state.showQuestionGridSheet) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.toggleQuestionGridSheet(false) },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Navigasi Nomor Soal (${state.questions.size} Soal)",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    IconButton(onClick = { viewModel.toggleQuestionGridSheet(false) }) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Tutup")
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    LegendItem("Terjawab (${answeredIndices.size})", CatAnsweredGreen)
                    LegendItem("Ragu (${doubtfulIndices.size})", CatDoubtfulAmber)
                    LegendItem("Kosong (${state.questions.size - answeredIndices.size})", CatUnansweredGray)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(340.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    QuestionNumberGrid(
                        totalQuestions = state.questions.size,
                        currentIndex = state.currentIndex,
                        answeredIndices = answeredIndices,
                        doubtfulIndices = doubtfulIndices,
                        onSelectIndex = { viewModel.jumpToQuestion(it) }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    // Confirmation Finish Exam Dialog
    if (state.showFinishConfirmDialog) {
        val answeredCount = answeredIndices.size
        val doubtfulCount = doubtfulIndices.size
        val unansweredCount = state.questions.size - answeredCount

        AlertDialog(
            onDismissRequest = { viewModel.showFinishConfirmation(false) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = if (unansweredCount > 0) CrimsonRed else EmeraldSuccess,
                    modifier = Modifier.size(36.dp)
                )
            },
            title = {
                Text(
                    text = "Konfirmasi Selesai Ujian",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        text = "Apakah Anda yakin ingin mengakhiri sesi ujian simulasi ini?",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("• Sudah Dijawab: $answeredCount Soal", color = CatAnsweredGreen, fontWeight = FontWeight.Bold)
                            Text("• Masih Ragu-ragu: $doubtfulCount Soal", color = CatDoubtfulAmber, fontWeight = FontWeight.Bold)
                            Text("• Belum Dijawab: $unansweredCount Soal", color = CrimsonRed, fontWeight = FontWeight.Bold)
                        }
                    }
                    if (unansweredCount > 0) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Peringatan: Masih ada $unansweredCount soal belum dijawab. Nilai kosong tidak menghasilkan skor.",
                            style = MaterialTheme.typography.bodySmall,
                            color = CrimsonRed
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.finishExam() },
                    colors = ButtonDefaults.buttonColors(containerColor = CrimsonRed),
                    modifier = Modifier.testTag("btn_confirm_finish_dialog")
                ) {
                    Text("Ya, Selesai Sekarang")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { viewModel.showFinishConfirmation(false) }
                ) {
                    Text("Lanjut Mengerjakan")
                }
            }
        )
    }
}

@Composable
private fun QuestionDetailCard(
    question: Question,
    totalQuestions: Int,
    isBookmarked: Boolean,
    onToggleBookmark: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = when (question.category) {
                            ExamCategory.TWK -> NavyPrimary
                            ExamCategory.TIU -> GoldSecondary
                            ExamCategory.TKP -> EmeraldSuccess
                        }
                    ) {
                        Text(
                            text = "${question.category.code} (Min ${question.category.passingGrade})",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = question.subCategory,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Gray
                        )
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "No. ${question.number} / $totalQuestions",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace,
                            color = NavyPrimary
                        )
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    IconButton(
                        onClick = onToggleBookmark,
                        modifier = Modifier.size(32.dp).testTag("btn_bookmark_question")
                    ) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Simpan Soal",
                            tint = if (isBookmarked) GoldSecondary else Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = question.questionText,
                style = MaterialTheme.typography.bodyLarge.copy(
                    lineHeight = 24.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.testTag("question_text_statement")
            )
        }
    }
}

@Composable
private fun ExamBottomActionBar(
    currentIndex: Int,
    totalQuestions: Int,
    isDoubtful: Boolean,
    onPrevious: () -> Unit,
    onToggleDoubtful: () -> Unit,
    onNext: () -> Unit,
    onFinish: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        OutlinedButton(
            onClick = onPrevious,
            enabled = currentIndex > 0,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.testTag("btn_cat_previous")
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text("Sebelumnya")
        }

        Button(
            onClick = onToggleDoubtful,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isDoubtful) CatDoubtfulAmber else Color(0xFFF1F5F9),
                contentColor = if (isDoubtful) Color.White else Color(0xFF334155)
            ),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.testTag("btn_cat_doubtful")
        ) {
            Text(
                text = if (isDoubtful) "✓ Ragu-Ragu" else "Ragu-Ragu",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
        }

        if (currentIndex < totalQuestions - 1) {
            Button(
                onClick = onNext,
                colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("btn_cat_next")
            ) {
                Text("Selanjutnya")
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        } else {
            Button(
                onClick = onFinish,
                colors = ButtonDefaults.buttonColors(containerColor = CrimsonRed),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("btn_cat_finish_exam")
            ) {
                Text("Selesai Ujian", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun OptionSelectionCard(
    option: QuestionOption,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) CatActiveBlueBg else MaterialTheme.colorScheme.surface
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) CatActiveBlue else Color(0xFFCBD5E1)
        ),
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("option_${option.key}")
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) CatActiveBlue else Color(0xFFF1F5F9))
            ) {
                Text(
                    text = option.key,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = if (isSelected) Color.White else Color(0xFF334155)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = option.text,
                style = MaterialTheme.typography.bodyMedium.copy(
                    lineHeight = 22.sp,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun LegendItem(label: String, tintColor: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(tintColor)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = label, style = MaterialTheme.typography.labelSmall, fontSize = 10.sp)
    }
}
