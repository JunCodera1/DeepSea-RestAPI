package com.example.deepsea.controller

import com.example.deepsea.model.AudioResponse
import com.example.deepsea.model.HearingExercise
import com.example.deepsea.service.AudioService
import com.example.deepsea.service.ExerciseService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.http.HttpHeaders
import org.springframework.core.io.Resource

@RestController
@RequestMapping("/api")
class LanguageController(
    private val exerciseService: ExerciseService,
    private val audioService: AudioService
) {

    @GetMapping("/exercises/hearing")
    fun getHearingExercise(): ResponseEntity<HearingExercise> {
        val exercise = exerciseService.getRandomHearingExercise()
        return ResponseEntity.ok(exercise)
    }

    @GetMapping("/audio/{word}")
    fun getWordAudio(@PathVariable word: String): ResponseEntity<AudioResponse> {
        val audioResponse = audioService.getAudioInfo(word)
        return ResponseEntity.ok(audioResponse)
    }

    @GetMapping("/audio/file/{fileName}")
    fun getAudioFile(@PathVariable fileName: String): ResponseEntity<Resource> {
        val resource = audioService.getAudioFile(fileName)
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"${resource.filename}\"")
            .body(resource)
    }
}