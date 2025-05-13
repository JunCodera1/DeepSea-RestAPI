package com.example.deepsea.service

import com.example.deepsea.model.WordPair
import com.example.deepsea.repository.WordPairRepository
import org.springframework.stereotype.Service

@Service
class WordPairService(
    private val wordPairRepository: WordPairRepository
) {
    fun getRandomWordPairs(sectionId: Long, unitId: Long, count: Int = 5): List<WordPair> {
        val unitPairs = wordPairRepository.findByUnitId(unitId)
        if (unitPairs.isEmpty()) {
            throw IllegalStateException("No word pairs found for unit $unitId")
        }
        return unitPairs.shuffled().take(count.coerceAtMost(unitPairs.size))
    }
}