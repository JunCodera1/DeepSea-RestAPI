package com.example.deepsea.service

import com.example.deepsea.dto.SectionProgressDto
import com.example.deepsea.dto.UnitProgressDto
import com.example.deepsea.dto.UserProgressDto
import com.example.deepsea.dto.UserStatsDto
import com.example.deepsea.model.UserStats
import com.example.deepsea.repository.*
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class ProgressService(
    private val sectionRepository: SectionRepository,
    private val unitRepository: UnitRepository,
    private val lessonRepository: LessonRepository,
    private val userProgressRepository: UserProgressRepository,
    private val userStatsRepository: UserStatsRepository
) {

    fun getUserProgress(userId: Long): UserProgressDto {
        val userStats = userStatsRepository.findById(userId).orElse(UserStats(userId = userId))
        val sections = sectionRepository.findAllByOrderByOrderIndex()

        val sectionsProgress = sections.map { section ->
            val totalUnits = unitRepository.countBySectionId(section.id)
            val totalStars = unitRepository.getTotalStarsBySectionId(section.id) ?: 0

            val completedUnits = userProgressRepository.countCompletedUnitsByUserIdAndSectionId(userId, section.id)
            val earnedStars = userProgressRepository.sumStarsEarnedByUserIdAndSectionId(userId, section.id) ?: 0

            val isCompleted = completedUnits >= totalUnits && totalUnits > 0

            SectionProgressDto(
                sectionId = section.id,
                title = section.title,
                completedUnits = completedUnits,
                totalUnits = totalUnits,
                earnedStars = earnedStars,
                totalStars = totalStars,
                isCompleted = isCompleted
            )
        }

        return UserProgressDto(
            userId = userId,
            sectionsProgress = sectionsProgress
        )
    }

    fun getUserUnitProgress(userId: Long, unitId: Long): UnitProgressDto {
        val unitService = UnitService(
            unitRepository,
            lessonRepository,
            userProgressRepository,
            userStatsRepository
        )
        return unitService.getUnitProgress(unitId, userId)
    }

    fun getUserStats(userId: Long): UserStatsDto {
        val stats = userStatsRepository.findById(userId).orElse(UserStats(userId = userId))

        // Check if we need to update the streak (user hasn't practiced today)
        if (stats.lastActivityDate != null && stats.lastActivityDate != LocalDate.now()) {
            val daysSinceLastActivity = LocalDate.now().toEpochDay() - stats.lastActivityDate!!.toEpochDay()

            if (daysSinceLastActivity > 1) {
                // Streak broken
                stats.currentStreak = 0
                userStatsRepository.save(stats)
            }
        }

        return UserStatsDto(
            userId = stats.userId,
            totalXp = stats.totalXp,
            totalStars = stats.totalStars,
            currentStreak = stats.currentStreak,
            lastActivityDate = stats.lastActivityDate,
            levelsCompleted = stats.levelsCompleted
        )
    }

    fun getTopUsersByXp(limit: Int = 10): List<UserStatsDto> {
        return userStatsRepository.findTopUsersByXp(limit).map { stats ->
            UserStatsDto(
                userId = stats.userId,
                totalXp = stats.totalXp,
                totalStars = stats.totalStars,
                currentStreak = stats.currentStreak,
                lastActivityDate = stats.lastActivityDate,
                levelsCompleted = stats.levelsCompleted
            )
        }
    }

    fun getTopUsersByStreak(limit: Int = 10): List<UserStatsDto> {
        return userStatsRepository.findTopUsersByStreak(limit).map { stats ->
            UserStatsDto(
                userId = stats.userId,
                totalXp = stats.totalXp,
                totalStars = stats.totalStars,
                currentStreak = stats.currentStreak,
                lastActivityDate = stats.lastActivityDate,
                levelsCompleted = stats.levelsCompleted
            )
        }
    }

    fun getUserRanking(userId: Long): Int {
        // Position in the leaderboard (1-based, not 0-based)
        return userStatsRepository.countUsersWithHigherXp(userId) + 1
    }
}