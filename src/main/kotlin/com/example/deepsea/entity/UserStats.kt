package com.example.deepsea.entity

import jakarta.persistence.*
import java.time.LocalDate


@Entity
@Table(name = "user_stats")
data class UserStats(
    @Id
    @Column(name = "user_id")
    val userId: Long=0L,

    @Column(name = "total_xp")
    var totalXp: Int = 0,

    @Column(name = "total_stars")
    var totalStars: Int = 0,

    @Column(name = "current_streak")
    var currentStreak: Int = 0,

    @Column(name = "last_activity_date")
    var lastActivityDate: LocalDate? = null,

    @Column(name = "levels_completed")
    var levelsCompleted: Int = 0


)