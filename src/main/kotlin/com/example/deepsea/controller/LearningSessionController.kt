package com.example.deepsea.controller

import com.example.deepsea.model.LearningSession
import com.example.deepsea.service.LearningSessionService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/sessions")
class LearningSessionController(
    private val service: LearningSessionService
) {
    @PostMapping
    fun saveSession(@RequestBody session: LearningSession): LearningSession {
        return service.saveSession(session)
    }

    @GetMapping("/user/{userId}/lesson/{lessonId}")
    fun getSessions(
        @PathVariable userId: Long,
        @PathVariable lessonId: Long
    ): List<LearningSession> {
        return service.getSessionsByUserAndLesson(userId, lessonId)
    }
}