package com.example.deepsea.config

import com.example.deepsea.domain.enums.QuestionType
import com.example.deepsea.model.LanguageContent
import com.example.deepsea.model.QuizQuestion
import com.example.deepsea.model.VocabularyOption
import com.example.deepsea.repository.VocabularyRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DataInitializer {

    // Define resource IDs for images
    object ImageResources {
        // Food & Drinks (1000s)
        const val BREAD = 1005
        const val MEAT = 1006
        const val TEA = 1012
        // Animals (2000s)
        const val DOG = 2001
        const val CAT = 2002
        // Objects (3000s)
        const val BOOK = 3001
        const val PEN = 3002
        // Places (4000s)
        const val SCHOOL = 4001
        const val HOME = 4002
        // Transport (5000s)
        const val CAR = 5001
        const val BUS = 5002
        // Colors (6000s)
        const val RED = 6001
        const val BLUE = 6002
        // Clothes (7000s)
        const val SHIRT = 7001
        const val PANTS = 7002
        // Emotions (8000s)
        const val HAPPY = 8001
        const val SAD = 8002
        // Weather (9000s)
        const val SUNNY = 9001
        const val RAINY = 9002
        // Verbs (10000s)
        const val EAT = 10002
    }

    @Bean
    fun initData(repository: VocabularyRepository): CommandLineRunner {
        return CommandLineRunner {
            // Check if we already have data
            if (repository.count() == 0L) {
                // Vocabulary as QuizQuestion objects
                val vocabulary = listOf(
                    createQuizQuestion(
                        nativeWord = "パン",
                        romaji = "pan",
                        english = "bread",
                        imageResourceId = ImageResources.BREAD,
                        lessonId = 1L
                    ),
                    createQuizQuestion(
                        nativeWord = "にく",
                        romaji = "niku",
                        english = "meat",
                        imageResourceId = ImageResources.MEAT,
                        lessonId = 1L
                    ),
                    createQuizQuestion(
                        nativeWord = "おちゃ",
                        romaji = "ocha",
                        english = "tea",
                        imageResourceId = ImageResources.TEA,
                        lessonId = 1L
                    ),
                    createQuizQuestion(
                        nativeWord = "いぬ",
                        romaji = "inu",
                        english = "dog",
                        imageResourceId = ImageResources.DOG,
                        lessonId = 1L
                    ),
                    createQuizQuestion(
                        nativeWord = "ねこ",
                        romaji = "neko",
                        english = "cat",
                        imageResourceId = ImageResources.CAT,
                        lessonId = 1L
                    ),
                    createQuizQuestion(
                        nativeWord = "ほん",
                        romaji = "hon",
                        english = "book",
                        imageResourceId = ImageResources.BOOK,
                        lessonId = 1L
                    ),
                    createQuizQuestion(
                        nativeWord = "ペン",
                        romaji = "pen",
                        english = "pen",
                        imageResourceId = ImageResources.PEN,
                        lessonId = 1L
                    ),
                    createQuizQuestion(
                        nativeWord = "がっこう",
                        romaji = "gakkou",
                        english = "school",
                        imageResourceId = ImageResources.SCHOOL,
                        lessonId = 1L
                    ),
                    createQuizQuestion(
                        nativeWord = "いえ",
                        romaji = "ie",
                        english = "home",
                        imageResourceId = ImageResources.HOME,
                        lessonId = 1L
                    ),
                    createQuizQuestion(
                        nativeWord = "くるま",
                        romaji = "kuruma",
                        english = "car",
                        imageResourceId = ImageResources.CAR,
                        lessonId = 1L
                    ),
                    createQuizQuestion(
                        nativeWord = "バス",
                        romaji = "basu",
                        english = "bus",
                        imageResourceId = ImageResources.BUS,
                        lessonId = 1L
                    ),
                    createQuizQuestion(
                        nativeWord = "あか",
                        romaji = "aka",
                        english = "red",
                        imageResourceId = ImageResources.RED,
                        lessonId = 1L
                    ),
                    createQuizQuestion(
                        nativeWord = "あお",
                        romaji = "ao",
                        english = "blue",
                        imageResourceId = ImageResources.BLUE,
                        lessonId = 1L
                    ),
                    createQuizQuestion(
                        nativeWord = "シャツ",
                        romaji = "shatsu",
                        english = "shirt",
                        imageResourceId = ImageResources.SHIRT,
                        lessonId = 1L
                    ),
                    createQuizQuestion(
                        nativeWord = "ズボン",
                        romaji = "zubon",
                        english = "pants",
                        imageResourceId = ImageResources.PANTS,
                        lessonId = 1L
                    ),
                    createQuizQuestion(
                        nativeWord = "しあわせ",
                        romaji = "shiawase",
                        english = "happy",
                        imageResourceId = ImageResources.HAPPY,
                        lessonId = 1L
                    ),
                    createQuizQuestion(
                        nativeWord = "かなしい",
                        romaji = "kanashii",
                        english = "sad",
                        imageResourceId = ImageResources.SAD,
                        lessonId = 1L
                    ),
                    createQuizQuestion(
                        nativeWord = "はれ",
                        romaji = "hare",
                        english = "sunny",
                        imageResourceId = ImageResources.SUNNY,
                        lessonId = 1L
                    ),
                    createQuizQuestion(
                        nativeWord = "あめ",
                        romaji = "ame",
                        english = "rainy",
                        imageResourceId = ImageResources.RAINY,
                        lessonId = 1L
                    ),
                    createQuizQuestion(
                        nativeWord = "たべる",
                        romaji = "taberu",
                        english = "eat",
                        imageResourceId = ImageResources.EAT,
                        lessonId = 1L
                    )
                )

                repository.saveAll(vocabulary)
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

        // For simplicity, only the correct option is included (no distractors)
        val options = listOf(correctOption)

        return QuizQuestion(
            type = QuestionType.IMAGE_SELECTION.name,
            prompt = "Choose the correct translation for '$english'",
            languageContent = mapOf(
                "en" to LanguageContent("Choose the correct image for $english", ""),
                "ja" to LanguageContent("${english}の正しい画像を選んでください", "")
            ),
            options = options,
            lessonId = lessonId
        )
    }
}