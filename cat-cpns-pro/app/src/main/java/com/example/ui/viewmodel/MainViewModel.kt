package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.AppDatabase
import com.example.data.db.BookmarkEntity
import com.example.data.db.ExamResultEntity
import com.example.data.model.AccuracyStatistics
import com.example.data.model.AuthState
import com.example.data.model.ExamCategory
import com.example.data.model.ExamPackage
import com.example.data.model.ImprovementRecommendation
import com.example.data.model.LeaderboardItem
import com.example.data.model.Question
import com.example.data.model.SubCategoryMastery
import com.example.data.model.UserAnswer
import com.example.data.model.UserProfile
import com.example.data.model.UserRole
import com.example.data.network.ApiClient
import com.example.data.network.LoginRequest
import com.example.data.network.SessionManager
import com.example.data.repository.ExamRepository
import com.example.data.seed.StudyMaterial
import com.example.data.seed.StudyMaterialData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONArray

data class DetailedResultUiState(
    val resultEntity: ExamResultEntity? = null,
    val questions: List<Question> = emptyList(),
    val userAnswers: Map<String, UserAnswer> = emptyMap(),
    val subCategoryMastery: List<SubCategoryMastery> = emptyList(),
    val accuracyStats: AccuracyStatistics? = null,
    val recommendations: List<ImprovementRecommendation> = emptyList(),
    val isLoading: Boolean = true
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ExamRepository(AppDatabase.getDatabase(application))

    private val _packages = MutableStateFlow<List<ExamPackage>>(repository.getPackages())
    val packagesFlow: StateFlow<List<ExamPackage>> = _packages.asStateFlow()
    val packages: List<ExamPackage> get() = _packages.value

    val allResults: StateFlow<List<ExamResultEntity>> = repository.getAllExamResults()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalExamsCount: StateFlow<Int> = repository.getTotalExamsCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val averageScore: StateFlow<Double?> = repository.getAverageTotalScore()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val highestScore: StateFlow<Int?> = repository.getHighestTotalScore()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val bookmarks: StateFlow<List<BookmarkEntity>> = repository.getAllBookmarks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _studyMaterials = MutableStateFlow<List<StudyMaterial>>(StudyMaterialData.getMaterials())
    val studyMaterials: StateFlow<List<StudyMaterial>> = _studyMaterials.asStateFlow()

    private val _detailedResultState = MutableStateFlow(DetailedResultUiState())
    val detailedResultState: StateFlow<DetailedResultUiState> = _detailedResultState.asStateFlow()

    init {
        SessionManager.init(application)
        val savedUser = SessionManager.currentUserState.value
        if (savedUser != null) {
            _authState.value = AuthState(
                currentUser = savedUser,
                isLoggedIn = true,
                error = null
            )
        }
        // Load remote study materials and packages in background if session exists
        viewModelScope.launch {
            try {
                val remotePackages = repository.fetchRemotePackages()
                if (remotePackages.isNotEmpty()) _packages.value = remotePackages
            } catch (_: Exception) {}

            try {
                val remote = repository.getStudyMaterials()
                if (remote.isNotEmpty()) _studyMaterials.value = remote
            } catch (_: Exception) {}
        }
    }

    fun loadDetailedResult(resultId: Long) {
        _detailedResultState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val entity = repository.getExamResultById(resultId)
            if (entity != null) {
                val questions = repository.getSavedQuestions(entity) ?: repository.getQuestions(entity.packageId)
                val answersMap = mutableMapOf<String, UserAnswer>()

                try {
                    val jsonArray = JSONArray(entity.answersJson)
                    for (i in 0 until jsonArray.length()) {
                        val obj = jsonArray.getJSONObject(i)
                        val qId = obj.getString("questionId")
                        val selKey = obj.optString("selectedKey", "")
                        val isDoubt = obj.optBoolean("isDoubtful", false)
                        val timeSpent = obj.optInt("timeSpent", 0)
                        answersMap[qId] = UserAnswer(
                            questionId = qId,
                            selectedKey = if (selKey.isNotEmpty()) selKey else null,
                            isDoubtful = isDoubt,
                            timeSpentSeconds = timeSpent
                        )
                    }
                } catch (_: Exception) {}

                val mastery = repository.calculateSubCategoryMastery(questions, answersMap)
                val accuracy = repository.calculateAccuracyStatistics(questions, answersMap)
                val realtimeStats = repository.calculateRealtimeStats(questions, answersMap)
                val recommendations = repository.generateImprovementRecommendations(mastery, realtimeStats)

                _detailedResultState.update {
                    DetailedResultUiState(
                        resultEntity = entity,
                        questions = questions,
                        userAnswers = answersMap,
                        subCategoryMastery = mastery,
                        accuracyStats = accuracy,
                        recommendations = recommendations,
                        isLoading = false
                    )
                }
            } else {
                _detailedResultState.update { it.copy(isLoading = false) }
            }
        }
    }

    private val _leaderboard = MutableStateFlow<List<LeaderboardItem>>(repository.getLocalLeaderboard())
    val leaderboardFlow: StateFlow<List<LeaderboardItem>> = _leaderboard.asStateFlow()

    fun loadLeaderboard(userBestScore: Int = 0) {
        viewModelScope.launch {
            try {
                val remote = repository.getLeaderboard(userBestScore)
                if (remote.isNotEmpty()) _leaderboard.value = remote
            } catch (_: Exception) {}
        }
    }

    fun getLeaderboard(userBestScore: Int = 0): List<LeaderboardItem> {
        loadLeaderboard(userBestScore)
        return _leaderboard.value
    }

    fun deleteResult(id: Long) {
        viewModelScope.launch {
            repository.deleteExamResult(id)
        }
    }

    // ==================== AUTH & USER SESSIONS ====================
    val defaultUser = UserProfile(
        id = "usr_001",
        name = "Rian Pratama, S.Tr.Kom",
        email = "peserta@gmail.com",
        role = UserRole.USER,
        participantNumber = "24-3012-008129",
        institution = "Kementerian Komunikasi dan Digital RI",
        targetFormation = "Pranata Komputer Ahli Pertama"
    )

    val defaultAdmin = UserProfile(
        id = "adm_001",
        name = "Bambang Sudirjo, M.Si",
        email = "admin@cpns.go.id",
        role = UserRole.ADMIN,
        participantNumber = "19880415 201212 1 001",
        institution = "Badan Kepegawaian Negara (BKN RI)",
        targetFormation = "Super Admin Tim Penyusun Soal CAT"
    )

    private val _authState = MutableStateFlow(
        AuthState(
            currentUser = defaultUser,
            isLoggedIn = true,
            error = null
        )
    )
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    // ==================== ADMIN QUESTION CMS STATE ====================
    private val _adminQuestions = MutableStateFlow<List<Question>>(repository.getAllAdminQuestions())
    val adminQuestions: StateFlow<List<Question>> = _adminQuestions.asStateFlow()

    fun login(email: String, pass: String, intendedRole: UserRole): Boolean {
        _authState.update { it.copy(isLoading = true, error = null) }
        val cleanEmail = email.trim().lowercase()
        val cleanPass = pass.trim()

        if (cleanEmail.isEmpty() || cleanPass.isEmpty()) {
            _authState.update { it.copy(isLoading = false, error = "Email dan password wajib diisi.") }
            return false
        }

        // Background call to Next.js API
        viewModelScope.launch {
            try {
                val res = ApiClient.apiService.login(LoginRequest(cleanEmail, cleanPass))
                if (res.isSuccessful && res.body()?.success == true) {
                    val token = res.body()?.token
                    val userDto = res.body()?.user
                    if (token != null && userDto != null) {
                        SessionManager.saveSession(token, userDto)
                    }
                }
            } catch (_: Exception) {}
        }

        if (intendedRole == UserRole.ADMIN) {
            if (cleanEmail == "admin@cpns.go.id" && (cleanPass == "admin123" || cleanPass == "admin12345678")) {
                _authState.update {
                    it.copy(
                        currentUser = defaultAdmin,
                        isLoggedIn = true,
                        isLoading = false,
                        error = null
                    )
                }
                return true
            } else {
                _authState.update {
                    it.copy(
                        isLoading = false,
                        error = "Kredensial Admin salah. Gunakan admin@cpns.go.id / admin123"
                    )
                }
                return false
            }
        } else {
            val user = if (cleanEmail == "peserta@gmail.com") {
                defaultUser
            } else {
                val usernamePart = cleanEmail.substringBefore("@").replaceFirstChar { it.uppercase() }
                UserProfile(
                    id = "usr_${System.currentTimeMillis() % 10000}",
                    name = "$usernamePart Pratama",
                    email = cleanEmail,
                    role = UserRole.USER,
                    participantNumber = "24-3012-${(1000..9999).random()}",
                    institution = "Kementerian / Lembaga RI",
                    targetFormation = "Calon Pegawai Negeri Sipil (CPNS)"
                )
            }
            _authState.update {
                it.copy(
                    currentUser = user,
                    isLoggedIn = true,
                    isLoading = false,
                    error = null
                )
            }
            return true
        }
    }

    fun quickLogin(role: UserRole) {
        val user = if (role == UserRole.ADMIN) defaultAdmin else defaultUser
        _authState.update {
            it.copy(currentUser = user, isLoggedIn = true, isLoading = false, error = null)
        }
    }

    fun logout() {
        SessionManager.clearSession()
        _authState.update {
            it.copy(currentUser = null, isLoggedIn = false, error = null)
        }
    }

    fun switchRole(role: UserRole) {
        quickLogin(role)
    }

    fun refreshAdminQuestions() {
        _adminQuestions.value = repository.getAllAdminQuestions()
    }

    fun saveQuestion(question: Question) {
        val existing = _adminQuestions.value.find { it.id == question.id }
        if (existing != null) {
            repository.updateQuestion(question)
        } else {
            repository.addQuestion(question)
        }
        refreshAdminQuestions()
    }

    fun deleteQuestion(questionId: String) {
        repository.deleteQuestion(questionId)
        refreshAdminQuestions()
    }
}
