package com.example.deepsea.controller
import com.example.deepsea.dto.SurveySelectionDto
import com.example.deepsea.entity.UserProfile
import com.example.deepsea.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/survey")
class SurveySelectionController(private val userService: UserService) {
    @PostMapping("/save")
    fun saveSurveySelections(@RequestBody request: SurveySelectionDto): ResponseEntity<UserProfile> {
        val updatedProfile = userService.updateUserSurveySelections(
            userId = request.userId,
            surveySelections = request.surveyOptions
        )
        return ResponseEntity.ok(updatedProfile)
    }

}