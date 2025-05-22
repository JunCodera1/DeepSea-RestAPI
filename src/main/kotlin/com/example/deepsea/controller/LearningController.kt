package com.example.deepsea.controller

import com.example.deepsea.dto.HearingExerciseDto
import com.example.deepsea.entity.WordPair
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

    @GetMapping("/sections/{sectionId}/units/{unitId}/exercise")
    fun getRandomExercise(
        @PathVariable sectionId: Long,
        @PathVariable unitId: Long
    ): HearingExerciseDto? {
        return learningService.getRandomExercise(sectionId, unitId)
    }
}