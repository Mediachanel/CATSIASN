package com.example.data.db

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "exam_results")
data class ExamResultEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val packageId: String,
    val packageName: String,
    val packageType: String,
    val timestamp: Long,
    val timeSpentSeconds: Int,
    val totalTimeSeconds: Int,
    val twkScore: Int,
    val tiuScore: Int,
    val tkpScore: Int,
    val totalScore: Int,
    val isPassed: Boolean,
    val twkPassed: Boolean,
    val tiuPassed: Boolean,
    val tkpPassed: Boolean,
    val answeredCount: Int,
    val totalQuestions: Int,
    val answersJson: String
)

@Entity(tableName = "question_bookmarks")
data class BookmarkEntity(
    @PrimaryKey
    val questionId: String,
    val packageId: String,
    val category: String,
    val subCategory: String,
    val questionText: String,
    val savedAt: Long = System.currentTimeMillis()
)

@Dao
interface ExamResultDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResult(result: ExamResultEntity): Long

    @Query("SELECT * FROM exam_results ORDER BY timestamp DESC")
    fun getAllResults(): Flow<List<ExamResultEntity>>

    @Query("SELECT * FROM exam_results WHERE id = :id LIMIT 1")
    suspend fun getResultById(id: Long): ExamResultEntity?

    @Query("SELECT COUNT(*) FROM exam_results")
    fun getTotalExamsCount(): Flow<Int>

    @Query("SELECT AVG(totalScore) FROM exam_results")
    fun getAverageTotalScore(): Flow<Double?>

    @Query("SELECT MAX(totalScore) FROM exam_results")
    fun getHighestTotalScore(): Flow<Int?>

    @Query("DELETE FROM exam_results WHERE id = :id")
    suspend fun deleteResult(id: Long)
}

@Dao
interface BookmarkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(bookmark: BookmarkEntity)

    @Query("DELETE FROM question_bookmarks WHERE questionId = :questionId")
    suspend fun removeBookmark(questionId: String)

    @Query("SELECT EXISTS(SELECT 1 FROM question_bookmarks WHERE questionId = :questionId)")
    fun isBookmarked(questionId: String): Flow<Boolean>

    @Query("SELECT * FROM question_bookmarks ORDER BY savedAt DESC")
    fun getAllBookmarks(): Flow<List<BookmarkEntity>>
}
