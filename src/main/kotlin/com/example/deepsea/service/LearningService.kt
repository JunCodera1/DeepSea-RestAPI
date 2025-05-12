package com.example.deepsea.service

import com.example.deepsea.model.WordPair
import com.example.deepsea.repository.UnitRepository
import org.springframework.stereotype.Service

@Service
class LearningService(
    private val unitRepository: UnitRepository,
    private val wordPairService: WordPairService
) {
    fun getRandomWordPairsForSectionAndUnit(sectionId: Long, unitId: Long): List<WordPair> {
        // Validate unit belongs to section
        val unit = unitRepository.findById(unitId)
            .orElseThrow { IllegalArgumentException("Unit not found: $unitId") }
        if (unit.sectionId != sectionId) {
            throw IllegalArgumentException("Unit $unitId does not belong to section $sectionId")
        }
        // Get random word pairs for the unit
        return wordPairService.getRandomWordPairs(sectionId, unitId)
    }
}
