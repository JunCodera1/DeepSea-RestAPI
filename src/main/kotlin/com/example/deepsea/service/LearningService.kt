package com.example.deepsea.service

import com.example.deepsea.dto.HearingExerciseDto
import com.example.deepsea.entity.WordPair
import com.example.deepsea.repository.HearingExerciseRepository
import com.example.deepsea.repository.UnitRepository
import org.springframework.stereotype.Service

@Service
class LearningService(
    private val unitRepository: UnitRepository,
    private val wordPairService: WordPairService,
    private val hearingExerciseRepository: HearingExerciseRepository

) {
    fun getRandomWordPairsForSectionAndUnit(sectionId: Long, unitId: Long): List<WordPair> {
        val unit = unitRepository.findById(unitId)
            .orElseThrow { IllegalArgumentException("Unit not found: $unitId") }
        if (unit.sectionId != sectionId) {
            throw IllegalArgumentException("Unit $unitId does not belong to section $sectionId")
        }
        return wordPairService.getRandomWordPairs(sectionId, unitId)
    }
    fun getRandomExercise(sectionId: Long, unitId: Long): HearingExerciseDto? {
        // Validate section and unit (simplified; in practice, check against database)
        if (sectionId !in 1L..6L) {
            throw IllegalArgumentException("Invalid section ID")
        }
        val validUnitIds = getValidUnitIdsForSection(sectionId)
        if (unitId !in validUnitIds) {
            throw IllegalArgumentException("Invalid unit ID for section $sectionId")
        }

        return hearingExerciseRepository.findRandomByUnitId(unitId)?.toDto()
    }

    private fun getValidUnitIdsForSection(sectionId: Long): List<Long> {
        // Based on the provided data, each section (1-6) has 5 units
        // This is a simplified mapping based on the provided database dump
        return when (sectionId) {
            1L -> listOf(1L, 2L, 3L, 4L, 5L)
            2L -> listOf(6L, 7L, 8L, 9L, 10L)
            3L -> listOf(11L, 12L, 13L, 14L, 15L)
            4L -> listOf(16L, 17L, 18L, 19L, 20L)
            5L -> listOf(21L, 22L, 23L, 24L, 25L)
            6L -> listOf(26L, 27L, 28L, 29L, 30L)
            else -> emptyList()
        }
    }
}
