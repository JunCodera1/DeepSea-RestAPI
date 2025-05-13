package com.example.deepsea.controller

import com.example.deepsea.dto.DailyGoalRequest
import com.example.deepsea.domain.enums.SurveyOption
import com.example.deepsea.dto.SurveySelectionDto
import com.example.deepsea.dto.UserProfileDto
import com.example.deepsea.model.User
import com.example.deepsea.model.UserProfile
import com.example.deepsea.service.UserProfileService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserProfileController(
    private val userProfileService: UserProfileService
) {

    @GetMapping("/{userId}")
    fun getProfile(@PathVariable userId: Long): ResponseEntity<UserProfile> {
        val profile = userProfileService.getUserProfile(userId)
        return ResponseEntity.ok(profile)
    }

    @GetMapping("/data/{userId}/profile")
    fun getProfileData(@PathVariable userId: Long): ResponseEntity<UserProfileDto> {
        val profileData = userProfileService.getUserProfileData(userId)
        return ResponseEntity.ok(profileData)
    }
    @PostMapping("/update-survey")
    fun updateSurveySelection(
        @AuthenticationPrincipal user: User?,
        @RequestBody request: SurveySelectionDto
    ): ResponseEntity<String> {
        if (user == null) return ResponseEntity.status(401).body("Unauthorized")

        val profile = user.profile ?: return ResponseEntity.notFound().build()

        val updatedSurveys = request.surveyOptions.mapNotNull {
            try {
                SurveyOption.valueOf(it.toString().uppercase())
            } catch (e: IllegalArgumentException) {
                null
            }
        }.toSet()

        // Remove this line since it's not used
        // val updatedProfile = profile.copy(selectedSurveys = updatedSurveys)

        userProfileService.updateSurveySelections(profile, updatedSurveys)
        return ResponseEntity.ok("Survey selections updated.")
    }

    @PutMapping("/{userId}/daily-goal")
    fun updateDailyGoal(
        @PathVariable userId: Long,
        @RequestBody request: DailyGoalRequest
    ): ResponseEntity<Unit> {
        return try {
            userProfileService.updateDailyGoal(userId, request.goal)
            ResponseEntity.ok().build()
        } catch (e: Exception) {
            ResponseEntity.badRequest().build()
        }
    }

}