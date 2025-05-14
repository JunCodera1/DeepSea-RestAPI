package com.example.deepsea.service

import com.example.deepsea.config.DataInitializer
import com.example.deepsea.domain.enums.QuestionType
import com.example.deepsea.model.LanguageContent
import com.example.deepsea.model.QuizQuestion
import com.example.deepsea.model.VocabularyOption
import com.example.deepsea.repository.VocabularyRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class VocabularyService(
    private val vocabularyRepository: VocabularyRepository
) {
    /**
     * Get questions for a specific lesson
     */
    fun getLessonQuestions(lessonId: Long, type: String? = null): List<QuizQuestion> {
        val questionType = type?.let { QuestionType.valueOf(it) }
        // Sử dụng questionType.name để lấy tên của enum dưới dạng String
        return vocabularyRepository.findByLessonId(lessonId, questionType?.name)
    }

    /**
     * Get a specific vocabulary item by ID
     */
    fun getVocabularyItem(wordId: Long): QuizQuestion? {
        return vocabularyRepository.findById(wordId).orElse(null)
    }

    /**
     * Get the next vocabulary item after the specified ID
     */
    fun getNextVocabularyItem(wordId: Long): QuizQuestion {
        // Find the current word's lessonId
        val currentWord = getVocabularyItem(wordId)
        val lessonId = currentWord?.lessonId ?: 1L

        // Find all words in the same lesson
        val lessonWords = vocabularyRepository.findByLessonId(lessonId, null)

        // If we have lesson words
        if (lessonWords.isNotEmpty()) {
            // Find the index of the current word
            val currentIndex = lessonWords.indexOfFirst { it.id == wordId }

            // If found and not the last word
            if (currentIndex != -1 && currentIndex < lessonWords.size - 1) {
                return lessonWords[currentIndex + 1]
            }
        }

        // Default to returning a random word if we can't find a "next" word
        return getRandomVocabularyItem()
    }

    /**
     * Get a random vocabulary item
     */
    fun getRandomVocabularyItem(): QuizQuestion {
        return vocabularyRepository.findRandom() ?: createFallbackVocabularyItem()
    }

    /**
     * Get vocabulary options for quizzes
     */
    fun getVocabularyOptions(size: Int): List<QuizQuestion> {
        return vocabularyRepository.findRandom(size)
    }

    /**
     * Create a fallback vocabulary item in case DB returns null
     */
    private fun createFallbackVocabularyItem(): QuizQuestion {
        val option1 = VocabularyOption(
            id = 1L,
            image = DataInitializer.ImageResources.BREAD,
            languageContent = mapOf(
                "en" to LanguageContent("bread", ""),
                "ja" to LanguageContent("パン", "pan")
            )
        )

        val option2 = VocabularyOption(
            id = 2L,
            image = DataInitializer.ImageResources.DOG,
            languageContent = mapOf(
                "en" to LanguageContent("dog", ""),
                "ja" to LanguageContent("いぬ", "inu")
            )
        )

        val option3 = VocabularyOption(
            id = 3L,
            image = DataInitializer.ImageResources.BOOK,
            languageContent = mapOf(
                "en" to LanguageContent("book", ""),
                "ja" to LanguageContent("ほん", "hon")
            )
        )

        val option4 = VocabularyOption(
            id = 4L,
            image = DataInitializer.ImageResources.SCHOOL,
            languageContent = mapOf(
                "en" to LanguageContent("school", ""),
                "ja" to LanguageContent("がっこう", "gakkou")
            )
        )

        return QuizQuestion(
            id = 1L,
            type = QuestionType.IMAGE_SELECTION.name,
            prompt = "Choose the correct translation for 'bread'",
            languageContent = mapOf(
                "en" to LanguageContent("Choose the correct image for bread", ""),
                "ja" to LanguageContent("breadの正しい画像を選んでください", "")
            ),
            options = listOf(option1, option2, option3, option4)
        )
    }
}