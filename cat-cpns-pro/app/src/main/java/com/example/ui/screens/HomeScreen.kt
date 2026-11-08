package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SwitchAccount
import androidx.compose.material.icons.filled.Timer
import com.example.data.model.UserRole
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.db.ExamResultEntity
import com.example.ui.theme.CatActiveBlue
import com.example.ui.theme.CatActiveBlueBg
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

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onStartSimulation: (String) -> Unit,
    onSelectPackage: () -> Unit,
    onViewResult: (Long) -> Unit,
    onOpenLeaderboard: () -> Unit,
    onOpenStudyMaterial: () -> Unit,
    onOpenBookmarks: () -> Unit,
    onOpenLogin: () -> Unit = {},
    onOpenAdminDashboard: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val authState by viewModel.authState.collectAsState()
    val allResults by viewModel.allResults.collectAsState()
    val totalCount by viewModel.totalExamsCount.collectAsState()
    val avgScore by viewModel.averageScore.collectAsState()
    val maxScore by viewModel.highestScore.collectAsState()

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen_container")
    ) {
        val isTabletOrPc = maxWidth >= 720.dp

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            if (isTabletOrPc) {
                // ==================== TABLET & PC MULTI-COLUMN LAYOUT ====================
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .widthIn(max = 1280.dp)
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // LEFT COLUMN: User session, Hero, CTA, Metrics, Ambang Batas
                    Column(
                        modifier = Modifier
                            .weight(1.1f)
                            .verticalScroll(rememberScrollState())
                    ) {
                        UserSessionHeaderBar(
                            authState = authState,
                            onOpenLogin = onOpenLogin,
                            onOpenAdminDashboard = onOpenAdminDashboard,
                            onQuickSwitchRole = {
                                val nextRole = if (authState.currentUser?.role == UserRole.ADMIN) UserRole.USER else UserRole.ADMIN
                                viewModel.switchRole(nextRole)
                            },
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        HeroBannerCard()

                        Spacer(modifier = Modifier.height(14.dp))

                        RandomizedExamCtaCard(
                            onStartRandom = { onStartSimulation("pkg_random_skd") },
                            onSelectPackages = onSelectPackage
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        KpiStatsCard(
                            totalCount = totalCount,
                            avgScore = avgScore,
                            maxScore = maxScore
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        PassingGradeReferenceCard()
                    }

                    // RIGHT COLUMN: Drills, Bank Soal Overview, Menu Persiapan, History
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                    ) {
                        CategoryDrillsSection(onStartDrill = onStartSimulation)

                        Spacer(modifier = Modifier.height(14.dp))

                        QuestionBankOverviewCard()

                        Spacer(modifier = Modifier.height(14.dp))

                        QuickMenuSection(
                            onOpenLeaderboard = onOpenLeaderboard,
                            onOpenStudyMaterial = onOpenStudyMaterial,
                            onOpenBookmarks = onOpenBookmarks
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        RecentHistorySection(
                            allResults = allResults,
                            onViewResult = onViewResult
                        )

                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            } else {
                // ==================== MOBILE COMPACT SINGLE COLUMN ====================
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("home_screen_lazy_column")
                ) {
                    item {
                        UserSessionHeaderBar(
                            authState = authState,
                            onOpenLogin = onOpenLogin,
                            onOpenAdminDashboard = onOpenAdminDashboard,
                            onQuickSwitchRole = {
                                val nextRole = if (authState.currentUser?.role == UserRole.ADMIN) UserRole.USER else UserRole.ADMIN
                                viewModel.switchRole(nextRole)
                            },
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }

                    item {
                        HeroBannerCard()
                    }

                    item {
                        RandomizedExamCtaCard(
                            onStartRandom = { onStartSimulation("pkg_random_skd") },
                            onSelectPackages = onSelectPackage,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                        )
                    }

                    item {
                        KpiStatsCard(
                            totalCount = totalCount,
                            avgScore = avgScore,
                            maxScore = maxScore,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }

                    item {
                        CategoryDrillsSection(
                            onStartDrill = onStartSimulation,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }

                    item {
                        QuestionBankOverviewCard(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                        )
                    }

                    item {
                        PassingGradeReferenceCard(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                        )
                    }

                    item {
                        QuickMenuSection(
                            onOpenLeaderboard = onOpenLeaderboard,
                            onOpenStudyMaterial = onOpenStudyMaterial,
                            onOpenBookmarks = onOpenBookmarks,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }

                    item {
                        RecentHistorySection(
                            allResults = allResults,
                            onViewResult = onViewResult,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
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
private fun HeroBannerCard(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_cpns_hero),
            contentDescription = "Simulasi CAT CPNS Resmi",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color(0xDD0F172A))
                    )
                )
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = GoldSecondary
            ) {
                Text(
                    text = "STANDAR RESMI BKN 2024 / 2025",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    ),
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Simulasi Sistem CAT CPNS",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
            Text(
                text = "Ujian Acak Real-time • Passing Grade SKD • 110 Soal HOTS",
                style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFFCBD5E1))
            )
        }
    }
}

@Composable
private fun RandomizedExamCtaCard(
    onStartRandom: () -> Unit,
    onSelectPackages: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = NavyPrimary),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("cta_start_skd_card")
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = GoldSecondary
                    ) {
                        Text(
                            text = "FITUR UJIAN ACAK BARU",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Black),
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            fontSize = 9.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Simulasi CAT SKD Acak",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Set 110 soal berbeda setiap sesi ujian (TWK 30, TIU 35, TKP 45) • 100 Menit",
                        style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF93C5FD))
                    )
                }

                Surface(
                    shape = CircleShape,
                    color = GoldSecondary,
                    modifier = Modifier.size(42.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Mulai",
                            tint = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = onStartRandom,
                    colors = ButtonDefaults.buttonColors(containerColor = GoldSecondary),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("btn_start_random_simulation")
                ) {
                    Text("Mulai Ujian Acak Baru", fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = onSelectPackages,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF60A5FA)),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("btn_select_packages")
                ) {
                    Text("Pilih Paket")
                }
            }
        }
    }
}

@Composable
private fun KpiStatsCard(
    totalCount: Int,
    avgScore: Double?,
    maxScore: Int?,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Statistik Evaluasi Peserta",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            StatMetricCard(
                title = "Tryout Selesai",
                value = "$totalCount",
                subtext = "Sesi latihan",
                containerColor = NavyContainer,
                contentColor = OnNavyContainer,
                modifier = Modifier.weight(1f)
            )
            StatMetricCard(
                title = "Rata-rata Skor",
                value = if (avgScore != null) String.format("%.0f", avgScore) else "-",
                subtext = "Dari maks 550",
                containerColor = GoldContainer,
                contentColor = OnGoldContainer,
                modifier = Modifier.weight(1f)
            )
            StatMetricCard(
                title = "Skor Tertinggi",
                value = if (maxScore != null) "$maxScore" else "-",
                subtext = if (maxScore != null && maxScore >= 311) "Lolos PG" else "Belum PG",
                containerColor = EmeraldContainer,
                contentColor = OnEmeraldContainer,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun CategoryDrillsSection(
    onStartDrill: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Latihan Fokus Per Kategori",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = "Standar PG 2024",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            CategoryDrillCard(
                code = "TWK",
                title = "Wawasan Kebangsaan",
                pgText = "Min. 65 Poin",
                soalText = "30 Soal • 30 Mnt",
                icon = Icons.Default.Security,
                color = NavyPrimary,
                onClick = { onStartDrill("pkg_drill_twk") },
                modifier = Modifier.weight(1f)
            )
            CategoryDrillCard(
                code = "TIU",
                title = "Inteligensia Umum",
                pgText = "Min. 80 Poin",
                soalText = "35 Soal • 35 Mnt",
                icon = Icons.Default.Psychology,
                color = GoldSecondary,
                onClick = { onStartDrill("pkg_drill_tiu") },
                modifier = Modifier.weight(1f)
            )
            CategoryDrillCard(
                code = "TKP",
                title = "Karakteristik Pribadi",
                pgText = "Min. 166 Poin",
                soalText = "45 Soal • 45 Mnt",
                icon = Icons.Default.Star,
                color = EmeraldSuccess,
                onClick = { onStartDrill("pkg_drill_tkp") },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun QuestionBankOverviewCard(modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.MenuBook,
                        contentDescription = null,
                        tint = GoldSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Bank Soal Komprehensif CPNS",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = NavyPrimary.copy(alpha = 0.1f)
                ) {
                    Text(
                        text = "Standar HOTS BKN",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = NavyPrimary,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        fontSize = 10.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "• TWK: Pancasila (Sila 1-5), UUD 1945 (Amandemen I-IV), Integritas KPK (9 Nilai), Bela Negara, NKRI, Bhinneka Tunggal Ika, dan Bahasa Indonesia PUEBI.",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF334155),
                fontSize = 11.sp,
                lineHeight = 16.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "• TIU: Kemampuan Verbal (Analogi, Silogisme, Analitis), Kemampuan Numerik (Deret Angka, Berhitung Cepat, Perbandingan, Soal Cerita Kecepatan & Pekerja), dan Figural.",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF334155),
                fontSize = 11.sp,
                lineHeight = 16.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "• TKP: Pelayanan Publik, Jejaring Kerja, Sosial Budaya, TIK Birokrasi, Profesionalisme, dan Anti-Radikalisme (Bobot bertingkat 1 s.d. 5 untuk seluruh opsi).",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF334155),
                fontSize = 11.sp,
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
private fun PassingGradeReferenceCard(modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Assessment,
                    contentDescription = "Passing Grade",
                    tint = NavyPrimary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Ambang Batas Kelulusan SKD (KepmenPAN-RB)",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Peserta dinyatakan LULUS SKD hanya jika ketiga subtes secara serentak melampaui passing grade: TWK (65), TIU (80), dan TKP (166). Total nilai maksimal adalah 550.",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF64748B)
            )
        }
    }
}

@Composable
private fun QuickMenuSection(
    onOpenLeaderboard: () -> Unit,
    onOpenStudyMaterial: () -> Unit,
    onOpenBookmarks: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Menu Persiapan ASN",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            FeatureQuickCard(
                title = "Peringkat Nasional",
                subtitle = "Simulasi Ranking",
                icon = Icons.Default.Leaderboard,
                iconTint = GoldSecondary,
                onClick = onOpenLeaderboard,
                modifier = Modifier.weight(1f)
            )
            FeatureQuickCard(
                title = "Materi & Rumus",
                subtitle = "Cheat Sheet CAT",
                icon = Icons.AutoMirrored.Filled.MenuBook,
                iconTint = NavyPrimary,
                onClick = onOpenStudyMaterial,
                modifier = Modifier.weight(1f)
            )
            FeatureQuickCard(
                title = "Soal Tersimpan",
                subtitle = "Bookmark Soal",
                icon = Icons.Default.Bookmark,
                iconTint = EmeraldSuccess,
                onClick = onOpenBookmarks,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun RecentHistorySection(
    allResults: List<ExamResultEntity>,
    onViewResult: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Riwayat Tryout Terakhir",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            if (allResults.isNotEmpty()) {
                Text(
                    text = "${allResults.size} Riwayat",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (allResults.isEmpty()) {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = null,
                        tint = Color.LightGray,
                        modifier = Modifier.size(44.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Belum Ada Riwayat Ujian",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "Selesaikan simulasi CAT pertama Anda untuk melihat analisis skor mendalam dan rekomendasi kelulusan.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        } else {
            allResults.take(4).forEach { result ->
                HistoryItemCard(
                    result = result,
                    onClick = { onViewResult(result.id) },
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun StatMetricCard(
    title: String,
    value: String,
    subtext: String,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                color = contentColor.copy(alpha = 0.8f)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
                color = contentColor
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtext,
                style = MaterialTheme.typography.labelSmall,
                color = contentColor.copy(alpha = 0.7f),
                fontSize = 10.sp
            )
        }
    }
}

@Composable
private fun CategoryDrillCard(
    code: String,
    title: String,
    pgText: String,
    soalText: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
        modifier = modifier.clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = color.copy(alpha = 0.12f),
                modifier = Modifier.size(32.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = code,
                        tint = color,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = code,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Black),
                color = color
            )
            Text(
                text = pgText,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF334155),
                fontSize = 11.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = soalText,
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray,
                fontSize = 9.sp
            )
        }
    }
}

@Composable
private fun FeatureQuickCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
        modifier = modifier.clickable { onClick() }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = iconTint,
                modifier = Modifier.size(26.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                fontSize = 11.sp
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray,
                fontSize = 9.sp
            )
        }
    }
}

@Composable
fun HistoryItemCard(
    result: ExamResultEntity,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dateStr = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
        .format(Date(result.timestamp))

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (result.isPassed) Color(0xFF86EFAC) else Color(0xFFFECDD3)
        ),
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("history_item_${result.id}")
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(14.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = if (result.isPassed) EmeraldSuccess else CrimsonRed
                    ) {
                        Text(
                            text = if (result.isPassed) "LULUS PG" else "TIDAK LULUS PG",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            fontSize = 10.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = dateStr,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = result.packageName,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "TWK: ${result.twkScore} • TIU: ${result.tiuScore} • TKP: ${result.tkpScore}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF475569)
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${result.totalScore}",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Black,
                        color = if (result.isPassed) EmeraldSuccess else NavyPrimary
                    )
                )
                Text(
                    text = "Skor Akhir",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray,
                    fontSize = 10.sp
                )
            }
        }
    }
}

@Composable
private fun UserSessionHeaderBar(
    authState: com.example.data.model.AuthState,
    onOpenLogin: () -> Unit,
    onOpenAdminDashboard: () -> Unit,
    onQuickSwitchRole: () -> Unit,
    modifier: Modifier = Modifier
) {
    val user = authState.currentUser
    val isAdmin = user?.role == UserRole.ADMIN

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isAdmin) Color(0xFF0F172A) else MaterialTheme.colorScheme.surface
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isAdmin) Color(0xFF334155) else Color(0xFFE2E8F0)
        ),
        modifier = modifier
            .fillMaxWidth()
            .testTag("user_session_header_bar")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Surface(
                    shape = CircleShape,
                    color = if (isAdmin) GoldSecondary else NavyPrimary,
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = if (isAdmin) Icons.Default.AdminPanelSettings else Icons.Default.Person,
                            contentDescription = null,
                            tint = if (isAdmin) Color(0xFF1E293B) else Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = user?.name ?: "Tamu Simulasi CAT",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (isAdmin) Color.White else MaterialTheme.colorScheme.onSurface
                            ),
                            maxLines = 1,
                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                        )
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = if (isAdmin) GoldSecondary else NavyPrimary
                        ) {
                            Text(
                                text = if (isAdmin) "ADMINISTRATOR" else "PESERTA CPNS",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 9.sp
                                ),
                                color = if (isAdmin) Color(0xFF1E293B) else Color.White,
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        Text(
                            text = if (isAdmin) user?.institution ?: "BKN RI" else "No: ${user?.participantNumber ?: "24-3012-008129"}",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = if (isAdmin) Color(0xFF94A3B8) else Color.Gray,
                            maxLines = 1,
                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                        )
                    }
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (isAdmin) {
                    Button(
                        onClick = onOpenAdminDashboard,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GoldSecondary),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("open_admin_dashboard_button")
                    ) {
                        Text(
                            text = "Panel CMS",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1E293B)
                            )
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                }

                OutlinedButton(
                    onClick = onOpenLogin,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = if (isAdmin) Color.White else NavyPrimary
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isAdmin) Color(0xFF64748B) else Color(0xFFCBD5E1)
                    ),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("switch_account_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.SwitchAccount,
                        contentDescription = "Ganti Akun",
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Ganti Akun",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium)
                    )
                }
            }
        }
    }
}

