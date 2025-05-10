package com.example.deepsea.controller

import com.example.deepsea.dto.LessonCompletionDto
import com.example.deepsea.dto.LessonContentDto
import com.example.deepsea.dto.LessonDto
import com.example.deepsea.dto.LessonProgressDto
import com.example.deepsea.service.LessonService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/lessons")
class LessonController(private val lessonService: LessonService) {

    @GetMapping("/unit/{unitId}")
    fun getLessonsByUnit(@PathVariable unitId: Long): ResponseEntity<List<LessonDto>> {
        return ResponseEntity.ok(lessonService.getLessonsByUnit(unitId))
    }

    @GetMapping("/{id}")
    fun getLessonById(@PathVariable id: Long): ResponseEntity<LessonDto> {
        return ResponseEntity.ok(lessonService.getLessonById(id))
    }

    @GetMapping("/{id}/content")
    fun getLessonContent(@PathVariable id: Long): ResponseEntity<LessonContentDto> {
        return ResponseEntity.ok(lessonService.getLessonContent(id))
    }

    @PostMapping("/{id}/complete")
    fun completeLession(
        @PathVariable id: Long,
        @RequestParam userId: Long,
        @RequestBody completionData: LessonCompletionDto
    ): ResponseEntity<LessonProgressDto> {
        return ResponseEntity.ok(lessonService.completeLession(id, userId, completionData))
    }
}