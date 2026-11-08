package com.example.data.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginRequest(
    val email: String,
    val password: String
)

@JsonClass(generateAdapter = true)
data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)

@JsonClass(generateAdapter = true)
data class UserDto(
    val id: String,
    val email: String,
    val name: String,
    val role: String
)

@JsonClass(generateAdapter = true)
data class AuthResponse(
    val success: Boolean,
    val token: String? = null,
    val user: UserDto? = null,
    val error: String? = null,
    val message: String? = null
)

@JsonClass(generateAdapter = true)
data class PackageDto(
    val id: String,
    val title: String,
    val badge: String,
    val description: String,
    val type: String,
    val durationMinutes: Int,
    val totalQuestions: Int,
    val twkCount: Int,
    val tiuCount: Int,
    val tkpCount: Int,
    val difficulty: String,
    val isRecommended: Boolean,
    val isRandomized: Boolean
)

@JsonClass(generateAdapter = true)
data class PackageListResponse(
    val success: Boolean,
    val data: List<PackageDto>? = null,
    val error: String? = null
)

@JsonClass(generateAdapter = true)
data class StartExamRequest(
    val eventCode: String = "DEMO-SKD-110"
)

@JsonClass(generateAdapter = true)
data class ExamSessionDto(
    val sessionId: String,
    val eventCode: String,
    val eventTitle: String,
    val deadlineAt: String,
    val remainingSeconds: Int,
    val totalQuestions: Int,
    val durationMinutes: Int,
    val twkCount: Int,
    val tiuCount: Int,
    val tkpCount: Int
)

@JsonClass(generateAdapter = true)
data class StartExamResponse(
    val success: Boolean,
    val data: ExamSessionDto? = null,
    val error: String? = null,
    val message: String? = null
)

@JsonClass(generateAdapter = true)
data class QuestionOptionDto(
    val key: String,
    val text: String,
    val score: Int = 0
)

@JsonClass(generateAdapter = true)
data class QuestionItemDto(
    val id: String,
    val packageId: String,
    val number: Int,
    val category: String,
    val subCategory: String,
    val questionText: String,
    val options: List<QuestionOptionDto>,
    val correctKey: String? = null,
    val explanation: String = "",
    val fastTips: String = "",
    val selectedKey: String? = null,
    val isDoubtful: Boolean = false
)

@JsonClass(generateAdapter = true)
data class QuestionSessionDto(
    val sessionId: String,
    val deadlineAt: String,
    val remainingSeconds: Int,
    val totalQuestions: Int,
    val questions: List<QuestionItemDto>
)

@JsonClass(generateAdapter = true)
data class ExamQuestionsResponse(
    val success: Boolean,
    val data: QuestionSessionDto? = null,
    val error: String? = null,
    val message: String? = null
)

@JsonClass(generateAdapter = true)
data class SaveAnswerRequest(
    val sessionId: String,
    val position: Int,
    val selectedOption: String,
    val isDoubtful: Boolean? = false
)

@JsonClass(generateAdapter = true)
data class ApiResponse(
    val success: Boolean,
    val status: String? = null,
    val error: String? = null,
    val message: String? = null
)

@JsonClass(generateAdapter = true)
data class SubmitExamRequest(
    val sessionId: String
)

@JsonClass(generateAdapter = true)
data class ExamResultDto(
    val id: String? = null,
    val sessionId: String,
    val twkScore: Int,
    val tiuScore: Int,
    val tkpScore: Int,
    val totalScore: Int,
    val twkPassed: Boolean,
    val tiuPassed: Boolean,
    val tkpPassed: Boolean,
    val isPassed: Boolean
)

@JsonClass(generateAdapter = true)
data class SubmitExamResponse(
    val success: Boolean,
    val data: ExamResultDto? = null,
    val error: String? = null,
    val message: String? = null
)

@JsonClass(generateAdapter = true)
data class ReviewItemDto(
    val id: String,
    val number: Int,
    val category: String,
    val subCategory: String,
    val questionText: String,
    val options: List<QuestionOptionDto>,
    val correctKey: String? = null,
    val explanation: String = "",
    val fastTips: String = "",
    val selectedKey: String? = null,
    val scoreEarned: Int = 0,
    val isCorrect: Boolean = false
)

@JsonClass(generateAdapter = true)
data class ExamReviewDto(
    val sessionId: String,
    val eventName: String,
    val result: ExamResultDto? = null,
    val totalQuestions: Int,
    val reviews: List<ReviewItemDto>
)

@JsonClass(generateAdapter = true)
data class ExamReviewResponse(
    val success: Boolean,
    val data: ExamReviewDto? = null,
    val error: String? = null,
    val message: String? = null
)

@JsonClass(generateAdapter = true)
data class LeaderboardItemDto(
    val rank: Int,
    val userId: String,
    val userName: String,
    val twkScore: Int,
    val tiuScore: Int,
    val tkpScore: Int,
    val totalScore: Int,
    val isPassed: Boolean,
    val submittedAt: String
)

@JsonClass(generateAdapter = true)
data class LeaderboardResponse(
    val success: Boolean,
    val data: List<LeaderboardItemDto>? = null,
    val error: String? = null
)

@JsonClass(generateAdapter = true)
data class StudyMaterialDto(
    val id: String,
    val category: String,
    val title: String,
    val summary: String,
    val points: List<String>
)

@JsonClass(generateAdapter = true)
data class StudyMaterialResponse(
    val success: Boolean,
    val data: List<StudyMaterialDto>? = null,
    val error: String? = null
)
