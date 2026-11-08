package com.example.ui.screens

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Grading
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SwitchAccount
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.db.ExamResultEntity
import com.example.data.model.ExamCategory
import com.example.data.model.Question
import com.example.data.model.QuestionOption
import com.example.data.model.UserRole
import com.example.ui.theme.CatActiveBlue
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

enum class AdminTab {
    BANK_SOAL,
    REKAP_PESERTA,
    PAKET_UJIAN
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    viewModel: MainViewModel,
    onSwitchToUserMode: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val authState by viewModel.authState.collectAsState()
    val adminQuestions by viewModel.adminQuestions.collectAsState()
    val allResults by viewModel.allResults.collectAsState()
    val totalExams by viewModel.totalExamsCount.collectAsState()
    val avgScore by viewModel.averageScore.collectAsState()

    var selectedTab by remember { mutableStateOf(AdminTab.BANK_SOAL) }
    var selectedCategoryFilter by remember { mutableStateOf<ExamCategory?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    // Dialog state for Add/Edit question
    var showQuestionDialog by remember { mutableStateOf(false) }
    var editingQuestion by remember { mutableStateOf<Question?>(null) }
    var questionToDelete by remember { mutableStateOf<Question?>(null) }

    val filteredQuestions = adminQuestions.filter { q ->
        val matchCat = selectedCategoryFilter == null || q.category == selectedCategoryFilter
        val matchQuery = searchQuery.isEmpty() ||
                q.questionText.contains(searchQuery, ignoreCase = true) ||
                q.subCategory.contains(searchQuery, ignoreCase = true)
        matchCat && matchQuery
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFF0F172A),
                            modifier = Modifier.size(32.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.AdminPanelSettings,
                                    contentDescription = null,
                                    tint = GoldSecondary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Panel Pengelola CAT BKN",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "${authState.currentUser?.name ?: "Administrator"} • ${authState.currentUser?.institution ?: "BKN RI"}",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.Gray
                            )
                        }
                    }
                },
                actions = {
                    TextButton(
                        onClick = onSwitchToUserMode,
                        modifier = Modifier.testTag("switch_to_user_button")
                    ) {
                        Icon(imageVector = Icons.Default.SwitchAccount, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Mode Peserta", fontWeight = FontWeight.Bold)
                    }

                    IconButton(
                        onClick = {
                            viewModel.logout()
                            onLogout()
                        },
                        modifier = Modifier.testTag("admin_logout_button")
                    ) {
                        Icon(imageVector = Icons.Default.Logout, contentDescription = "Keluar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        floatingActionButton = {
            if (selectedTab == AdminTab.BANK_SOAL) {
                FloatingActionButton(
                    onClick = {
                        editingQuestion = null
                        showQuestionDialog = true
                    },
                    containerColor = NavyPrimary,
                    contentColor = Color.White,
                    modifier = Modifier.testTag("add_question_fab")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Tambah Soal")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Tambah Soal", fontWeight = FontWeight.Bold)
                    }
                }
            }
        },
        modifier = modifier.testTag("admin_dashboard_screen")
    ) { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF8FAFC))
        ) {
            val isTabletOrPc = maxWidth >= 720.dp

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = 1280.dp)
                    .align(Alignment.TopCenter)
            ) {
                // Overview Metric Cards
                AdminMetricsHeader(
                    totalQuestions = adminQuestions.size,
                    twkCount = adminQuestions.count { it.category == ExamCategory.TWK },
                    tiuCount = adminQuestions.count { it.category == ExamCategory.TIU },
                    tkpCount = adminQuestions.count { it.category == ExamCategory.TKP },
                    totalExams = totalExams,
                    avgScore = avgScore,
                    isWide = isTabletOrPc,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )

                // Tabs
                TabRow(
                    selectedTabIndex = selectedTab.ordinal,
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = NavyPrimary,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Tab(
                        selected = selectedTab == AdminTab.BANK_SOAL,
                        onClick = { selectedTab = AdminTab.BANK_SOAL },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.MenuBook, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Bank Soal & CMS (${adminQuestions.size})", fontWeight = FontWeight.Bold)
                            }
                        },
                        modifier = Modifier.testTag("tab_bank_soal")
                    )
                    Tab(
                        selected = selectedTab == AdminTab.REKAP_PESERTA,
                        onClick = { selectedTab = AdminTab.REKAP_PESERTA },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.Assessment, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Rekap Nilai Peserta (${allResults.size})", fontWeight = FontWeight.Bold)
                            }
                        },
                        modifier = Modifier.testTag("tab_rekap_peserta")
                    )
                    Tab(
                        selected = selectedTab == AdminTab.PAKET_UJIAN,
                        onClick = { selectedTab = AdminTab.PAKET_UJIAN },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.Folder, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Paket Ujian (${viewModel.packages.size})", fontWeight = FontWeight.Bold)
                            }
                        },
                        modifier = Modifier.testTag("tab_paket_ujian")
                    )
                }

                // Tab Content
                when (selectedTab) {
                    AdminTab.BANK_SOAL -> {
                        AdminQuestionBankContent(
                            questions = filteredQuestions,
                            selectedCategory = selectedCategoryFilter,
                            onSelectCategory = { selectedCategoryFilter = it },
                            searchQuery = searchQuery,
                            onSearchQueryChange = { searchQuery = it },
                            onEditQuestion = { q ->
                                editingQuestion = q
                                showQuestionDialog = true
                            },
                            onDeleteQuestion = { q ->
                                questionToDelete = q
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    AdminTab.REKAP_PESERTA -> {
                        AdminExamResultsContent(
                            results = allResults,
                            onDeleteResult = { id -> viewModel.deleteResult(id) },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    AdminTab.PAKET_UJIAN -> {
                        AdminPackagesContent(
                            packages = viewModel.packages,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }

    // Dialog for Add / Edit Question
    if (showQuestionDialog) {
        QuestionEditorDialog(
            initialQuestion = editingQuestion,
            onDismiss = { showQuestionDialog = false },
            onSave = { questionToSave ->
                viewModel.saveQuestion(questionToSave)
                showQuestionDialog = false
            }
        )
    }

    // Confirmation Dialog for Delete Question
    if (questionToDelete != null) {
        AlertDialog(
            onDismissRequest = { questionToDelete = null },
            title = { Text("Hapus Soal?") },
            text = {
                Text("Soal [${questionToDelete?.category?.code}] \"${questionToDelete?.questionText?.take(80)}...\" akan dihapus dari bank soal sistem.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        questionToDelete?.let { viewModel.deleteQuestion(it.id) }
                        questionToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CrimsonRed)
                ) {
                    Text("Hapus Permanen")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { questionToDelete = null }) {
                    Text("Batal")
                }
            }
        )
    }
}

@Composable
private fun AdminMetricsHeader(
    totalQuestions: Int,
    twkCount: Int,
    tiuCount: Int,
    tkpCount: Int,
    totalExams: Int,
    avgScore: Double?,
    isWide: Boolean,
    modifier: Modifier = Modifier
) {
    if (isWide) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MetricCard("Total Bank Soal", "$totalQuestions Butir", "TWK: $twkCount | TIU: $tiuCount | TKP: $tkpCount", NavyPrimary, Modifier.weight(1f))
            MetricCard("Peserta Ujian Selesai", "$totalExams Sesi", "Terekam di Room Database", CatActiveBlue, Modifier.weight(1f))
            MetricCard("Rata-rata Skor", avgScore?.let { String.format(java.util.Locale.US, "%.1f", it) } ?: "Belum ada", "Passing Grade Total 311", EmeraldSuccess, Modifier.weight(1f))
            MetricCard("Sistem Ujian", "PermenPAN-RB", "Simulasi CAT BKN HOTS", GoldSecondary, Modifier.weight(1f))
        }
    } else {
        Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                MetricCard("Bank Soal", "$totalQuestions Butir", "TWK:$twkCount TIU:$tiuCount TKP:$tkpCount", NavyPrimary, Modifier.weight(1f))
                MetricCard("Sesi Ujian", "$totalExams Selesai", "Terekam lokal", CatActiveBlue, Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun MetricCard(
    title: String,
    value: String,
    subtitle: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = title, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = value, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = color)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = subtitle, style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp), color = Color.Gray, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}

@Composable
private fun AdminQuestionBankContent(
    questions: List<Question>,
    selectedCategory: ExamCategory?,
    onSelectCategory: (ExamCategory?) -> Unit,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onEditQuestion: (Question) -> Unit,
    onDeleteQuestion: (Question) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        item {
            // Search & Filter Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    placeholder = { Text("Cari soal atau materi...", fontSize = 12.sp) },
                    leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null, modifier = Modifier.size(18.dp)) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { onSearchQueryChange("") }) {
                                Icon(imageVector = Icons.Default.Close, contentDescription = null, modifier = Modifier.size(16.dp))
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("admin_search_input")
                )
            }

            // Filter Chips
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
            ) {
                FilterChip(
                    selected = selectedCategory == null,
                    onClick = { onSelectCategory(null) },
                    label = { Text("Semua (${questions.size})") },
                    modifier = Modifier.testTag("filter_all_questions")
                )
                FilterChip(
                    selected = selectedCategory == ExamCategory.TWK,
                    onClick = { onSelectCategory(ExamCategory.TWK) },
                    label = { Text("TWK") },
                    modifier = Modifier.testTag("filter_twk_questions")
                )
                FilterChip(
                    selected = selectedCategory == ExamCategory.TIU,
                    onClick = { onSelectCategory(ExamCategory.TIU) },
                    label = { Text("TIU") },
                    modifier = Modifier.testTag("filter_tiu_questions")
                )
                FilterChip(
                    selected = selectedCategory == ExamCategory.TKP,
                    onClick = { onSelectCategory(ExamCategory.TKP) },
                    label = { Text("TKP") },
                    modifier = Modifier.testTag("filter_tkp_questions")
                )
            }

            Spacer(modifier = Modifier.height(6.dp))
        }

        if (questions.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Tidak ada soal yang sesuai kriteria pencarian.", color = Color.Gray)
                }
            }
        } else {
            items(questions, key = { it.id }) { q ->
                AdminQuestionItemCard(
                    question = q,
                    onEdit = { onEditQuestion(q) },
                    onDelete = { onDeleteQuestion(q) },
                    modifier = Modifier.padding(vertical = 5.dp)
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
private fun AdminQuestionItemCard(
    question: Question,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
        modifier = modifier
            .fillMaxWidth()
            .testTag("admin_question_card_${question.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header: Category, Subcategory, Edit/Delete
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
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            fontSize = 10.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = question.subCategory,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                        color = Color.Gray
                    )
                }

                Row {
                    IconButton(
                        onClick = onEdit,
                        modifier = Modifier.size(32.dp).testTag("edit_question_${question.id}")
                    ) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Soal", tint = NavyPrimary, modifier = Modifier.size(18.dp))
                    }
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(32.dp).testTag("delete_question_${question.id}")
                    ) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Hapus Soal", tint = CrimsonRed, modifier = Modifier.size(18.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Question Text
            Text(
                text = question.questionText,
                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Options Preview (Clickable to expand)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF8FAFC), RoundedCornerShape(8.dp))
                    .padding(8.dp)
            ) {
                question.options.forEach { opt ->
                    val isMaxScore = if (question.category == ExamCategory.TKP) opt.score == 5 else opt.score == 5
                    Row(
                        verticalAlignment = Alignment.Top,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = if (isMaxScore) Color(0xFFDCFCE7) else Color(0xFFE2E8F0),
                            modifier = Modifier.size(20.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = opt.key,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isMaxScore) Color(0xFF166534) else Color(0xFF475569)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = opt.text,
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = if (isMaxScore) Color(0xFF166534) else Color(0xFF475569),
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = if (isMaxScore) EmeraldSuccess else Color(0xFFCBD5E1)
                        ) {
                            Text(
                                text = "${opt.score} pt",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp, fontWeight = FontWeight.Bold),
                                color = if (isMaxScore) Color.White else Color(0xFF1E293B),
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Explanation Section
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(vertical = 2.dp)
            ) {
                Text(
                    text = if (expanded) "Sembunyikan Pembahasan ▲" else "Lihat Pembahasan Resmi ▼",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = CatActiveBlue
                )
            }

            AnimatedVisibility(visible = expanded) {
                Card(
                    shape = RoundedCornerShape(6.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBBF7D0)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = "PEMBAHASAN KUNCI JAWABAN:",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFF166534)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = question.explanation,
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, lineHeight = 18.sp),
                            color = Color(0xFF14532D)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AdminExamResultsContent(
    results: List<ExamResultEntity>,
    onDeleteResult: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    if (results.isEmpty()) {
        Box(modifier = modifier.padding(40.dp), contentAlignment = Alignment.Center) {
            Text("Belum ada riwayat ujian yang terekam dari peserta.", color = Color.Gray)
        }
    } else {
        LazyColumn(modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            item {
                Text(
                    text = "Daftar Hasil Ujian Peserta (${results.size} sesi)",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Data disimpan di database Room lokal dan siap diekspor ke PostgreSQL",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            items(results) { res ->
                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = if (res.isPassed) EmeraldSuccess else CrimsonRed
                                ) {
                                    Text(
                                        text = if (res.isPassed) "LOLOS PG" else "TIDAK LOLOS",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        fontSize = 9.sp
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = res.packageName,
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Skor: TWK ${res.twkScore} | TIU ${res.tiuScore} | TKP ${res.tkpScore}  →  TOTAL: ${res.totalScore}",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                                color = NavyPrimary
                            )
                        }

                        IconButton(onClick = { onDeleteResult(res.id) }) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = "Hapus Riwayat", tint = Color.Gray, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}

@Composable
private fun AdminPackagesContent(
    packages: List<com.example.data.model.ExamPackage>,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        item {
            Text(
                text = "Konfigurasi Paket Simulasi Ujian (${packages.size} Paket)",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = "Paket acak otomatis mengambil kombinasi proporsional dari Bank Soal di atas.",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        items(packages) { pkg ->
            Card(
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = pkg.title,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = if (pkg.isRandomized) GoldSecondary else NavyPrimary
                        ) {
                            Text(
                                text = pkg.badge,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                fontSize = 9.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = pkg.description, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Durasi: ${pkg.durationMinutes} menit • Total: ${pkg.totalQuestions} Soal • Tingkat: ${pkg.difficulty}",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                        color = NavyPrimary
                    )
                }
            }
        }
    }
}

@Composable
private fun QuestionEditorDialog(
    initialQuestion: Question?,
    onDismiss: () -> Unit,
    onSave: (Question) -> Unit
) {
    var category by remember { mutableStateOf(initialQuestion?.category ?: ExamCategory.TWK) }
    var subCategory by remember { mutableStateOf(initialQuestion?.subCategory ?: "Pilar Negara") }
    var questionText by remember { mutableStateOf(initialQuestion?.questionText ?: "") }
    var optA by remember { mutableStateOf(initialQuestion?.options?.getOrNull(0)?.text ?: "") }
    var optB by remember { mutableStateOf(initialQuestion?.options?.getOrNull(1)?.text ?: "") }
    var optC by remember { mutableStateOf(initialQuestion?.options?.getOrNull(2)?.text ?: "") }
    var optD by remember { mutableStateOf(initialQuestion?.options?.getOrNull(3)?.text ?: "") }
    var optE by remember { mutableStateOf(initialQuestion?.options?.getOrNull(4)?.text ?: "") }
    var correctKey by remember {
        mutableStateOf(
            initialQuestion?.options?.find { it.score == 5 }?.key ?: "A"
        )
    }
    var explanationText by remember { mutableStateOf(initialQuestion?.explanation ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (initialQuestion == null) "Tambah Soal Baru" else "Edit Soal [${initialQuestion.id}]",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                // Category Selector
                Text("Kategori Tes:", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ExamCategory.entries.forEach { cat ->
                        FilterChip(
                            selected = category == cat,
                            onClick = { category = cat },
                            label = { Text(cat.code) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Subcategory
                Text("Sub-Kategori / Topik:", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                OutlinedTextField(
                    value = subCategory,
                    onValueChange = { subCategory = it },
                    placeholder = { Text("Contoh: Bela Negara, Silogisme, Pelayanan Publik") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("editor_subcategory_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Question Text
                Text("Teks Soal / Pertanyaan:", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                OutlinedTextField(
                    value = questionText,
                    onValueChange = { questionText = it },
                    placeholder = { Text("Ketik butir soal HOTS...") },
                    minLines = 3,
                    modifier = Modifier.fillMaxWidth().testTag("editor_question_text_input")
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Options A to E
                Text("Pilihan Jawaban (A-E):", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                OutlinedTextField(value = optA, onValueChange = { optA = it }, label = { Text("Opsi A") }, singleLine = true, modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp))
                OutlinedTextField(value = optB, onValueChange = { optB = it }, label = { Text("Opsi B") }, singleLine = true, modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp))
                OutlinedTextField(value = optC, onValueChange = { optC = it }, label = { Text("Opsi C") }, singleLine = true, modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp))
                OutlinedTextField(value = optD, onValueChange = { optD = it }, label = { Text("Opsi D") }, singleLine = true, modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp))
                OutlinedTextField(value = optE, onValueChange = { optE = it }, label = { Text("Opsi E") }, singleLine = true, modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp))

                Spacer(modifier = Modifier.height(10.dp))

                // Correct Answer Selector for TWK/TIU
                Text(
                    text = if (category == ExamCategory.TKP) "Kunci Opsi Poin 5 Tertinggi:" else "Kunci Jawaban Benar (Skor 5):",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    listOf("A", "B", "C", "D", "E").forEach { key ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(selected = correctKey == key, onClick = { correctKey = key })
                            Text(key, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Explanation
                Text("Pembahasan & Rasionalisasi:", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                OutlinedTextField(
                    value = explanationText,
                    onValueChange = { explanationText = it },
                    placeholder = { Text("Rasionalisasi kunci jawaban...") },
                    minLines = 2,
                    modifier = Modifier.fillMaxWidth().testTag("editor_explanation_input")
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val finalId = initialQuestion?.id ?: "custom_${category.code.lowercase()}_${System.currentTimeMillis()}"
                    val options = if (category == ExamCategory.TKP) {
                        // Assign 5 to chosen key, and 4, 3, 2, 1 to others
                        val otherKeys = listOf("A", "B", "C", "D", "E").filter { it != correctKey }
                        val texts = mapOf("A" to optA, "B" to optB, "C" to optC, "D" to optD, "E" to optE)
                        val resultOpts = mutableListOf<QuestionOption>()
                        resultOpts.add(QuestionOption(correctKey, texts[correctKey] ?: "", 5))
                        var currentScore = 4
                        for (k in otherKeys) {
                            resultOpts.add(QuestionOption(k, texts[k] ?: "", currentScore--))
                        }
                        resultOpts.sortedBy { it.key }
                    } else {
                        val texts = mapOf("A" to optA, "B" to optB, "C" to optC, "D" to optD, "E" to optE)
                        listOf("A", "B", "C", "D", "E").map { k ->
                            QuestionOption(k, texts[k] ?: "", if (k == correctKey) 5 else 0)
                        }
                    }

                    val newQuestion = Question(
                        id = finalId,
                        packageId = initialQuestion?.packageId ?: "pkg_custom",
                        category = category,
                        subCategory = subCategory.ifEmpty { "Umum" },
                        number = initialQuestion?.number ?: 1,
                        questionText = questionText.ifEmpty { "Pertanyaan" },
                        options = options,
                        explanation = explanationText.ifEmpty { "Pembahasan belum tersedia." }
                    )
                    onSave(newQuestion)
                },
                modifier = Modifier.testTag("save_question_button")
            ) {
                Text("Simpan Soal")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}
