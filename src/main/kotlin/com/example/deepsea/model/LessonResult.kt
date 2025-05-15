package com.example.deepsea.model

import com.example.deepsea.dto.LessonResultDto
import jakarta.persistence.*

@Entity
@Table(name = "lesson_results")
data class LessonResult(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val lessonId: Long = 0,
    val userId: Long = 0,
    val xp: Int = 0,
    val accuracy: Double = 0.0,
    val timeSpent: Int = 0 // thời gian tính theo giây
)

// Extension function chuyển LessonResult -> LessonResultDto
fun LessonResult.toDto(): LessonResultDto {
    val minutes = timeSpent / 60
    val seconds = timeSpent % 60
    val formattedTime = String.format("%02d:%02d", minutes, seconds)

    return LessonResultDto(
        xp = this.xp,
        time = formattedTime,
        accuracy = this.accuracy.toInt()  // Nếu muốn làm tròn thành Int
    )
}
