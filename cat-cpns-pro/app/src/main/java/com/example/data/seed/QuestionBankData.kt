package com.example.data.seed

import com.example.data.model.ExamCategory
import com.example.data.model.ExamPackage
import com.example.data.model.PackageType
import com.example.data.model.Question
import com.example.data.model.QuestionOption
import kotlin.random.Random

object QuestionBankData {

    val masterTwkPool: MutableList<Question> = TwkQuestionBank.questions.toMutableList()
    val masterTiuPool: MutableList<Question> = TiuQuestionBank.questions.toMutableList()
    val masterTkpPool: MutableList<Question> = TkpQuestionBank.questions.toMutableList()

    fun getAllAdminQuestions(): List<Question> = masterTwkPool + masterTiuPool + masterTkpPool

    fun addQuestion(question: Question) {
        when (question.category) {
            ExamCategory.TWK -> masterTwkPool.add(0, question)
            ExamCategory.TIU -> masterTiuPool.add(0, question)
            ExamCategory.TKP -> masterTkpPool.add(0, question)
        }
    }

    fun updateQuestion(question: Question) {
        fun replaceInList(list: MutableList<Question>) {
            val idx = list.indexOfFirst { it.id == question.id }
            if (idx != -1) {
                list[idx] = question
            }
        }
        replaceInList(masterTwkPool)
        replaceInList(masterTiuPool)
        replaceInList(masterTkpPool)
    }

    fun deleteQuestion(questionId: String): Boolean {
        val removedTwk = masterTwkPool.removeAll { it.id == questionId }
        val removedTiu = masterTiuPool.removeAll { it.id == questionId }
        val removedTkp = masterTkpPool.removeAll { it.id == questionId }
        return removedTwk || removedTiu || removedTkp
    }

    val availablePackages: List<ExamPackage> = listOf(
        ExamPackage(
            id = "pkg_random_skd",
            title = "Simulasi CAT SKD Acak Baru",
            badge = "UJIAN ACAK RESMI",
            description = "Meniru kondisi riil tes CAT BKN! Setiap sesi menghasilkan kombinasi 110 soal acak yang berbeda (TWK 30, TIU 35, TKP 45) dengan durasi 100 menit.",
            type = PackageType.RANDOM_FULL_SKD,
            durationMinutes = 100,
            totalQuestions = 110,
            difficulty = "Acak Adaptif (Standar BKN)",
            isRecommended = true,
            isRandomized = true
        ),
        ExamPackage(
            id = "pkg_full_skd_1",
            title = "Simulasi Lengkap SKD BKN - Paket 1",
            badge = "STANDAR BKN",
            description = "Format resmi MenPAN-RB: TWK 30 soal, TIU 35 soal, TKP 45 soal. Waktu 100 menit dengan sistem penilaian CAT asli.",
            type = PackageType.FULL_SKD,
            durationMinutes = 100,
            totalQuestions = 110,
            difficulty = "Standar HOTS 2024/2025"
        ),
        ExamPackage(
            id = "pkg_full_skd_2",
            title = "Simulasi Prediksi Lolos SKD - Paket 2",
            badge = "PREDIKSI AKURAT",
            description = "Paket simulasi komprehensif dengan penekanan pada soal HOTS Penalaran Integritas dan Logika Analitis.",
            type = PackageType.FULL_SKD,
            durationMinutes = 100,
            totalQuestions = 110,
            difficulty = "Tinggi (HOTS Advance)"
        ),
        ExamPackage(
            id = "pkg_drill_twk",
            title = "Drill Spesialis TWK (Pilar & Integritas)",
            badge = "DRILL TWK",
            description = "Fokus pendalaman Tes Wawasan Kebangsaan untuk menembus Passing Grade 65. Nilai benar 5, salah 0.",
            type = PackageType.TWK_ONLY,
            durationMinutes = 30,
            totalQuestions = 30,
            difficulty = "Sedang - Tinggi"
        ),
        ExamPackage(
            id = "pkg_drill_tiu",
            title = "Drill Spesialis TIU (Numerik & Silogisme)",
            badge = "DRILL TIU",
            description = "Latihan intensif berhitung cepat, deret angka, silogisme, dan kemampuan figural untuk Passing Grade 80.",
            type = PackageType.TIU_ONLY,
            durationMinutes = 35,
            totalQuestions = 35,
            difficulty = "Tinggi (Trik Rumus Cepat)"
        ),
        ExamPackage(
            id = "pkg_drill_tkp",
            title = "Drill Spesialis TKP (Poin 5 ASN)",
            badge = "DRILL TKP",
            description = "Kuasai mindset ASN BerAKHLAK dan Anti-Radikalisme untuk meraup poin 5 di setiap soal TKP (PG 166).",
            type = PackageType.TKP_ONLY,
            durationMinutes = 45,
            totalQuestions = 45,
            difficulty = "Trik Jawaban Tertinggi"
        ),
        ExamPackage(
            id = "pkg_quick_quiz",
            title = "Simulasi Kilat Harian (15 Soal Mix)",
            badge = "KUIS KILAT",
            description = "Latihan kilat 15 menit untuk mengasah kecepatan berpikir dan akurasi di sela kesibukan.",
            type = PackageType.QUICK_DRILL,
            durationMinutes = 15,
            totalQuestions = 15,
            difficulty = "Campuran Harian"
        )
    )

    fun getQuestionsForPackage(packageId: String): List<Question> {
        val isRandom = packageId == "pkg_random_skd" || packageId.startsWith("random_")

        return when {
            isRandom -> {
                generateRandomizedFullSkd(packageId)
            }
            packageId == "pkg_full_skd_1" -> {
                val twk = expandAndOrder(masterTwkPool, 30, ExamCategory.TWK, packageId, 1, shuffle = false)
                val tiu = expandAndOrder(masterTiuPool, 35, ExamCategory.TIU, packageId, 31, shuffle = false)
                val tkp = expandAndOrder(masterTkpPool, 45, ExamCategory.TKP, packageId, 66, shuffle = false)
                twk + tiu + tkp
            }
            packageId == "pkg_full_skd_2" -> {
                val twk = expandAndOrder(masterTwkPool.reversed(), 30, ExamCategory.TWK, packageId, 1, shuffle = false)
                val tiu = expandAndOrder(masterTiuPool.reversed(), 35, ExamCategory.TIU, packageId, 31, shuffle = false)
                val tkp = expandAndOrder(masterTkpPool.reversed(), 45, ExamCategory.TKP, packageId, 66, shuffle = false)
                twk + tiu + tkp
            }
            packageId == "pkg_drill_twk" -> {
                expandAndOrder(masterTwkPool, 30, ExamCategory.TWK, packageId, 1, shuffle = true)
            }
            packageId == "pkg_drill_tiu" -> {
                expandAndOrder(masterTiuPool, 35, ExamCategory.TIU, packageId, 1, shuffle = true)
            }
            packageId == "pkg_drill_tkp" -> {
                expandAndOrder(masterTkpPool, 45, ExamCategory.TKP, packageId, 1, shuffle = true)
            }
            packageId == "pkg_quick_quiz" -> {
                val twk = masterTwkPool.shuffled().take(5)
                val tiu = masterTiuPool.shuffled().take(5)
                val tkp = masterTkpPool.shuffled().take(5)
                var idx = 1
                (twk + tiu + tkp).map { q ->
                    q.copy(id = "${packageId}_q${idx}", packageId = packageId, number = idx++)
                }
            }
            else -> {
                expandAndOrder(masterTwkPool, 30, ExamCategory.TWK, packageId, 1, shuffle = true)
            }
        }
    }

    fun generateRandomizedFullSkd(customPackageId: String = "pkg_random_skd"): List<Question> {
        val randomSeed = System.currentTimeMillis()
        val twk = expandAndOrder(masterTwkPool.shuffled(Random(randomSeed)), 30, ExamCategory.TWK, customPackageId, 1, shuffle = true)
        val tiu = expandAndOrder(masterTiuPool.shuffled(Random(randomSeed + 1)), 35, ExamCategory.TIU, customPackageId, 31, shuffle = true)
        val tkp = expandAndOrder(masterTkpPool.shuffled(Random(randomSeed + 2)), 45, ExamCategory.TKP, customPackageId, 66, shuffle = true)
        return twk + tiu + tkp
    }

    private fun expandAndOrder(
        seedList: List<Question>,
        targetCount: Int,
        category: ExamCategory,
        packageId: String,
        startNumber: Int,
        shuffle: Boolean
    ): List<Question> {
        val list = if (shuffle) seedList.shuffled() else seedList
        val result = mutableListOf<Question>()
        var currentNumber = startNumber
        var index = 0
        while (result.size < targetCount) {
            val template = list[index % list.size]
            val uniqueSuffix = if (index >= list.size) "_var${index / list.size}" else ""
            result.add(
                template.copy(
                    id = "${packageId}_${category.code}_${currentNumber}$uniqueSuffix",
                    packageId = packageId,
                    number = currentNumber
                )
            )
            currentNumber++
            index++
        }
        return result
    }
}
