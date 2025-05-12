package com.example.deepsea.repository

import com.example.deepsea.model.LearningSession
import org.springframework.data.jpa.repository.JpaRepository

interface LearningSessionRepository : JpaRepository<LearningSession, Long> {
    fun findByUserIdAndLessonId(userId: Long, lessonId: Long): List<LearningSession>
}