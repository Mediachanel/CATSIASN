package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.AppDatabase
import com.example.data.model.ExamCategory
import com.example.data.model.ExamPackage
import com.example.data.model.ExamRealtimeStats
import com.example.data.model.Question
import com.example.data.model.UserAnswer
import com.example.data.network.SessionManager
import com.example.data.repository.ExamRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ExamUiState(
    val currentPackage: ExamPackage? = null,
    val questions: List<Question> = emptyList(),
    val currentIndex: Int = 0,
    val answers: Map<String, UserAnswer> = emptyMap(),
    val remainingTimeSeconds: Int = 0,
    val totalTimeSeconds: Int = 0,
    val isTimerRunning: Boolean = false,
    val realtimeStats: ExamRealtimeStats = ExamRealtimeStats(),
    val showLiveScoreOverlay: Boolean = true,
    val showFinishConfirmDialog: Boolean = false,
    val showQuestionGridSheet: Boolean = false,
    val isExamFinished: Boolean = false,
    val savedResultId: Long? = null,
    val isBookmarked: Boolean = false,
    val onlineSessionId: String? = null,
    val isOnlineMode: Boolean = false,
    val isSyncing: Boolean = false
)

sealed interface ExamNavigationEvent {
    data class NavigateToResult(val resultId: Long) : ExamNavigationEvent
    data object NavigateBack : ExamNavigationEvent
}

class ExamViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ExamRepository(AppDatabase.getDatabase(application))

    private val _uiState = MutableStateFlow(ExamUiState())
    val uiState: StateFlow<ExamUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<ExamNavigationEvent>()
    val navigationEvent: SharedFlow<ExamNavigationEvent> = _navigationEvent.asSharedFlow()

    private var timerJob: Job? = null
    private var bookmarkCheckJob: Job? = null

    init {
        SessionManager.init(application)
    }

    fun startExam(packageId: String, forceNewSession: Boolean = false) {
        timerJob?.cancel()

        viewModelScope.launch {
            val packages = repository.getPackages()
            val selectedPkg = packages.find { it.id == packageId } ?: packages.first()
            val durationSeconds = selectedPkg.durationMinutes * 60

            // Check if online mode is available
            var onlineSessionId: String? = null
            var questions: List<Question> = emptyList()
            var isOnline = false

            if (SessionManager.getBearerToken() != null) {
                _uiState.update { it.copy(isSyncing = true) }
                val onlineResult = repository.startOnlineSession(packageId)
                if (onlineResult != null && onlineResult.second.isNotEmpty()) {
                    onlineSessionId = onlineResult.first
                    questions = onlineResult.second
                    isOnline = true
                }
            }

            // Fallback to local questions if offline or not logged in
            if (questions.isEmpty()) {
                questions = repository.getQuestions(packageId)
                isOnline = false
            }

            _uiState.update {
                ExamUiState(
                    currentPackage = selectedPkg,
                    questions = questions,
                    currentIndex = 0,
                    answers = emptyMap(),
                    remainingTimeSeconds = durationSeconds,
                    totalTimeSeconds = durationSeconds,
                    isTimerRunning = true,
                    realtimeStats = repository.calculateRealtimeStats(questions, emptyMap()),
                    showLiveScoreOverlay = true,
                    isExamFinished = false,
                    savedResultId = null,
                    onlineSessionId = onlineSessionId,
                    isOnlineMode = isOnline,
                    isSyncing = false
                )
            }

            checkCurrentBookmark()
            startTimer()
        }
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000L)
                val currentRemaining = _uiState.value.remainingTimeSeconds
                if (currentRemaining <= 1) {
                    _uiState.update { it.copy(remainingTimeSeconds = 0, isTimerRunning = false) }
                    finishExam()
                    break
                } else {
                    val newRemaining = currentRemaining - 1
                    _uiState.update { it.copy(remainingTimeSeconds = newRemaining) }

                    // Alert vibrate at exactly 5 minutes remaining (300s)
                    if (newRemaining == 300) {
                        triggerVibration(longArrayOf(0, 300, 200, 300))
                    }
                }
            }
        }
    }

    fun selectOption(optionKey: String) {
        val state = _uiState.value
        val currentQuestion = state.questions.getOrNull(state.currentIndex) ?: return
        val currentAnswer = state.answers[currentQuestion.id] ?: UserAnswer(currentQuestion.id)

        val updatedAnswer = currentAnswer.copy(selectedKey = optionKey)
        val updatedAnswers = state.answers.toMutableMap().apply {
            put(currentQuestion.id, updatedAnswer)
        }

        val updatedStats = repository.calculateRealtimeStats(state.questions, updatedAnswers)

        _uiState.update {
            it.copy(
                answers = updatedAnswers,
                realtimeStats = updatedStats
            )
        }

        triggerTapHaptic()

        // Sync to backend if session is online
        val sessionId = state.onlineSessionId
        if (sessionId != null) {
            viewModelScope.launch {
                repository.saveAnswerRemote(
                    sessionId = sessionId,
                    position = state.currentIndex + 1,
                    selectedOption = optionKey,
                    isDoubtful = updatedAnswer.isDoubtful
                )
            }
        }
    }

    fun toggleDoubtful() {
        val state = _uiState.value
        val currentQuestion = state.questions.getOrNull(state.currentIndex) ?: return
        val currentAnswer = state.answers[currentQuestion.id] ?: UserAnswer(currentQuestion.id)

        val updatedAnswer = currentAnswer.copy(isDoubtful = !currentAnswer.isDoubtful)
        val updatedAnswers = state.answers.toMutableMap().apply {
            put(currentQuestion.id, updatedAnswer)
        }

        val updatedStats = repository.calculateRealtimeStats(state.questions, updatedAnswers)

        _uiState.update {
            it.copy(
                answers = updatedAnswers,
                realtimeStats = updatedStats
            )
        }

        triggerTapHaptic()

        // Sync to backend if session is online
        val sessionId = state.onlineSessionId
        if (sessionId != null && !updatedAnswer.selectedKey.isNullOrEmpty()) {
            viewModelScope.launch {
                repository.saveAnswerRemote(
                    sessionId = sessionId,
                    position = state.currentIndex + 1,
                    selectedOption = updatedAnswer.selectedKey,
                    isDoubtful = updatedAnswer.isDoubtful
                )
            }
        }
    }

    fun nextQuestion() {
        val state = _uiState.value
        if (state.currentIndex < state.questions.size - 1) {
            _uiState.update { it.copy(currentIndex = it.currentIndex + 1) }
            checkCurrentBookmark()
        }
    }

    fun previousQuestion() {
        val state = _uiState.value
        if (state.currentIndex > 0) {
            _uiState.update { it.copy(currentIndex = it.currentIndex - 1) }
            checkCurrentBookmark()
        }
    }

    fun jumpToQuestion(index: Int) {
        if (index in 0 until _uiState.value.questions.size) {
            _uiState.update { it.copy(currentIndex = index, showQuestionGridSheet = false) }
            checkCurrentBookmark()
        }
    }

    fun toggleQuestionGridSheet(show: Boolean) {
        _uiState.update { it.copy(showQuestionGridSheet = show) }
    }

    fun toggleLiveScoreOverlay() {
        _uiState.update { it.copy(showLiveScoreOverlay = !it.showLiveScoreOverlay) }
    }

    fun showFinishConfirmation(show: Boolean) {
        _uiState.update { it.copy(showFinishConfirmDialog = show) }
    }

    fun finishExam() {
        timerJob?.cancel()
        val state = _uiState.value
        val pkg = state.currentPackage ?: return
        val timeSpent = (state.totalTimeSeconds - state.remainingTimeSeconds).coerceAtLeast(1)

        viewModelScope.launch {
            // Submit online if available
            val sessionId = state.onlineSessionId
            if (sessionId != null) {
                repository.submitOnlineExam(sessionId)
            }

            val resultId = repository.saveExamResult(
                pkg = pkg,
                questions = state.questions,
                answers = state.answers,
                timeSpentSeconds = timeSpent
            )

            _uiState.update {
                it.copy(
                    isExamFinished = true,
                    isTimerRunning = false,
                    showFinishConfirmDialog = false,
                    savedResultId = resultId
                )
            }

            _navigationEvent.emit(ExamNavigationEvent.NavigateToResult(resultId))
        }
    }

    private fun checkCurrentBookmark() {
        val state = _uiState.value
        val currentQuestion = state.questions.getOrNull(state.currentIndex) ?: return
        bookmarkCheckJob?.cancel()
        bookmarkCheckJob = viewModelScope.launch {
            repository.isBookmarked(currentQuestion.id).collect { isBookmarked ->
                _uiState.update { it.copy(isBookmarked = isBookmarked) }
            }
        }
    }

    fun toggleBookmarkCurrent() {
        val state = _uiState.value
        val currentQuestion = state.questions.getOrNull(state.currentIndex) ?: return
        viewModelScope.launch {
            repository.toggleBookmark(currentQuestion, state.isBookmarked)
        }
    }

    private fun triggerTapHaptic() {
        try {
            val context = getApplication<Application>()
            val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vibratorManager?.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createOneShot(30, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(30)
            }
        } catch (_: Exception) {}
    }

    private fun triggerVibration(pattern: LongArray) {
        try {
            val context = getApplication<Application>()
            val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vibratorManager?.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createWaveform(pattern, -1))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(pattern, -1)
            }
        } catch (_: Exception) {}
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        bookmarkCheckJob?.cancel()
    }
}
