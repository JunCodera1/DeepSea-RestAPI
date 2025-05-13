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
    val id: String,
    @Column(name = "unit_id")
    val unitId: Long,
    @Column(name = "audio_url")
    val audioUrl: String,
    @Column(name = "correct_answer")
    val correctAnswer: String,
    val options: String // Assuming options are stored as a comma-separated string
) {
    fun toDto(): HearingExerciseDto {
        return HearingExerciseDto(
            id = id,
            audio = audioUrl,
            correctAnswer = correctAnswer,
            options = options.split(",").map { it.trim() }
        )
    }
}