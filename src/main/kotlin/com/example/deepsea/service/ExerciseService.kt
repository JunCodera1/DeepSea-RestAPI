package com.example.deepsea.service

import com.example.deepsea.model.HearingExercise
import org.springframework.stereotype.Service

@Service
class ExerciseService {

    private val exercises = mutableListOf(
        HearingExercise(
            id = "1",
            audio = "/api/audio/file/kudasai.mp3",
            correctAnswer = "ください",
            options = listOf("ください", "おちゃ", "ごはん", "と")
        ),
        HearingExercise(
            id = "2",
            audio = "/api/audio/file/ocha.mp3",
            correctAnswer = "おちゃ",
            options = listOf("おはよう", "おちゃ", "すみません", "ありがとう")
        ),
        HearingExercise(
            id = "3",
            audio = "/api/audio/file/gohan.mp3",
            correctAnswer = "ごはん",
            options = listOf("ください", "りんご", "ごはん", "すし")
        )
    )

    fun getRandomHearingExercise(): HearingExercise {
        return exercises.random()
    }
}