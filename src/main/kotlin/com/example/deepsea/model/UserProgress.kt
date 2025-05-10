package com.example.deepsea.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "user_progress")
data class UserProgress(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "user_id")
    val userId: Long,

    @Column(name = "unit_id")
    val unitId: Long,

    @Column(name = "lesson_id")
    val lessonId: Long,

    var completed: Boolean = false,

    @Column(name = "stars_earned")
    var starsEarned: Int = 0,

    @Column(name = "xp_earned")
    var xpEarned: Int = 0,

    @Column(name = "completion_date")
    var completionDate: LocalDateTime? = null
)