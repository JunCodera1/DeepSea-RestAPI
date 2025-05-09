package com.example.deepsea.repository

import com.example.deepsea.model.QuestionType
import com.example.deepsea.model.QuizQuestion
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface VocabularyRepository : JpaRepository<QuizQuestion, Long> {

    /**
     * Find questions by lesson ID and optionally filter by question type
     */
    @Query("""
    SELECT q FROM QuizQuestion q 
    WHERE q.lessonId = :lessonId 
    AND (:type IS NULL OR q.type = :type)
""")
    fun findByLessonId(
        @Param("lessonId") lessonId: Long,
        @Param("type") type: String?
    ): List<QuizQuestion>

    /**
     * Find a random vocabulary item
     */
    @Query(value = "SELECT * FROM quiz_question ORDER BY RAND() LIMIT 1", nativeQuery = true)
    fun findRandom(): QuizQuestion?

    /**
     * Find multiple random vocabulary items for options
     */
    @Query(value = "SELECT * FROM quiz_question ORDER BY RAND() LIMIT :limit", nativeQuery = true)
    fun findRandom(@Param("limit") limit: Int): List<QuizQuestion>
}
