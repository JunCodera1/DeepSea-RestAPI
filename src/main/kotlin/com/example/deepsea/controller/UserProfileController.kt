package com.example.deepsea.controller

import com.example.deepsea.dto.UserProfileDto
import com.example.deepsea.model.UserProfile
import com.example.deepsea.service.UserProfileService
import org.springframework.http.ResponseEntity
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
}