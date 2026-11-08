package com.example.data.model

enum class ExamCategory(
    val code: String,
    val title: String,
    val passingGrade: Int,
    val maxScore: Int,
    val standardQuestionCount: Int
) {
    TWK(
        code = "TWK",
        title = "Tes Wawasan Kebangsaan",
        passingGrade = 65,
        maxScore = 150,
        standardQuestionCount = 30
    ),
    TIU(
        code = "TIU",
        title = "Tes Inteligensia Umum",
        passingGrade = 80,
        maxScore = 175,
        standardQuestionCount = 35
    ),
    TKP(
        code = "TKP",
        title = "Tes Karakteristik Pribadi",
        passingGrade = 166,
        maxScore = 225,
        standardQuestionCount = 45
    )
}

enum class PackageType {
    FULL_SKD,
    RANDOM_FULL_SKD,
    TWK_ONLY,
    TIU_ONLY,
    TKP_ONLY,
    QUICK_DRILL
}

enum class PriorityLevel {
    HIGH,   // Needs immediate improvement (< 60% or failed PG)
    MEDIUM, // Borderline (60-79%)
    GOOD    // Strong performance (>= 80%)
}

data class QuestionOption(
    val key: String, // "A", "B", "C", "D", "E"
    val text: String,
    val score: Int = 0 // 1-5 for TKP, 5 if correct for TWK/TIU
)

data class Question(
    val id: String,
    val packageId: String,
    val number: Int,
    val category: ExamCategory,
    val subCategory: String,
    val questionText: String,
    val options: List<QuestionOption>,
    val correctKey: String? = null,
    val explanation: String,
    val fastTips: String = ""
)

data class UserAnswer(
    val questionId: String,
    val selectedKey: String? = null,
    val isDoubtful: Boolean = false,
    val timeSpentSeconds: Int = 0
)

data class ExamPackage(
    val id: String,
    val title: String,
    val badge: String,
    val description: String,
    val type: PackageType,
    val durationMinutes: Int,
    val totalQuestions: Int,
    val difficulty: String,
    val isRecommended: Boolean = false,
    val isRandomized: Boolean = false
)

data class ExamRealtimeStats(
    val twkScore: Int = 0,
    val tiuScore: Int = 0,
    val tkpScore: Int = 0,
    val totalScore: Int = 0,
    val answeredCount: Int = 0,
    val doubtfulCount: Int = 0,
    val unansweredCount: Int = 0,
    val twkPassed: Boolean = false,
    val tiuPassed: Boolean = false,
    val tkpPassed: Boolean = false,
    val allPassed: Boolean = false
)

data class SubCategoryMastery(
    val subCategory: String,
    val category: ExamCategory,
    val scoredPoints: Int,
    val maxPoints: Int,
    val percentage: Int
)

data class AccuracyStatistics(
    val twkCorrectCount: Int,
    val twkTotal: Int,
    val twkAccuracyPct: Int,
    val tiuCorrectCount: Int,
    val tiuTotal: Int,
    val tiuAccuracyPct: Int,
    val tkpPerfectCount: Int, // options with 5 points
    val tkpTotal: Int,
    val tkpPerfectPct: Int,
    val totalAnsweredCount: Int,
    val totalQuestionsCount: Int,
    val overallAccuracyPct: Int
)

data class ImprovementRecommendation(
    val id: String,
    val category: ExamCategory,
    val topic: String,
    val priority: PriorityLevel,
    val currentPercentage: Int,
    val diagnosis: String,
    val actionPlan: String,
    val targetDrillPackageId: String
)

data class LeaderboardItem(
    val rank: Int,
    val participantName: String,
    val regNumber: String,
    val targetFormation: String,
    val twkScore: Int,
    val tiuScore: Int,
    val tkpScore: Int,
    val totalScore: Int,
    val isPassed: Boolean,
    val isCurrentUser: Boolean = false
)
