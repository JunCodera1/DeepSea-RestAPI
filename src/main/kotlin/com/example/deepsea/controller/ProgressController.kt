package com.example.deepsea.controller

import com.example.deepsea.dto.UnitProgressDto
import com.example.deepsea.dto.UserProgressDto
import com.example.deepsea.dto.UserStatsDto
import com.example.deepsea.service.ProgressService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/progress")
class ProgressController(private val progressService: ProgressService) {

    @GetMapping("/user/{userId}")
    fun getUserProgress(@PathVariable userId: Long): ResponseEntity<UserProgressDto> {
        return ResponseEntity.ok(progressService.getUserProgress(userId))
    }

    @GetMapping("/user/{userId}/unit/{unitId}")
    fun getUserUnitProgress(@PathVariable userId: Long, @PathVariable unitId: Long): ResponseEntity<UnitProgressDto> {
        return ResponseEntity.ok(progressService.getUserUnitProgress(userId, unitId))
    }

    @GetMapping("/user/{userId}/stats")
    fun getUserStats(@PathVariable userId: Long): ResponseEntity<UserStatsDto> {
        return ResponseEntity.ok(progressService.getUserStats(userId))
    }
}