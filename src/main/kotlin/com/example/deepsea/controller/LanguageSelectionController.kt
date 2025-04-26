package com.example.deepsea.controller

import com.example.deepsea.dto.LanguageSelectionDto
import com.example.deepsea.dto.SurveySelectionDto
import com.example.deepsea.model.UserProfile
import com.example.deepsea.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/language")
class LanguageSelectionController(private val userService: UserService) {
    @PostMapping("/save")
    fun saveLanguageSelection(@RequestBody request: LanguageSelectionDto): ResponseEntity<UserProfile> {
        val updatedProfile = userService.updateUserLanguageSelections(
            userId = request.userId,
            languageSelections = request.languageOptions
        )
        return ResponseEntity.ok(updatedProfile)
    }

}