package com.example.deepsea.service

import com.example.deepsea.dto.*
import com.example.deepsea.entity.UserStats
import com.example.deepsea.entity.UserProgress
import com.example.deepsea.entity.toContentItemDto
import com.example.deepsea.entity.toDto
import com.example.deepsea.repository.*
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime


@Service
class LessonService(
    private val lessonRepository: LessonRepository,
    private val lessonContentRepository: LessonContentRepository,
    private val userProgressRepository: UserProgressRepository,
    private val userStatsRepository: UserStatsRepository,
    private val lessonResultRepository: LessonResultRepository
) {

    fun getLessonsByUnit(unitId: Long): List<LessonDto> {
        return lessonRepository.findByUnitIdOrderByOrderIndex(unitId).map { it.toDto() }
    }

    fun getLessonById(id: Long): LessonDto {
        return lessonRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Lesson not found") }
            .toDto()
    }

    fun getLessonContent(id: Long): LessonContentDto {
        val lesson = lessonRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Lesson not found") }
        val contents = lessonContentRepository.findByLessonIdOrderByOrderIndex(id)

        return LessonContentDto(
            lessonId = lesson.id,
            lessonType = lesson.lessonType,
            content = contents.map { it.toContentItemDto() }
        )
    }

    @Transactional
    fun completeLession(id: Long, userId: Long, completionData: LessonCompletionDto): LessonProgressDto {
        val lesson = lessonRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Lesson not found") }

        // Calculate stars based on performance
        val starsEarned = calculateStars(completionData)

        // Update user progress
        val progress = userProgressRepository.findByUserIdAndLessonId(userId, id) ?: UserProgress(
            userId = userId,
            unitId = lesson.unitId,
            lessonId = id
        )

        progress.completed = true
        progress.starsEarned = starsEarned
        progress.xpEarned = lesson.xpReward
        progress.completionDate = LocalDateTime.now()
        userProgressRepository.save(progress)

        // Update user stats
        updateUserStats(userId, lesson.xpReward, starsEarned)

        // Find next lesson
        val nextLesson = lessonRepository.findFirstByUnitIdAndOrderIndexGreaterThanOrderByOrderIndex(
            lesson.unitId, lesson.orderIndex
        )

        return LessonProgressDto(
            lessonId = id,
            completed = true,
            starsEarned = starsEarned,
            xpEarned = lesson.xpReward,
            nextLessonId = nextLesson?.id
        )
    }

    private fun calculateStars(completionData: LessonCompletionDto): Int {
        // Logic to calculate stars based on performance
        return when {
            completionData.score >= 90 -> 3
            completionData.score >= 70 -> 2
            else -> 1
        }
    }

    private fun updateUserStats(userId: Long, xpEarned: Int, starsEarned: Int) {
        val stats = userStatsRepository.findById(userId).orElse(UserStats(userId = userId))

        stats.totalXp += xpEarned
        stats.totalStars += starsEarned
        stats.lastActivityDate = LocalDate.now()

        // Update streak logic
        if (stats.lastActivityDate != LocalDate.now().minusDays(1) &&
            stats.lastActivityDate != LocalDate.now()) {
            stats.currentStreak = 1
        } else {
            stats.currentStreak++
        }

        userStatsRepository.save(stats)
    }

    fun getLessonResultById(id: Long): LessonResultDto {
        val result = lessonResultRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("LessonResult not found") }
        return result.toDto()
    }

    @Transactional
    fun saveLessonResult(resultDto: LessonResultDto) {
        val userId = 1L

        val stats = userStatsRepository.findById(userId).orElse(UserStats(userId = userId))

        stats.totalXp += resultDto.xp

        userStatsRepository.save(stats)

    }
}

// Custom exception class for resource not found scenarios
class ResourceNotFoundException(message: String) : RuntimeException(message)