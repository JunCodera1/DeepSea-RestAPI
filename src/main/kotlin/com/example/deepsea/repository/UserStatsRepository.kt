package com.example.deepsea.repository

import com.example.deepsea.entity.UserStats
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface UserStatsRepository : JpaRepository<UserStats, Long> {
    @Query("SELECT COUNT(DISTINCT us.userId) FROM UserStats us WHERE us.totalXp > " +
            "(SELECT us2.totalXp FROM UserStats us2 WHERE us2.userId = :userId)")
    fun countUsersWithHigherXp(userId: Long): Int

    fun findByLastActivityDateBetween(startDate: LocalDate, endDate: LocalDate): List<UserStats>

    @Query("SELECT AVG(us.currentStreak) FROM UserStats us")
    fun getAverageCurrentStreak(): Double

    @Query("SELECT us FROM UserStats us ORDER BY us.totalXp DESC LIMIT :limit")
    fun findTopUsersByXp(limit: Int): List<UserStats>

    @Query("SELECT us FROM UserStats us ORDER BY us.currentStreak DESC LIMIT :limit")
    fun findTopUsersByStreak(limit: Int): List<UserStats>
}