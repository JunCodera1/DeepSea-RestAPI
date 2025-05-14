package com.example.deepsea.service

import com.example.deepsea.dto.TranslationExerciseDto
import com.example.deepsea.repository.TranslationExerciseRepository
import org.springframework.stereotype.Service

@Service
class TranslationExerciseService(private val repository: TranslationExerciseRepository) {

    fun getRandomExercise(): TranslationExerciseDto {
        val entity = repository.findRandomExercise()
            ?: throw RuntimeException("No translation exercises available")
        return TranslationExerciseDto(
            id = entity.id,
            sourceText = entity.sourceText,
            targetText = entity.targetText,
            sourceLanguage = entity.sourceLanguage,
            targetLanguage = entity.targetLanguage,
            wordOptions = entity.wordOptions
        )
    }
}