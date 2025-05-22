package com.example.deepsea.controller

import com.example.deepsea.enums.SurveyOption
import com.example.deepsea.dto.*
import com.example.deepsea.entity.User
import com.example.deepsea.entity.UserProfile
import com.example.deepsea.service.UserProfileService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

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

    @PostMapping("/{userId}/add-xp")
    fun addXpToUser(
        @PathVariable userId: Long,
        @RequestParam amount: Int
    ): ResponseEntity<String> {
        return try {
            userProfileService.addXp(userId, amount)
            ResponseEntity.ok("XP added successfully")
        } catch (e: Exception) {
            ResponseEntity.badRequest().body("Failed to add XP: ${e.message}")
        }
    }

    @PutMapping("/{userId}/progress")
    fun updateProgress(
        @AuthenticationPrincipal user: User?,
        @PathVariable userId: Long,
        @RequestBody request: UpdateProgressRequest
    ): ResponseEntity<String> {
        if (user == null || user.id != userId) {
            return ResponseEntity.status(401).body("Unauthorized")
        }
        return try {
            userProfileService.updateProgress(userId, request.dailyStreak, LocalDate.parse(request.lastLogin.toString()))
            ResponseEntity.ok("Progress updated successfully")
        } catch (e: Exception) {
            ResponseEntity.badRequest().body("Failed to update progress: ${e.message}")
        }
    }
    @PutMapping("/{userId}/streak")
    fun updateDayStreak(
        @AuthenticationPrincipal currentUser: User?,
        @PathVariable userId: Long,
        @RequestBody request: DayStreakRequest
    ): ResponseEntity<String> {
        return try {
            userProfileService.updateDayStreak(userId, request.dayStreak)
            ResponseEntity.ok("Day streak updated to ${request.dayStreak}")
        } catch (e: Exception) {
            ResponseEntity.badRequest().body("Failed to update streak: ${e.message}")
        }
    }

}