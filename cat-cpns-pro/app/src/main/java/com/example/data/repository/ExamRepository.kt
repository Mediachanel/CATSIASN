package com.example.data.repository

import com.example.data.db.AppDatabase
import com.example.data.db.BookmarkEntity
import com.example.data.db.ExamResultEntity
import com.example.data.model.AccuracyStatistics
import com.example.data.model.ExamCategory
import com.example.data.model.ExamPackage
import com.example.data.model.ExamRealtimeStats
import com.example.data.model.ImprovementRecommendation
import com.example.data.model.LeaderboardItem
import com.example.data.model.PackageType
import com.example.data.model.PriorityLevel
import com.example.data.model.Question
import com.example.data.model.QuestionOption
import com.example.data.model.QuestionOption
import com.example.data.model.SubCategoryMastery
import com.example.data.model.UserAnswer
import com.example.data.network.ApiClient
import com.example.data.network.CatApiService
import com.example.data.network.ExamResultDto
import com.example.data.network.ExamReviewDto
import com.example.data.network.SaveAnswerRequest
import com.example.data.network.SessionManager
import com.example.data.network.StartExamRequest
import com.example.data.network.SubmitExamRequest
import com.example.data.seed.QuestionBankData
import com.example.data.seed.StudyMaterial
import com.example.data.seed.StudyMaterialData
import kotlinx.coroutines.flow.Flow
import org.json.JSONArray
import org.json.JSONObject

class ExamRepository(
    private val database: AppDatabase,
    private val apiService: CatApiService = ApiClient.apiService
) {

    private val resultDao = database.examResultDao()
    private val bookmarkDao = database.bookmarkDao()

    fun getPackages(): List<ExamPackage> = QuestionBankData.availablePackages

    suspend fun fetchRemotePackages(): List<ExamPackage> {
        return try {
            val response = apiService.getPackages()
            if (response.isSuccessful && response.body()?.success == true) {
                val list = response.body()?.data ?: emptyList()
                if (list.isNotEmpty()) {
                    list.map { dto ->
                        ExamPackage(
                            id = dto.id,
                            title = dto.title,
                            badge = dto.badge,
                            description = dto.description,
                            type = try { PackageType.valueOf(dto.type) } catch (e: Exception) { PackageType.FULL_SKD },
                            durationMinutes = dto.durationMinutes,
                            totalQuestions = dto.totalQuestions,
                            difficulty = dto.difficulty,
                            isRecommended = dto.isRecommended,
                            isRandomized = dto.isRandomized
                        )
                    }
                } else {
                    QuestionBankData.availablePackages
                }
            } else {
                QuestionBankData.availablePackages
            }
        } catch (e: Exception) {
            QuestionBankData.availablePackages
        }
    }

    suspend fun startOnlineSession(packageId: String): Pair<String, List<Question>>? {
        val token = SessionManager.getBearerToken() ?: return null
        return try {
            val startRes = apiService.startExam(token, StartExamRequest(packageId))
            if (!startRes.isSuccessful || startRes.body()?.success != true) return null
            val sessionDto = startRes.body()?.data ?: return null

            val qRes = apiService.getQuestions(token, sessionDto.sessionId)
            if (!qRes.isSuccessful || qRes.body()?.success != true) return null
            val qSession = qRes.body()?.data ?: return null

            val questions = qSession.questions.map { qDto ->
                val category = try {
                    ExamCategory.valueOf(qDto.category.uppercase())
                } catch (e: Exception) {
                    ExamCategory.TWK
                }
                Question(
                    id = qDto.id,
                    packageId = packageId,
                    number = qDto.number,
                    category = category,
                    subCategory = qDto.subCategory,
                    questionText = qDto.questionText,
                    options = qDto.options.map { opt ->
                        QuestionOption(key = opt.key, text = opt.text, score = opt.score)
                    },
                    correctKey = qDto.correctKey,
                    explanation = qDto.explanation,
                    fastTips = qDto.fastTips
                )
            }
            Pair(sessionDto.sessionId, questions)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun saveAnswerRemote(
        sessionId: String,
        position: Int,
        selectedOption: String,
        isDoubtful: Boolean = false
    ): Boolean {
        val token = SessionManager.getBearerToken() ?: return false
        return try {
            val res = apiService.saveAnswer(
                token,
                SaveAnswerRequest(
                    sessionId = sessionId,
                    position = position,
                    selectedOption = selectedOption,
                    isDoubtful = isDoubtful
                )
            )
            res.isSuccessful && res.body()?.success == true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun submitOnlineExam(sessionId: String): ExamResultDto? {
        val token = SessionManager.getBearerToken() ?: return null
        return try {
            val res = apiService.submitExam(token, SubmitExamRequest(sessionId))
            if (res.isSuccessful && res.body()?.success == true) {
                res.body()?.data
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getExamReviewRemote(sessionId: String): ExamReviewDto? {
        val token = SessionManager.getBearerToken() ?: return null
        return try {
            val res = apiService.getExamReview(token, sessionId)
            if (res.isSuccessful && res.body()?.success == true) {
                res.body()?.data
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    fun getQuestions(packageId: String): List<Question> =
        QuestionBankData.getQuestionsForPackage(packageId)

    fun getAllAdminQuestions(): List<Question> =
        QuestionBankData.getAllAdminQuestions()

    fun addQuestion(question: Question) {
        QuestionBankData.addQuestion(question)
    }

    fun updateQuestion(question: Question) {
        QuestionBankData.updateQuestion(question)
    }

    fun deleteQuestion(questionId: String): Boolean =
        QuestionBankData.deleteQuestion(questionId)

    fun calculateRealtimeStats(
        questions: List<Question>,
        answers: Map<String, UserAnswer>
    ): ExamRealtimeStats {
        var twkScore = 0
        var tiuScore = 0
        var tkpScore = 0
        var answeredCount = 0
        var doubtfulCount = 0

        for (question in questions) {
            val answer = answers[question.id]
            if (answer != null) {
                if (answer.isDoubtful) {
                    doubtfulCount++
                }
                if (!answer.selectedKey.isNullOrEmpty()) {
                    answeredCount++
                    val selectedOption = question.options.find { it.key == answer.selectedKey }
                    val points = selectedOption?.score ?: 0

                    when (question.category) {
                        ExamCategory.TWK -> twkScore += points
                        ExamCategory.TIU -> tiuScore += points
                        ExamCategory.TKP -> tkpScore += points
                    }
                }
            }
        }

        val totalScore = twkScore + tiuScore + tkpScore
        val twkPassed = twkScore >= ExamCategory.TWK.passingGrade
        val tiuPassed = tiuScore >= ExamCategory.TIU.passingGrade
        val tkpPassed = tkpScore >= ExamCategory.TKP.passingGrade
        val allPassed = twkPassed && tiuPassed && tkpPassed

        return ExamRealtimeStats(
            twkScore = twkScore,
            tiuScore = tiuScore,
            tkpScore = tkpScore,
            totalScore = totalScore,
            answeredCount = answeredCount,
            doubtfulCount = doubtfulCount,
            unansweredCount = (questions.size - answeredCount).coerceAtLeast(0),
            twkPassed = twkPassed,
            tiuPassed = tiuPassed,
            tkpPassed = tkpPassed,
            allPassed = allPassed
        )
    }

    suspend fun saveExamResult(
        pkg: ExamPackage,
        questions: List<Question>,
        answers: Map<String, UserAnswer>,
        timeSpentSeconds: Int
    ): Long {
        val stats = calculateRealtimeStats(questions, answers)

        val answersJsonArray = JSONArray()
        for (q in questions) {
            val ans = answers[q.id]
            val obj = JSONObject().apply {
                put("questionId", q.id)
                put("question", JSONObject().apply {
                    put("id", q.id)
                    put("packageId", q.packageId)
                    put("number", q.number)
                    put("category", q.category.name)
                    put("subCategory", q.subCategory)
                    put("questionText", q.questionText)
                    put("correctKey", q.correctKey ?: JSONObject.NULL)
                    put("explanation", q.explanation)
                    put("fastTips", q.fastTips)
                    put("options", JSONArray().apply {
                        q.options.forEach { option ->
                            put(JSONObject().put("key", option.key).put("text", option.text).put("score", option.score))
                        }
                    })
                })
                put("selectedKey", ans?.selectedKey ?: "")
                put("isDoubtful", ans?.isDoubtful ?: false)
                put("timeSpent", ans?.timeSpentSeconds ?: 0)
            }
            answersJsonArray.put(obj)
        }

        val entity = ExamResultEntity(
            packageId = pkg.id,
            packageName = pkg.title,
            packageType = pkg.type.name,
            timestamp = System.currentTimeMillis(),
            timeSpentSeconds = timeSpentSeconds,
            totalTimeSeconds = pkg.durationMinutes * 60,
            twkScore = stats.twkScore,
            tiuScore = stats.tiuScore,
            tkpScore = stats.tkpScore,
            totalScore = stats.totalScore,
            isPassed = stats.allPassed,
            twkPassed = stats.twkPassed,
            tiuPassed = stats.tiuPassed,
            tkpPassed = stats.tkpPassed,
            answeredCount = stats.answeredCount,
            totalQuestions = questions.size,
            answersJson = answersJsonArray.toString()
        )

        return resultDao.insertResult(entity)
    }

    fun getAllExamResults(): Flow<List<ExamResultEntity>> = resultDao.getAllResults()

    suspend fun getExamResultById(id: Long): ExamResultEntity? = resultDao.getResultById(id)

    fun getSavedQuestions(result: ExamResultEntity): List<Question>? = try {
        val entries = JSONArray(result.answersJson)
        val questions = (0 until entries.length()).map { index ->
            val q = entries.getJSONObject(index).getJSONObject("question")
            val options = q.getJSONArray("options")
            Question(
                id = q.getString("id"), packageId = q.getString("packageId"),
                number = q.getInt("number"), category = ExamCategory.valueOf(q.getString("category")),
                subCategory = q.getString("subCategory"), questionText = q.getString("questionText"),
                options = (0 until options.length()).map { optionIndex ->
                    val option = options.getJSONObject(optionIndex)
                    QuestionOption(option.getString("key"), option.getString("text"), option.getInt("score"))
                },
                correctKey = if (q.isNull("correctKey")) null else q.getString("correctKey"),
                explanation = q.getString("explanation"), fastTips = q.getString("fastTips")
            )
        }
        questions.takeIf { it.isNotEmpty() }
    } catch (_: Exception) { null }

    fun getTotalExamsCount(): Flow<Int> = resultDao.getTotalExamsCount()

    fun getAverageTotalScore(): Flow<Double?> = resultDao.getAverageTotalScore()

    fun getHighestTotalScore(): Flow<Int?> = resultDao.getHighestTotalScore()

    suspend fun deleteExamResult(id: Long) = resultDao.deleteResult(id)

    // Bookmarks
    fun getAllBookmarks(): Flow<List<BookmarkEntity>> = bookmarkDao.getAllBookmarks()

    fun isBookmarked(questionId: String): Flow<Boolean> = bookmarkDao.isBookmarked(questionId)

    suspend fun toggleBookmark(question: Question, isCurrentlyBookmarked: Boolean) {
        if (isCurrentlyBookmarked) {
            bookmarkDao.removeBookmark(question.id)
        } else {
            val entity = BookmarkEntity(
                questionId = question.id,
                packageId = question.packageId,
                category = question.category.name,
                subCategory = question.subCategory,
                questionText = question.questionText
            )
            bookmarkDao.insertBookmark(entity)
        }
    }

    suspend fun getStudyMaterials(): List<StudyMaterial> {
        return try {
            val res = apiService.getStudyMaterials()
            if (res.isSuccessful && res.body()?.success == true) {
                val list = res.body()?.data ?: emptyList()
                if (list.isNotEmpty()) {
                    list.map { dto ->
                        val cat = try {
                            ExamCategory.valueOf(dto.category.uppercase())
                        } catch (e: Exception) {
                            ExamCategory.TWK
                        }
                        StudyMaterial(
                            id = dto.id,
                            category = cat,
                            title = dto.title,
                            summary = dto.summary,
                            points = dto.points
                        )
                    }
                } else {
                    StudyMaterialData.materials
                }
            } else {
                StudyMaterialData.materials
            }
        } catch (e: Exception) {
            StudyMaterialData.materials
        }
    }

    fun getLocalLeaderboard(userBestScore: Int = 0): List<LeaderboardItem> =
        StudyMaterialData.getSimulatedLeaderboard(userBestScore)

    suspend fun getLeaderboard(userBestScore: Int = 0): List<LeaderboardItem> {
        return try {
            val res = apiService.getLeaderboard()
            if (res.isSuccessful && res.body()?.success == true) {
                val list = res.body()?.data ?: emptyList()
                if (list.isNotEmpty()) {
                    list.map { dto ->
                        LeaderboardItem(
                            rank = dto.rank,
                            name = dto.userName,
                            targetFormation = "Analis Kebijakan / Ahli Pertama",
                            twk = dto.twkScore,
                            tiu = dto.tiuScore,
                            tkp = dto.tkpScore,
                            totalScore = dto.totalScore,
                            isCurrentUser = false
                        )
                    }
                } else {
                    StudyMaterialData.getSimulatedLeaderboard(userBestScore)
                }
            } else {
                StudyMaterialData.getSimulatedLeaderboard(userBestScore)
            }
        } catch (e: Exception) {
            StudyMaterialData.getSimulatedLeaderboard(userBestScore)
        }
    }

    fun calculateSubCategoryMastery(
        questions: List<Question>,
        answers: Map<String, UserAnswer>
    ): List<SubCategoryMastery> {
        val grouped = questions.groupBy { it.subCategory }
        return grouped.map { (subCat, qList) ->
            val category = qList.first().category
            var scoredPoints = 0
            var maxPossiblePoints = 0

            for (q in qList) {
                val maxPoint = 5
                maxPossiblePoints += maxPoint
                val ans = answers[q.id]?.selectedKey
                if (!ans.isNullOrEmpty()) {
                    val opt = q.options.find { it.key == ans }
                    scoredPoints += opt?.score ?: 0
                }
            }

            val pct = if (maxPossiblePoints > 0) {
                ((scoredPoints.toFloat() / maxPossiblePoints) * 100).toInt().coerceIn(0, 100)
            } else 0

            val priority = when {
                pct < 60 -> PriorityLevel.HIGH
                pct < 80 -> PriorityLevel.MEDIUM
                else -> PriorityLevel.GOOD
            }

            SubCategoryMastery(
                subCategory = subCat,
                category = category,
                percentage = pct,
                answeredCount = qList.count { !answers[it.id]?.selectedKey.isNullOrEmpty() },
                totalQuestions = qList.size,
                priorityLevel = priority
            )
        }.sortedBy { it.percentage }
    }

    fun calculateAccuracyStatistics(
        questions: List<Question>,
        answers: Map<String, UserAnswer>
    ): AccuracyStatistics = calculateAccuracyStats(questions, answers)

    fun calculateAccuracyStats(
        questions: List<Question>,
        answers: Map<String, UserAnswer>
    ): AccuracyStatistics {
        var correctCount = 0
        var totalAnswered = 0
        var totalScore = 0

        for (q in questions) {
            val ans = answers[q.id]?.selectedKey
            if (!ans.isNullOrEmpty()) {
                totalAnswered++
                val opt = q.options.find { it.key == ans }
                val score = opt?.score ?: 0
                totalScore += score

                if (q.category == ExamCategory.TKP) {
                    if (score == 5) correctCount++
                } else {
                    if (ans == q.correctKey) correctCount++
                }
            }
        }

        val accuracyPct = if (totalAnswered > 0) {
            ((correctCount.toFloat() / totalAnswered) * 100).toInt()
        } else 0

        return AccuracyStatistics(
            accuracyPercentage = accuracyPct,
            correctCount = correctCount,
            incorrectCount = (totalAnswered - correctCount).coerceAtLeast(0),
            unansweredCount = (questions.size - totalAnswered).coerceAtLeast(0),
            totalQuestions = questions.size,
            averageScorePerQuestion = if (totalAnswered > 0) totalScore.toFloat() / totalAnswered else 0f
        )
    }

    fun generateImprovementRecommendations(
        questions: List<Question>,
        answers: Map<String, UserAnswer>,
        stats: ExamRealtimeStats
    ): List<ImprovementRecommendation> {
        val masteryList = calculateSubCategoryMastery(questions, answers)
        return generateImprovementRecommendations(masteryList, stats)
    }

    fun generateImprovementRecommendations(
        masteryList: List<SubCategoryMastery>,
        stats: ExamRealtimeStats
    ): List<ImprovementRecommendation> {
        val recommendations = mutableListOf<ImprovementRecommendation>()

        for (mastery in masteryList) {
            if (mastery.percentage < 80) {
                val priority = if (mastery.percentage < 60) PriorityLevel.HIGH else PriorityLevel.MEDIUM
                val topic = mastery.subCategory
                val category = mastery.category

                val (diagnosis, actionPlan, drillPkg) = when {
                    topic.contains("Pancasila", ignoreCase = true) -> Triple(
                        "Sering terkecoh membedakan implementasi Sila ke-2 (kemanusiaan) dan Sila ke-5 (keadilan sosial/distribusi publik).",
                        "Pelajari kata kunci 45 butir Pancasila dan latihan analisis kasus sengketa agraria / bansos.",
                        "pkg_drill_twk"
                    )
                    topic.contains("UUD", ignoreCase = true) || topic.contains("Konstitusi", ignoreCase = true) -> Triple(
                        "Pemahaman pasal amandemen UUD 1945 terkait pembagian wewenang MK, MA, dan KY masih kurang presisi.",
                        "Hafalkan tabel matriks kewenangan lembaga peradilan dan hak prerogatif presiden (Pasal 14 & 15).",
                        "pkg_drill_twk"
                    )
                    topic.contains("Bela Negara", ignoreCase = true) -> Triple(
                        "Konsep bela negara non-militer melalui profesi dan sejarah pergerakan kebangsaan perlu diperdalam.",
                        "Pahami 5 nilai dasar bela negara UU 23/2019 dan kronologi peristiwa menjelang proklamasi kemerdekaan.",
                        "pkg_drill_twk"
                    )
                    topic.contains("Silogisme", ignoreCase = true) -> Triple(
                        "Terkecoh dalam menarik kesimpulan logis pada kombinasi premis universal dan partikular.",
                        "Gunakan rumus mutlak: 'Semua + Sebagian = Sebagian'. Jika ada premis negatif, kesimpulan wajib negatif.",
                        "pkg_drill_tiu"
                    )
                    topic.contains("Deret", ignoreCase = true) || topic.contains("Hitung", ignoreCase = true) -> Triple(
                        "Kecepatan dan ketepatan berhitung numerik masih membutuhkan waktu lama.",
                        "Latih pengenalan deret prima, pola loncat 2 angka, dan substitusi pecahan istimewa (1/8=12.5%, 3/8=37.5%).",
                        "pkg_drill_tiu"
                    )
                    topic.contains("Cerita", ignoreCase = true) || topic.contains("Analitis", ignoreCase = true) -> Triple(
                        "Kurang efisien dalam memodelkan soal cerita pekerja bersama atau kecepatan berpapasan.",
                        "Gunakan rumus cepat proyek terhenti (Libur x Awal / Sisa) dan sketsa urutan posisi duduk langsung di kertas buram.",
                        "pkg_drill_tiu"
                    )
                    topic.contains("Pelayanan", ignoreCase = true) -> Triple(
                        "Pemilihan opsi jawaban belum sepenuhnya mencerminkan orientasi kepuasan masyarakat tanpa diskriminasi.",
                        "Pilihlah selalu opsi yang solutif, proaktif, bertanggung jawab tuntas, dan tidak melempar masalah ke pihak lain.",
                        "pkg_drill_tkp"
                    )
                    topic.contains("Anti-Radikalisme", ignoreCase = true) -> Triple(
                        "Sikap terhadap indikasi intoleransi dan radikalisme di lingkungan dinas masih ragu-ragu.",
                        "ASN wajib memegang teguh Pancasila: bersikap tegas terhadap paham intoleran dan laporkan melalui jalur resmi kepegawaian.",
                        "pkg_drill_tkp"
                    )
                    else -> Triple(
                        "Penguasaan materi pada topik $topic masih berada pada tingkat ${mastery.percentage}%.",
                        "Fokuskan drill mandiri pada topik ini dan baca pembahasan tips sakti bimbel.",
                        when (category) {
                            ExamCategory.TWK -> "pkg_drill_twk"
                            ExamCategory.TIU -> "pkg_drill_tiu"
                            ExamCategory.TKP -> "pkg_drill_tkp"
                        }
                    )
                }

                recommendations.add(
                    ImprovementRecommendation(
                        id = "rec_${mastery.subCategory.hashCode()}",
                        category = category,
                        topic = topic,
                        priority = priority,
                        currentPercentage = mastery.percentage,
                        diagnosis = diagnosis,
                        actionPlan = actionPlan,
                        targetDrillPackageId = drillPkg
                    )
                )
            }
        }

        // If subtests failed passing grade, ensure high priority alerts
        if (!stats.twkPassed && recommendations.none { it.category == ExamCategory.TWK }) {
            recommendations.add(
                0,
                ImprovementRecommendation(
                    id = "rec_twk_critical",
                    category = ExamCategory.TWK,
                    topic = "Passing Grade TWK Kritis",
                    priority = PriorityLevel.HIGH,
                    currentPercentage = ((stats.twkScore / 65f) * 100).toInt().coerceAtMost(99),
                    diagnosis = "Skor TWK (${stats.twkScore}) berada di bawah ambang batas minimal 65.",
                    actionPlan = "Tingkatkan akurasi hafalan pasal UUD 1945, implementasi butir Pancasila, dan 9 nilai integritas KPK.",
                    targetDrillPackageId = "pkg_drill_twk"
                )
            )
        }
        if (!stats.tiuPassed && recommendations.none { it.category == ExamCategory.TIU }) {
            recommendations.add(
                0,
                ImprovementRecommendation(
                    id = "rec_tiu_critical",
                    category = ExamCategory.TIU,
                    topic = "Passing Grade TIU Kritis",
                    priority = PriorityLevel.HIGH,
                    currentPercentage = ((stats.tiuScore / 80f) * 100).toInt().coerceAtMost(99),
                    diagnosis = "Skor TIU (${stats.tiuScore}) belum mencapai ambang batas kelulusan minimal 80.",
                    actionPlan = "Fokus latih trik rumus cepat silogisme logika dan pola deret bilangan tanpa menghitung manual yang panjang.",
                    targetDrillPackageId = "pkg_drill_tiu"
                )
            )
        }
        if (!stats.tkpPassed && recommendations.none { it.category == ExamCategory.TKP }) {
            recommendations.add(
                0,
                ImprovementRecommendation(
                    id = "rec_tkp_critical",
                    category = ExamCategory.TKP,
                    topic = "Passing Grade TKP Kritis",
                    priority = PriorityLevel.HIGH,
                    currentPercentage = ((stats.tkpScore / 166f) * 100).toInt().coerceAtMost(99),
                    diagnosis = "Skor TKP (${stats.tkpScore}) belum memenuhi ambang batas 166 poin.",
                    actionPlan = "Kuasai pola mindset ASN BerAKHLAK untuk konsisten meraih opsi 5 poin di setiap kasus.",
                    targetDrillPackageId = "pkg_drill_tkp"
                )
            )
        }

        return recommendations.take(5)
    }
}
