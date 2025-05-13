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
            image = DataInitializer.ImageResources.RICE,
            languageContent = mapOf(
                "en" to LanguageContent("water", ""),
                "ja" to LanguageContent("水", "mizu")
            )
        )

        val option2 = VocabularyOption(
            id = 2L,
            image = DataInitializer.ImageResources.RICE,
            languageContent = mapOf(
                "en" to LanguageContent("fire", ""),
                "ja" to LanguageContent("火", "hi")
            )
        )

        val option3 = VocabularyOption(
            id = 3L,
            image = DataInitializer.ImageResources.RICE,
            languageContent = mapOf(
                "en" to LanguageContent("earth", ""),
                "ja" to LanguageContent("地球", "chikyuu")
            )
        )

        val option4 = VocabularyOption(
            id = 4L,
            image = DataInitializer.ImageResources.RICE,
            languageContent = mapOf(
                "en" to LanguageContent("wind", ""),
                "ja" to LanguageContent("風", "kaze")
            )
        )

        return QuizQuestion(
            id = 1L,
            type = QuestionType.IMAGE_SELECTION.name,
            prompt = "Choose the correct image for 水",
            languageContent = mapOf(
                "en" to LanguageContent("Choose the correct image for water", ""),
                "ja" to LanguageContent("水の正しい画像を選んでください", "mizu no tadashii gazou wo erande kudasai")
            ),
            options = listOf(option1, option2, option3, option4),
        )
    }
}