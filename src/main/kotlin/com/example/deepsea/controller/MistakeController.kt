package com.example.deepsea.controller

import com.example.deepsea.dto.MistakeRequest
import com.example.deepsea.dto.MistakeResponse
import com.example.deepsea.service.MistakeService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/mistakes")
class MistakeController(private val mistakeService: MistakeService) {

    @PostMapping
    fun createMistake(@RequestBody mistakeRequest: MistakeRequest): ResponseEntity<MistakeResponse> {
        val response = mistakeService.saveMistake(mistakeRequest)
        return ResponseEntity(response, HttpStatus.CREATED)
    }

    @GetMapping("/user/{userId}")
    fun getMistakesByUser(@PathVariable userId: Long): ResponseEntity<List<MistakeResponse>> {
        val responses = mistakeService.getMistakesByUserId(userId)
        return ResponseEntity(responses, HttpStatus.OK)
    }

    @PutMapping("/{mistakeId}/review")
    fun markAsReviewed(@PathVariable mistakeId: Long): ResponseEntity<MistakeResponse> {
        val response = mistakeService.markAsReviewed(mistakeId)
        return ResponseEntity(response, HttpStatus.OK)
    }

    @DeleteMapping("/{mistakeId}")
    fun deleteMistake(@PathVariable mistakeId: Long): ResponseEntity<Void> {
        mistakeService.deleteMistake(mistakeId)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}