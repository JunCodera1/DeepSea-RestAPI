package com.example.deepsea.controller

import com.example.deepsea.dto.TranslationExerciseDto
import com.example.deepsea.service.TranslationExerciseService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/translation-exercises")
class TranslationExerciseController(private val service: TranslationExerciseService) {

    @GetMapping("/random")
    fun getRandomExercise(): TranslationExerciseDto {
        return service.getRandomExercise()
    }
}