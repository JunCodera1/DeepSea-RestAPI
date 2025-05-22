package com.example.deepsea.controller

import com.example.deepsea.entity.QuizQuestion
import com.example.deepsea.service.VocabularyService


import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class VocabularyController(
    private val vocabularyService: VocabularyService
) {
    /**
     * Get questions for a specific lesson
     */
    @GetMapping("/lessons/{lessonId}/questions")
    fun getLessonQuestions(
        @PathVariable lessonId: Long,
        @RequestParam type: String? = null
    ): ResponseEntity<List<QuizQuestion>> {
        val questions = vocabularyService.getLessonQuestions(lessonId, type)
        return ResponseEntity.ok(questions)
    }

    @GetMapping("/questions/{questionId}")
    fun getQuestionById(@PathVariable questionId: Long): ResponseEntity<QuizQuestion> {
        val question = vocabularyService.getQuestionById(questionId)
        return if (question != null) {
            ResponseEntity.ok(question)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/questions/random")
    fun getRandomQuestion(): ResponseEntity<QuizQuestion> {
        val question = vocabularyService.getRandomQuestion()
        return ResponseEntity.ok(question)
    }


    /**
     * Get a specific vocabulary item by ID
     */
    @GetMapping("/vocabulary/{wordId}")
    fun getVocabularyItem(@PathVariable wordId: Long): ResponseEntity<QuizQuestion> {
        val vocabularyItem = vocabularyService.getVocabularyItem(wordId)
        return if (vocabularyItem != null) {
            ResponseEntity.ok(vocabularyItem)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    /**
     * Get a random vocabulary item
     */
    @GetMapping("/vocabulary/random")
    fun getRandomVocabularyItem(): ResponseEntity<QuizQuestion> {
        val vocabularyItem = vocabularyService.getRandomVocabularyItem()
        return ResponseEntity.ok(vocabularyItem)
    }

    /**
     * Get vocabulary options for quizzes
     */
    @GetMapping("/vocabulary/options")
    fun getVocabularyOptions(
        @RequestParam size: Int = 4
    ): ResponseEntity<List<QuizQuestion>> {
        val options = vocabularyService.getVocabularyOptions(size)
        return ResponseEntity.ok(options)
    }


}
