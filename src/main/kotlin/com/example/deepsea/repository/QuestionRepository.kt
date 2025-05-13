package com.example.deepsea.repository

import com.example.deepsea.model.Question
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface QuestionRepository : JpaRepository<Question, Long> {
    @Query("SELECT q FROM Question q WHERE q.gameMode = :gameMode AND q.language = :language ORDER BY RANDOM() LIMIT :count")
    fun findRandomQuestionsByModeAndLanguage(gameMode: String, language: String, count: Int): List<Question>
}