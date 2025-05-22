package com.example.deepsea.service

import com.example.deepsea.dto.UnitDto
import com.example.deepsea.dto.UnitProgressDto
import com.example.deepsea.entity.toDto
import com.example.deepsea.repository.LessonRepository
import com.example.deepsea.repository.UnitRepository
import com.example.deepsea.repository.UserProgressRepository
import com.example.deepsea.repository.UserStatsRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class UnitService(
    private val unitRepository: UnitRepository,
    private val lessonRepository: LessonRepository,
    private val userProgressRepository: UserProgressRepository,
    private val userStatsRepository: UserStatsRepository
) {

    fun getUnitsBySection(sectionId: Long): List<UnitDto> {
        return unitRepository.findBySectionIdOrderByOrderIndex(sectionId).map { unit ->
            val lessonCount = lessonRepository.countByUnitId(unit.id)
            unit.toDto(lessonCount = lessonCount)
        }
    }

    fun getUnitById(id: Long): UnitDto {
        val unit = unitRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Unit not found") }
        val lessonCount = lessonRepository.countByUnitId(unit.id)
        return unit.toDto(lessonCount = lessonCount)
    }

    fun getUnitProgress(unitId: Long, userId: Long): UnitProgressDto {
        val unit = unitRepository.findById(unitId)
            .orElseThrow { ResourceNotFoundException("Unit not found") }

        val totalLessons = lessonRepository.countByUnitId(unitId)
        val totalStars = lessonRepository.getTotalStarsByUnitId(unitId) ?: 0

        val completedLessons = userProgressRepository.countCompletedLessonsByUserIdAndUnitId(userId, unitId)
        val earnedStars = userProgressRepository.sumStarsEarnedByUserIdAndUnitId(userId, unitId) ?: 0

        val isCompleted = completedLessons >= totalLessons && totalLessons > 0

        // Find the last completed lesson in this unit
        val userProgress = userProgressRepository.findByUserIdAndUnitId(userId, unitId)
        val lastCompletedLessonId = userProgress
            .filter { it.completed }
            .maxByOrNull { it.completionDate ?: LocalDateTime.MIN }
            ?.lessonId

        return UnitProgressDto(
            unitId = unitId,
            totalLessons = totalLessons,
            completedLessons = completedLessons,
            earnedStars = earnedStars,
            totalStars = totalStars,
            isCompleted = isCompleted,
            lastCompletedLessonId = lastCompletedLessonId
        )
    }

    fun checkUnitUnlockStatus(unitId: Long, userId: Long): Boolean {
        val unit = unitRepository.findById(unitId)
            .orElseThrow { ResourceNotFoundException("Unit not found") }

        // If unit doesn't require stars, it's unlocked
        if (unit.starsRequired <= 0) {
            return true
        }

        // Get user's total stars
        val userStats = userStatsRepository.findById(userId).orElse(null)
        val totalStars = userStats?.totalStars ?: 0

        return totalStars >= unit.starsRequired
    }
}