package com.example.deepsea.controller

import com.example.deepsea.model.WordPair
import com.example.deepsea.service.LearningService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class LearningController(
    private val learningService: LearningService
) {
    @GetMapping("/api/learn/sections/{sectionId}/units/{unitId}/matching-pairs")
    fun getMatchingPairs(
        @PathVariable sectionId: Long,
        @PathVariable unitId: Long
    ): List<WordPair> {
        return learningService.getRandomWordPairsForSectionAndUnit(sectionId, unitId)
    }
}