package com.example.deepsea.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "user_progress")
class UserProgress() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    @Column(name = "user_id")
    var userId: Long = 0

    @Column(name = "unit_id")
    var unitId: Long = 0

    @Column(name = "lesson_id")
    var lessonId: Long = 0

    var completed: Boolean = false

    @Column(name = "stars_earned")
    var starsEarned: Int = 0

    @Column(name = "xp_earned")
    var xpEarned: Int = 0

    @Column(name = "completion_date")
    var completionDate: LocalDateTime? = null

    // Secondary constructor để khởi tạo nhanh
    constructor(
        userId: Long,
        unitId: Long,
        lessonId: Long
    ) : this() {
        this.userId = userId
        this.unitId = unitId
        this.lessonId = lessonId
    }
}
