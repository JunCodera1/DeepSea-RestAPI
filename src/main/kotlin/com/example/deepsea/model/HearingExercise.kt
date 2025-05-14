package com.example.deepsea.model

import com.example.deepsea.dto.HearingExerciseDto
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "hearing_exercises")
data class HearingExercise(
    @Id
    val id: String = "",

    @Column(name = "unit_id")
    val unitId: Long = 0L,

    @Column(name = "correct_answer")
    val correctAnswer: String = "",

    val options: String = "" // Comma-separated options
) {
    fun toDto(): HearingExerciseDto {
        return HearingExerciseDto(
            id = id,
            correctAnswer = correctAnswer,
            options = options.split(",").map { it.trim() }
        )
    }
}
