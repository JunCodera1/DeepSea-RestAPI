package com.example.deepsea.config

import com.example.deepsea.model.LanguageContent
import com.example.deepsea.model.QuestionType
import com.example.deepsea.model.QuizQuestion
import com.example.deepsea.model.VocabularyOption
import com.example.deepsea.repository.VocabularyRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DataInitializer {

    // Define resource IDs for images
    // In a real application, these would be your actual R.drawable resource IDs
    object ImageResources {
        const val RICE = 1001
        const val SUSHI = 1002
        const val WATER = 1003
        const val GREEN_TEA = 1004
    }

    @Bean
    fun initData(repository: VocabularyRepository): CommandLineRunner {
        return CommandLineRunner {
            // Check if we already have data
            if (repository.count() == 0L) {
                // Japanese vocabulary as QuizQuestion objects
                val japaneseVocabulary = listOf(
                    createQuizQuestion(
                        nativeWord = "ごはん",
                        romaji = "gohan",
                        english = "rice",
                        imageResourceId = ImageResources.RICE,
                        lessonId = 1L
                    ),
                    createQuizQuestion(
                        nativeWord = "すし",
                        romaji = "sushi",
                        english = "sushi",
                        imageResourceId = ImageResources.SUSHI,
                        lessonId = 1L
                    ),
                    createQuizQuestion(
                        nativeWord = "みず",
                        romaji = "mizu",
                        english = "water",
                        imageResourceId = ImageResources.WATER,
                        lessonId = 1L
                    ),
                    createQuizQuestion(
                        nativeWord = "おちゃ",
                        romaji = "ocha",
                        english = "green tea",
                        imageResourceId = ImageResources.GREEN_TEA,
                        lessonId = 1L
                    ),
                    // Add more vocabulary items as needed
                )

                repository.saveAll(japaneseVocabulary)
            }
        }
    }

    /**
     * Helper function to create a QuizQuestion from vocabulary data
     */
    private fun createQuizQuestion(
        nativeWord: String,
        romaji: String,
        english: String,
        imageResourceId: Int,
        lessonId: Long,
        difficulty: Int = 1
    ): QuizQuestion {
        // Create the correct option
        val correctOption = VocabularyOption(
            image = imageResourceId,
            languageContent = mapOf(
                "en" to LanguageContent(english, ""),
                "ja" to LanguageContent(nativeWord, romaji)
            )
        )

        // For a real implementation, you would create distractors (wrong answers)
        // Here we're just using the correct option for simplicity
        val options = listOf(correctOption)

        return QuizQuestion(
            type = QuestionType.IMAGE_SELECTION.name,
            prompt = "Choose the correct translation for '$english'",
            languageContent = mapOf(
                "en" to LanguageContent("Choose the correct image for $english", ""),
                "ja" to LanguageContent("${english}の正しい画像を選んでください", "")
            ),
            options = options,
            correctAnswerId = correctOption.id,
            lessonId = lessonId
        )
    }
}