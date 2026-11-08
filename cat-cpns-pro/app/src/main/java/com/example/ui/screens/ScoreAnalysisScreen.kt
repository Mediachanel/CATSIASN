package com.example.ui.screens

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.db.ExamResultEntity
import com.example.data.model.AccuracyStatistics
import com.example.data.model.ExamCategory
import com.example.data.model.ImprovementRecommendation
import com.example.data.model.PriorityLevel
import com.example.data.model.SubCategoryMastery
import com.example.ui.theme.CrimsonRed
import com.example.ui.theme.EmeraldContainer
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.GoldContainer
import com.example.ui.theme.GoldSecondary
import com.example.ui.theme.NavyContainer
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.OnEmeraldContainer
import com.example.ui.theme.OnGoldContainer
import com.example.ui.theme.OnNavyContainer
import com.example.ui.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoreAnalysisScreen(
    viewModel: MainViewModel,
    resultId: Long,
    onReviewQuestions: (Long) -> Unit,
    onRetakeExam: (String) -> Unit,
    onBackToHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    val detailedState by viewModel.detailedResultState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(resultId) {
        viewModel.loadDetailedResult(resultId)
    }

    BackHandler {
        onBackToHome()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Analisis Skor & Evaluasi Lanjutan",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackToHome) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali ke Beranda"
                        )
                    }
                },
                actions = {
                    val result = detailedState.resultEntity
                    if (result != null) {
                        IconButton(
                            onClick = {
                                val acc = detailedState.accuracyStats
                                val shareText = buildString {
                                    append("Rapor Hasil Simulasi CAT SKD CPNS BKN\n")
                                    append("Paket: ${result.packageName}\n")
                                    append("Status: ${if (result.isPassed) "LULUS PASSING GRADE ✅" else "BELUM LULUS PG ❌"}\n")
                                    append("Total Skor: ${result.totalScore} / 550\n")
                                    append("• TWK: ${result.twkScore} (Ambang Batas: 65)\n")
                                    append("• TIU: ${result.tiuScore} (Ambang Batas: 80)\n")
                                    append("• TKP: ${result.tkpScore} (Ambang Batas: 166)\n")
                                    if (acc != null) {
                                        append("Akurasi Jawaban: ${acc.overallAccuracyPct}%\n")
                                    }
                                    append("\nSimulasi CAT CPNS Pro - Standar Resmi MenPAN-RB.")
                                }
                                val sendIntent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TEXT, shareText)
                                    type = "text/plain"
                                }
                                context.startActivity(Intent.createChooser(sendIntent, "Bagikan Rapor Hasil"))
                            },
                            modifier = Modifier.testTag("btn_share_score_report")
                        ) {
                            Icon(imageVector = Icons.Default.Share, contentDescription = "Bagikan")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        bottomBar = {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 12.dp)
                        .widthIn(max = 1200.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            detailedState.resultEntity?.packageId?.let { onRetakeExam(it) }
                        },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("btn_retake_exam")
                    ) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Simulasi Ulang")
                    }

                    Button(
                        onClick = { onReviewQuestions(resultId) },
                        colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("btn_review_questions")
                    ) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.MenuBook, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Pembahasan Soal", fontWeight = FontWeight.Bold)
                    }
                }
            }
        },
        modifier = modifier.testTag("score_analysis_screen")
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
            val result = detailedState.resultEntity
            val accuracy = detailedState.accuracyStats
            val recommendations = detailedState.recommendations

            if (result == null) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    Text("Data hasil ujian tidak ditemukan.")
                }
            } else {
                val dateStr = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale.getDefault())
                    .format(Date(result.timestamp))
                val timeMinutes = result.timeSpentSeconds / 60
                val timeSeconds = result.timeSpentSeconds % 60
                val avgSecPerQuestion = if (result.answeredCount > 0) {
                    result.timeSpentSeconds / result.answeredCount
                } else 0

                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    val isTabletOrPc = maxWidth >= 720.dp

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        if (isTabletOrPc) {
                            // ==================== TABLET & PC TWO-COLUMN LAYOUT ====================
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .widthIn(max = 1280.dp)
                                    .padding(horizontal = 24.dp, vertical = 12.dp),
                                horizontalArrangement = Arrangement.spacedBy(20.dp)
                            ) {
                                // LEFT COLUMN: Certificate card and passing grade subtest breakdown
                                Column(
                                    modifier = Modifier
                                        .weight(1.1f)
                                        .verticalScroll(rememberScrollState())
                                ) {
                                    OfficialCertificateCard(result = result, dateStr = dateStr)

                                    Spacer(modifier = Modifier.height(14.dp))

                                    SubtestScoreSection(result = result)
                                }

                                // RIGHT COLUMN: Accuracy statistics, Recommendations, Time Pacing, Mastery
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .verticalScroll(rememberScrollState())
                                ) {
                                    if (accuracy != null) {
                                        AccuracySection(accuracy = accuracy, timeMinutes = timeMinutes, timeSeconds = timeSeconds)
                                        Spacer(modifier = Modifier.height(14.dp))
                                    }

                                    RecommendationsSection(
                                        recommendations = recommendations,
                                        onDrillAction = onRetakeExam
                                    )

                                    Spacer(modifier = Modifier.height(14.dp))

                                    TimeDiagnosticsCard(
                                        timeMinutes = timeMinutes,
                                        timeSeconds = timeSeconds,
                                        avgSecPerQuestion = avgSecPerQuestion
                                    )

                                    Spacer(modifier = Modifier.height(14.dp))

                                    MasterySection(masteryList = detailedState.subCategoryMastery)

                                    Spacer(modifier = Modifier.height(24.dp))
                                }
                            }
                        } else {
                            // ==================== MOBILE COMPACT SINGLE COLUMN ====================
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                item {
                                    OfficialCertificateCard(result = result, dateStr = dateStr)
                                }

                                if (accuracy != null) {
                                    item {
                                        Spacer(modifier = Modifier.height(10.dp))
                                        AccuracySection(accuracy = accuracy, timeMinutes = timeMinutes, timeSeconds = timeSeconds)
                                    }
                                }

                                item {
                                    Spacer(modifier = Modifier.height(10.dp))
                                    SubtestScoreSection(result = result)
                                }

                                item {
                                    Spacer(modifier = Modifier.height(10.dp))
                                    RecommendationsSection(
                                        recommendations = recommendations,
                                        onDrillAction = onRetakeExam
                                    )
                                }

                                item {
                                    Spacer(modifier = Modifier.height(10.dp))
                                    TimeDiagnosticsCard(
                                        timeMinutes = timeMinutes,
                                        timeSeconds = timeSeconds,
                                        avgSecPerQuestion = avgSecPerQuestion
                                    )
                                }

                                item {
                                    Spacer(modifier = Modifier.height(10.dp))
                                    MasterySection(masteryList = detailedState.subCategoryMastery)
                                }

                                item {
                                    Spacer(modifier = Modifier.height(24.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun OfficialCertificateCard(result: ExamResultEntity, dateStr: String) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("official_certificate_card")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                shape = RoundedCornerShape(4.dp),
                color = GoldSecondary
            ) {
                Text(
                    text = "BADAN KEPEGAWAIAN NEGARA • SIMULASI RESMI",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    ),
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = result.packageName,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFE2E8F0)
                ),
                textAlign = TextAlign.Center
            )

            Text(
                text = dateStr,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF94A3B8)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Passing Grade Verdict Banner
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = if (result.isPassed) Color(0xFF14532D) else Color(0xFF7F1D1D),
                border = androidx.compose.foundation.BorderStroke(
                    1.5.dp,
                    if (result.isPassed) EmeraldSuccess else CrimsonRed
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = if (result.isPassed) Icons.Default.CheckCircle else Icons.Default.Close,
                        contentDescription = null,
                        tint = if (result.isPassed) Color(0xFF86EFAC) else Color(0xFFFCA5A5),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (result.isPassed) "MEMENUHI AMBANG BATAS (LULUS)" else "TIDAK MEMENUHI AMBANG BATAS",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.5.sp
                        ),
                        color = if (result.isPassed) Color(0xFF86EFAC) else Color(0xFFFCA5A5)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "${result.totalScore}",
                style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace
                ),
                color = Color(0xFF38BDF8)
            )
            Text(
                text = "SKOR AKHIR SKD (MAKSIMAL 550)",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                ),
                color = Color(0xFF94A3B8)
            )
        }
    }
}

@Composable
private fun SubtestScoreSection(result: ExamResultEntity) {
    Column {
        Text(
            text = "Skor Per Bagian & Ambang Batas BKN",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(8.dp))

        ScoreSubtestCard(
            title = "TWK (Wawasan Kebangsaan)",
            score = result.twkScore,
            passingGrade = 65,
            maxScore = 150,
            isPassed = result.twkPassed,
            description = "Mencakup Nasionalisme, Integritas, Bela Negara, Pilar Bangsa, dan Bahasa Indonesia."
        )

        Spacer(modifier = Modifier.height(8.dp))

        ScoreSubtestCard(
            title = "TIU (Inteligensia Umum)",
            score = result.tiuScore,
            passingGrade = 80,
            maxScore = 175,
            isPassed = result.tiuPassed,
            description = "Mencakup Analogi, Silogisme, Analitis, Deret Angka, Numerik, dan Figural."
        )

        Spacer(modifier = Modifier.height(8.dp))

        ScoreSubtestCard(
            title = "TKP (Karakteristik Pribadi)",
            score = result.tkpScore,
            passingGrade = 166,
            maxScore = 225,
            isPassed = result.tkpPassed,
            description = "Mencakup Pelayanan Publik, Jejaring Kerja, TIK, Profesionalisme, dan Anti-Radikalisme."
        )
    }
}

@Composable
private fun AccuracySection(
    accuracy: AccuracyStatistics,
    timeMinutes: Int,
    timeSeconds: Int
) {
    Column {
        Text(
            text = "Statistik Persentase Jawaban Benar",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            AccuracyMetricCard(
                label = "TWK Benar",
                percentage = "${accuracy.twkAccuracyPct}%",
                detail = "${accuracy.twkCorrectCount}/${accuracy.twkTotal} Soal",
                containerColor = NavyContainer,
                textColor = OnNavyContainer,
                modifier = Modifier.weight(1f)
            )
            AccuracyMetricCard(
                label = "TIU Benar",
                percentage = "${accuracy.tiuAccuracyPct}%",
                detail = "${accuracy.tiuCorrectCount}/${accuracy.tiuTotal} Soal",
                containerColor = GoldContainer,
                textColor = OnGoldContainer,
                modifier = Modifier.weight(1f)
            )
            AccuracyMetricCard(
                label = "TKP Poin 5",
                percentage = "${accuracy.tkpPerfectPct}%",
                detail = "${accuracy.tkpPerfectCount}/${accuracy.tkpTotal} Soal",
                containerColor = EmeraldContainer,
                textColor = OnEmeraldContainer,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(14.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(NavyPrimary.copy(alpha = 0.1f))
                ) {
                    Icon(
                        imageVector = Icons.Default.Percent,
                        contentDescription = null,
                        tint = NavyPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Akurasi Jawaban Keseluruhan: ${accuracy.overallAccuracyPct}%",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "Total ${accuracy.totalAnsweredCount} dari ${accuracy.totalQuestionsCount} soal dijawab dalam $timeMinutes m $timeSeconds s.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
private fun RecommendationsSection(
    recommendations: List<ImprovementRecommendation>,
    onDrillAction: (String) -> Unit
) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                contentDescription = null,
                tint = GoldSecondary,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Rekomendasi Area yang Perlu Ditingkatkan",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        }
        Text(
            text = "Rencana aksi perbaikan belajar cerdas berdasarkan kelemahan yang terdeteksi.",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (recommendations.isEmpty()) {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF86EFAC)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(14.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = EmeraldSuccess,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Luar biasa! Penguasaan materi Anda di seluruh sub-kategori sangat merata dan prima (≥ 80%). Pertahankan kecepatan dan ketepatan Anda.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF14532D)
                    )
                }
            }
        } else {
            recommendations.forEach { rec ->
                RecommendationCard(
                    recommendation = rec,
                    onDrillAction = onDrillAction,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun TimeDiagnosticsCard(
    timeMinutes: Int,
    timeSeconds: Int,
    avgSecPerQuestion: Int
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Speed,
                    contentDescription = null,
                    tint = NavyPrimary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Analisis Manajemen Waktu & Pacing",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TimeMetricItem("Total Waktu", String.format("%d m %02d s", timeMinutes, timeSeconds))
                TimeMetricItem("Kecepatan Rata-rata", "$avgSecPerQuestion dtk/soal")
                TimeMetricItem("Standar BKN", "54 dtk/soal")
            }

            Spacer(modifier = Modifier.height(10.dp))

            val pacingAdvice = when {
                avgSecPerQuestion <= 45 -> "Kecepatan pengerjaan Anda sangat optimal! Anda memiliki cadangan waktu yang baik untuk memeriksa soal-soal ragu."
                avgSecPerQuestion <= 55 -> "Manajemen waktu Anda stabil sesuai ritme standar ujian BKN."
                else -> "Perhatian: Anda membutuhkan waktu relatif lama per soal (>55 dtk). Latihlah rumus sakti TIU untuk menghemat waktu."
            }

            Text(
                text = pacingAdvice,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF475569)
            )
        }
    }
}

@Composable
private fun MasterySection(masteryList: List<SubCategoryMastery>) {
    Column {
        Text(
            text = "Detail Pemahaman Per Topik Materi",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = "Evaluasi persentase pemahaman per sub-kategori soal",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(8.dp))

        masteryList.forEach { mastery ->
            MasteryProgressRow(mastery = mastery, modifier = Modifier.padding(vertical = 4.dp))
        }
    }
}

@Composable
private fun AccuracyMetricCard(
    label: String,
    percentage: String,
    detail: String,
    containerColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = textColor.copy(alpha = 0.85f),
                fontSize = 10.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = percentage,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace
                ),
                color = textColor
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = detail,
                style = MaterialTheme.typography.labelSmall,
                color = textColor.copy(alpha = 0.75f),
                fontSize = 9.sp
            )
        }
    }
}

@Composable
private fun RecommendationCard(
    recommendation: ImprovementRecommendation,
    onDrillAction: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isHigh = recommendation.priority == PriorityLevel.HIGH
    val borderColor = if (isHigh) Color(0xFFFCA5A5) else Color(0xFFFDE68A)
    val badgeColor = if (isHigh) CrimsonRed else GoldSecondary

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = badgeColor
                    ) {
                        Text(
                            text = if (isHigh) "PRIORITAS TINGGI" else "PERLU DIASAH",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Black),
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            fontSize = 9.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${recommendation.category.code} • ${recommendation.topic}",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }

                Text(
                    text = "${recommendation.currentPercentage}%",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        color = if (isHigh) CrimsonRed else GoldSecondary
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Kendala: ${recommendation.diagnosis}",
                style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF475569)),
                fontSize = 11.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Surface(
                shape = RoundedCornerShape(6.dp),
                color = Color(0xFFF8FAFC),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(8.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.Lightbulb,
                        contentDescription = null,
                        tint = GoldSecondary,
                        modifier = Modifier
                            .size(16.dp)
                            .padding(top = 2.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Rencana Aksi: ${recommendation.actionPlan}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF1E293B)
                        ),
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { onDrillAction(recommendation.targetDrillPackageId) },
                    colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.height(32.dp)
                ) {
                    Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Drill Topik Ini", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun ScoreSubtestCard(
    title: String,
    score: Int,
    passingGrade: Int,
    maxScore: Int,
    isPassed: Boolean,
    description: String
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isPassed) Color(0xFF86EFAC) else Color(0xFFFCA5A5)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )

                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = if (isPassed) EmeraldSuccess else CrimsonRed
                ) {
                    Text(
                        text = if (isPassed) "LULUS PG" else "TIDAK LULUS",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "$score",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        color = if (isPassed) EmeraldSuccess else CrimsonRed
                    )
                )
                Text(
                    text = " / Ambang Batas: $passingGrade (Maks $maxScore)",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    modifier = Modifier.padding(start = 6.dp, bottom = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            val progress = (score.toFloat() / maxScore.toFloat()).coerceIn(0f, 1f)
            LinearProgressIndicator(
                progress = { progress },
                color = if (isPassed) EmeraldSuccess else CrimsonRed,
                trackColor = Color(0xFFE2E8F0),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF64748B),
                fontSize = 11.sp
            )
        }
    }
}

@Composable
private fun TimeMetricItem(title: String, value: String) {
    Column {
        Text(text = title, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            ),
            color = NavyPrimary
        )
    }
}

@Composable
private fun MasteryProgressRow(
    mastery: SubCategoryMastery,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = mastery.subCategory,
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
                )
                Text(
                    text = "${mastery.percentage}%",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = when {
                            mastery.percentage >= 80 -> EmeraldSuccess
                            mastery.percentage >= 60 -> GoldSecondary
                            else -> CrimsonRed
                        }
                    )
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            LinearProgressIndicator(
                progress = { mastery.percentage / 100f },
                color = when {
                    mastery.percentage >= 80 -> EmeraldSuccess
                    mastery.percentage >= 60 -> GoldSecondary
                    else -> CrimsonRed
                },
                trackColor = Color(0xFFF1F5F9),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
            )
        }
    }
}
