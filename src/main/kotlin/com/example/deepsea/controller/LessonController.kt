package com.example.deepsea.controller

import com.example.deepsea.dto.*
import com.example.deepsea.service.LessonService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v2/lessons")
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

    @PostMapping("/results")
    fun saveLessonResult(@RequestBody resultDto: LessonResultDto): ResponseEntity<Unit> {
        lessonService.saveLessonResult(resultDto)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/results/{id}")
    fun getLessonResultById(@PathVariable id: Long): ResponseEntity<LessonResultDto> {
        val lessonResult = lessonService.getLessonResultById(id)
        return ResponseEntity.ok(lessonResult)
    }

}