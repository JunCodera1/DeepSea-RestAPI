package com.example.deepsea.service

import com.example.deepsea.entity.LearningSession
import com.example.deepsea.repository.LearningSessionRepository
import org.springframework.stereotype.Service

@Service
class LearningSessionService(
    private val repository: LearningSessionRepository
) {
    fun saveSession(session: LearningSession): LearningSession {
        return repository.save(session)
    }

    fun getSessionsByUserAndLesson(userId: Long, lessonId: Long): List<LearningSession> {
        return repository.findByUserIdAndLessonId(userId, lessonId)
    }
}