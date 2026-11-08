package com.example.data.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface CatApiService {

    @POST("mobile/auth/login")
    suspend fun login(
        @Body body: LoginRequest
    ): Response<AuthResponse>

    @POST("mobile/auth/register")
    suspend fun register(
        @Body body: RegisterRequest
    ): Response<AuthResponse>

    @GET("mobile/auth/me")
    suspend fun getCurrentUser(
        @Header("Authorization") token: String
    ): Response<AuthResponse>

    @GET("mobile/packages")
    suspend fun getPackages(): Response<PackageListResponse>

    @POST("mobile/exam/start")
    suspend fun startExam(
        @Header("Authorization") token: String,
        @Body body: StartExamRequest
    ): Response<StartExamResponse>

    @GET("mobile/exam/questions")
    suspend fun getQuestions(
        @Header("Authorization") token: String,
        @Query("sessionId") sessionId: String
    ): Response<ExamQuestionsResponse>

    @POST("mobile/exam/answer")
    suspend fun saveAnswer(
        @Header("Authorization") token: String,
        @Body body: SaveAnswerRequest
    ): Response<ApiResponse>

    @POST("mobile/exam/submit")
    suspend fun submitExam(
        @Header("Authorization") token: String,
        @Body body: SubmitExamRequest
    ): Response<SubmitExamResponse>

    @GET("mobile/exam/review")
    suspend fun getExamReview(
        @Header("Authorization") token: String,
        @Query("sessionId") sessionId: String
    ): Response<ExamReviewResponse>

    @GET("mobile/leaderboard")
    suspend fun getLeaderboard(): Response<LeaderboardResponse>

    @GET("mobile/materials")
    suspend fun getStudyMaterials(): Response<StudyMaterialResponse>
}
