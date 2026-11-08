package com.example

import com.example.data.model.AccuracyStatistics
import com.example.data.model.ExamCategory
import com.example.data.model.ExamRealtimeStats
import com.example.data.model.Question
import com.example.data.model.QuestionOption
import com.example.data.model.UserAnswer
import com.example.data.seed.QuestionBankData
import com.example.data.seed.TiuQuestionBank
import com.example.data.seed.TkpQuestionBank
import com.example.data.seed.TwkQuestionBank
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun testPassingGradeThresholds() {
        assertEquals(65, ExamCategory.TWK.passingGrade)
        assertEquals(80, ExamCategory.TIU.passingGrade)
        assertEquals(166, ExamCategory.TKP.passingGrade)
        assertEquals(150, ExamCategory.TWK.maxScore)
        assertEquals(175, ExamCategory.TIU.maxScore)
        assertEquals(225, ExamCategory.TKP.maxScore)
        assertEquals(550, ExamCategory.TWK.maxScore + ExamCategory.TIU.maxScore + ExamCategory.TKP.maxScore)
    }

    @Test
    fun testFullSkdPackageQuestionCounts() {
        val questions = QuestionBankData.getQuestionsForPackage("pkg_full_skd_1")
        assertEquals(110, questions.size)

        val twkQuestions = questions.filter { it.category == ExamCategory.TWK }
        val tiuQuestions = questions.filter { it.category == ExamCategory.TIU }
        val tkpQuestions = questions.filter { it.category == ExamCategory.TKP }

        assertEquals(30, twkQuestions.size)
        assertEquals(35, tiuQuestions.size)
        assertEquals(45, tkpQuestions.size)
    }

    @Test
    fun testRandomizedSkdPackageGenerates110Questions() {
        val randomQuestions1 = QuestionBankData.getQuestionsForPackage("pkg_random_skd")
        assertEquals(110, randomQuestions1.size)
        assertEquals(30, randomQuestions1.filter { it.category == ExamCategory.TWK }.size)
        assertEquals(35, randomQuestions1.filter { it.category == ExamCategory.TIU }.size)
        assertEquals(45, randomQuestions1.filter { it.category == ExamCategory.TKP }.size)

        // Verify sequential numbering 1 to 110
        assertEquals(1, randomQuestions1.first().number)
        assertEquals(110, randomQuestions1.last().number)
    }

    @Test
    fun testComprehensiveQuestionBankPools() {
        // Verify comprehensive coverage of categories and topics
        assertTrue(TwkQuestionBank.questions.isNotEmpty())
        assertTrue(TiuQuestionBank.questions.isNotEmpty())
        assertTrue(TkpQuestionBank.questions.isNotEmpty())

        val twkTopics = TwkQuestionBank.questions.map { it.subCategory }.toSet()
        assertTrue(twkTopics.any { it.contains("Pancasila") })
        assertTrue(twkTopics.any { it.contains("UUD") })
        assertTrue(twkTopics.any { it.contains("Integritas") })
        assertTrue(twkTopics.any { it.contains("Bela Negara") })
        assertTrue(twkTopics.any { it.contains("Bahasa Indonesia") })

        val tiuTopics = TiuQuestionBank.questions.map { it.subCategory }.toSet()
        assertTrue(tiuTopics.any { it.contains("Analogi") })
        assertTrue(tiuTopics.any { it.contains("Silogisme") })
        assertTrue(tiuTopics.any { it.contains("Deret") })
        assertTrue(tiuTopics.any { it.contains("Cerita") || it.contains("Kecepatan") })

        val tkpTopics = TkpQuestionBank.questions.map { it.subCategory }.toSet()
        assertTrue(tkpTopics.any { it.contains("Pelayanan") })
        assertTrue(tkpTopics.any { it.contains("Jejaring") })
        assertTrue(tkpTopics.any { it.contains("TIK") })
        assertTrue(tkpTopics.any { it.contains("Anti-Radikalisme") })
    }

    @Test
    fun testTkpGradedScoringOptions() {
        val questions = QuestionBankData.getQuestionsForPackage("pkg_full_skd_1")
        val tkpSample = questions.first { it.category == ExamCategory.TKP }

        val scores = tkpSample.options.map { it.score }.toSet()
        assertEquals(5, scores.size)
        assertTrue(scores.containsAll(listOf(1, 2, 3, 4, 5)))
    }

    @Test
    fun testAdminQuestionBankCrud() {
        val initialCount = QuestionBankData.getAllAdminQuestions().size
        val newQ = Question(
            id = "test_custom_twk_1",
            packageId = "pkg_custom",
            category = ExamCategory.TWK,
            subCategory = "Integritas KPK",
            number = 999,
            questionText = "Nilai integritas yang mencakup Jujur, Tanggung Jawab, dan Disiplin disebut...",
            options = listOf(
                QuestionOption("A", "Nilai Inti", 5),
                QuestionOption("B", "Nilai Sikap", 0),
                QuestionOption("C", "Nilai Kerja", 0),
                QuestionOption("D", "Nilai Etika", 0),
                QuestionOption("E", "Nilai Estetika", 0)
            ),
            explanation = "Jujur, Tanggung jawab, Disiplin (JuTangDis) adalah nilai inti KPK."
        )

        // Add
        QuestionBankData.addQuestion(newQ)
        assertEquals(initialCount + 1, QuestionBankData.getAllAdminQuestions().size)

        // Update
        val updatedQ = newQ.copy(questionText = "Versi Diperbarui Soal Integritas")
        QuestionBankData.updateQuestion(updatedQ)
        val found = QuestionBankData.getAllAdminQuestions().find { it.id == "test_custom_twk_1" }
        assertEquals("Versi Diperbarui Soal Integritas", found?.questionText)

        // Delete
        val deleted = QuestionBankData.deleteQuestion("test_custom_twk_1")
        assertTrue(deleted)
        assertEquals(initialCount, QuestionBankData.getAllAdminQuestions().size)
    }
}
