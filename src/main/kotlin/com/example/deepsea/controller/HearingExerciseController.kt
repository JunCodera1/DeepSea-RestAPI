package com.example.deepsea.controller

import com.example.deepsea.dto.HearingExerciseDto
import com.example.deepsea.repository.HearingExerciseRepository
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/hearing-exercises")
class HearingExerciseController(
    private val hearingExerciseRepository: HearingExerciseRepository
) {

    @GetMapping("/unit/{unitId}/random")
    fun getRandomHearingExercise(@PathVariable unitId: Long): HearingExerciseDto {
        val exercise = hearingExerciseRepository.findRandomByUnitId(unitId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "No hearing exercise found for unit $unitId")
        return exercise.toDto()
    }
}