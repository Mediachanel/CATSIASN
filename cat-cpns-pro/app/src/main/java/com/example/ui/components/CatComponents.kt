package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.QueryBuilder
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ExamCategory
import com.example.data.model.ExamRealtimeStats
import com.example.ui.theme.CatActiveBlue
import com.example.ui.theme.CatActiveBlueBg
import com.example.ui.theme.CatAnsweredGreen
import com.example.ui.theme.CatAnsweredGreenBg
import com.example.ui.theme.CatDoubtfulAmber
import com.example.ui.theme.CatDoubtfulAmberBg
import com.example.ui.theme.CatUnansweredGray
import com.example.ui.theme.CatUnansweredGrayBg
import com.example.ui.theme.CrimsonRed
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.GoldSecondary
import com.example.ui.theme.NavyPrimary

@Composable
fun CatTimerDisplay(
    remainingSeconds: Int,
    modifier: Modifier = Modifier
) {
    val hours = remainingSeconds / 3600
    val minutes = (remainingSeconds % 3600) / 60
    val seconds = remainingSeconds % 60

    val isUrgent = remainingSeconds <= 300 // 5 minutes left
    val isWarning = remainingSeconds in 301..900 // 15 minutes left

    val timerColor by animateColorAsState(
        targetValue = when {
            isUrgent -> CrimsonRed
            isWarning -> GoldSecondary
            else -> NavyPrimary
        },
        label = "timerColor"
    )

    val timeString = if (hours > 0) {
        String.format("%02d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format("%02d:%02d", minutes, seconds)
    }

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = timerColor.copy(alpha = 0.12f),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, timerColor),
        modifier = modifier.testTag("cat_timer_box")
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Icon(
                imageVector = Icons.Default.QueryBuilder,
                contentDescription = "Sisa Waktu",
                tint = timerColor,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = timeString,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 1.sp
                ),
                color = timerColor
            )
        }
    }
}

@Composable
fun RealtimeScoreTickerBar(
    stats: ExamRealtimeStats,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF0F172A)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("realtime_score_ticker")
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggleExpand() }
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(if (stats.allPassed) EmeraldSuccess else GoldSecondary)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "LIVE SCORE CAT BKN",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.5.sp
                        ),
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = if (stats.allPassed) EmeraldSuccess else CrimsonRed
                    ) {
                        Text(
                            text = if (stats.allPassed) "LULUS PG" else "BELUM LULUS PG",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Total: ${stats.totalScore}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace
                        ),
                        color = Color(0xFF38BDF8)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isExpanded) "▲" else "▼",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ScoreCategoryItem(
                            code = "TWK",
                            score = stats.twkScore,
                            passingGrade = 65,
                            isPassed = stats.twkPassed
                        )
                        ScoreCategoryItem(
                            code = "TIU",
                            score = stats.tiuScore,
                            passingGrade = 80,
                            isPassed = stats.tiuPassed
                        )
                        ScoreCategoryItem(
                            code = "TKP",
                            score = stats.tkpScore,
                            passingGrade = 166,
                            isPassed = stats.tkpPassed
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Terjawab: ${stats.answeredCount}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF86EFAC)
                        )
                        Text(
                            text = "Ragu-ragu: ${stats.doubtfulCount}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFFDE047)
                        )
                        Text(
                            text = "Kosong: ${stats.unansweredCount}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFCBD5E1)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ScoreCategoryItem(
    code: String,
    score: Int,
    passingGrade: Int,
    isPassed: Boolean
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = code,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
            color = Color(0xFF94A3B8)
        )
        Text(
            text = "$score / $passingGrade",
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            ),
            color = if (isPassed) Color(0xFF4ADE80) else Color(0xFFF87171)
        )
        Text(
            text = if (isPassed) "Lulus" else "Kurang ${passingGrade - score}",
            style = MaterialTheme.typography.labelSmall,
            color = if (isPassed) Color(0xFF4ADE80) else Color(0xFFFDA4AF),
            fontSize = 10.sp
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun QuestionNumberGrid(
    totalQuestions: Int,
    currentIndex: Int,
    answeredIndices: Set<Int>,
    doubtfulIndices: Set<Int>,
    onSelectIndex: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        for (i in 0 until totalQuestions) {
            val isCurrent = i == currentIndex
            val isDoubtful = doubtfulIndices.contains(i)
            val isAnswered = answeredIndices.contains(i)

            val backgroundColor = when {
                isCurrent -> CatActiveBlueBg
                isDoubtful -> CatDoubtfulAmberBg
                isAnswered -> CatAnsweredGreenBg
                else -> CatUnansweredGrayBg
            }

            val textColor = when {
                isCurrent -> CatActiveBlue
                isDoubtful -> CatDoubtfulAmber
                isAnswered -> CatAnsweredGreen
                else -> CatUnansweredGray
            }

            val borderColor = when {
                isCurrent -> CatActiveBlue
                isDoubtful -> CatDoubtfulAmber
                isAnswered -> CatAnsweredGreen
                else -> Color.Transparent
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(backgroundColor)
                    .border(
                        width = if (isCurrent) 2.dp else 1.dp,
                        color = borderColor,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable { onSelectIndex(i) }
                    .testTag("question_num_btn_${i + 1}")
            ) {
                Text(
                    text = "${i + 1}",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = if (isCurrent) FontWeight.Black else FontWeight.Bold
                    ),
                    color = textColor
                )
            }
        }
    }
}
