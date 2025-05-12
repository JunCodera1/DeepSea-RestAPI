package com.example.deepsea.model

import jakarta.persistence.*

@Entity
@Table(name = "learning_sessions")
data class LearningSession(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "lesson_id")
    val lessonId: Long,

    @Column(name = "user_id")
    val userId: Long,

    @Column(name = "total_screens")
    val totalScreens: Int,

    @Column(name = "completed_screens")
    val completedScreens: Int,

    @Column(name = "screens_completed")
    val screensCompleted: List<String>,

    @Column(name = "created_at")
    val createdAt: java.time.LocalDateTime = java.time.LocalDateTime.now()
)