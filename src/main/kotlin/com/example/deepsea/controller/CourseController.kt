package com.example.deepsea.controller

import com.example.deepsea.dto.PathRequest
import com.example.deepsea.dto.UserCourseDto
import com.example.deepsea.service.CourseService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/courses")
class CourseController(
    private val courseService: CourseService
) {
    @PostMapping("/path")
    fun savePath(@RequestBody request: PathRequest): ResponseEntity<Unit> {
        return try {
            courseService.saveOrUpdatePath(
                userId = request.userId,
                course = request.language,
                path = request.path
            )
            ResponseEntity.ok().build()
        } catch (e: Exception) {
            ResponseEntity.badRequest().build()
        }
    }

    @GetMapping("/{userId}")
    fun getCourses(@PathVariable userId: Long): ResponseEntity<List<UserCourseDto>> {
        return ResponseEntity.ok(courseService.getCoursesWithPaths(userId))
    }
}